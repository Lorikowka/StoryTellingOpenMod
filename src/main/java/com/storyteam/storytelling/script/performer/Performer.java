package com.storyteam.storytelling.script.performer;

import com.google.common.base.Preconditions;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.network.StoryServerHooks;
import com.storyteam.storytelling.network.utils.WebMessageHandler;
import com.storyteam.storytelling.script.performer.action.LabelAction;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.action.SavePointAction;
import com.storyteam.storytelling.script.performer.action.SpeechSetEntityNameAction;
import com.storyteam.storytelling.script.performer.action.modificators.PerformerModifier;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.performer.state.State;
import com.storyteam.storytelling.script.performer.state.StateHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;

public class Performer<P extends Performer<P>> implements IPerformer<P>, IAutoNBTSerializable {
   public static final String START_STATE = "start";
   public static final String END_STATE = "final";
   protected String stage = "start";
   public final Set<String> savePointsNames = new HashSet<>();
   public final Set<String> labelPointsNames = new HashSet<>();
   protected final ArrayList<PerformerAction<P>> actions = new ArrayList<>();
   private StateHolder<P> states = new StateHolder<>((P)this);
   protected int actionIndex = 0;
   protected String selfKnownName;
   private String dirtyDeserializedStage = null;
   private boolean paused = false;
   private final Map<Integer, String> helpReports = new HashMap<>();

   public void clear() {
      this.actions.clear();
      this.actionIndex = 0;
   }

   public void setName(String name) {
      this.selfKnownName = name;
   }

   @Override
   public String getName() {
      return this.selfKnownName;
   }

   @Override
   public boolean isPaused() {
      return this.paused;
   }

   @Override
   public void setPaused(boolean paused) {
      this.paused = paused;
   }

   @Override
   public void addAction(PerformerAction<P> action) {
      if (action instanceof LabelAction) {
         String label = ((LabelAction)action).getName();
         Preconditions.checkArgument(
            !"start".equals(label) && !"final".equals(label), "Label name '" + label + "' is reserved by the performer implementation. Try using another name."
         );
         Preconditions.checkArgument(
            this.getStageActionIndex0(LabelAction.class, label) == -1, "Label name '" + label + "' is already taken. Try using another name."
         );
      }

      this.actions.add(action);
   }

   public void applyModifier(PerformerModifier<P> modifier) {
      modifier.modify((P)this, this.actions);
   }

   public StateHolder<P> getStateRepository() {
      return this.states;
   }

   public void setStateRepository(StateHolder<P> states) {
      this.states = states;
   }

   public <T extends State<P>> T getState(State.Type<P, T> type) {
      return this.states.getOrCreate(type);
   }

   @Override
   public boolean tick() {
      this.getHelpReports().clear();
      if (this.dirtyDeserializedStage != null) {
         String deserializedStage = this.dirtyDeserializedStage;
         this.dirtyDeserializedStage = null;
         this.stage = "start";
         this.jumpTo(this.getSaveStateActionIndex(deserializedStage), false);
      }

      if (this.paused) {
         return false;
      } else {
         for (int i = this.actionIndex; i < this.actions.size(); i++) {
            PerformerAction<?> action = this.actions.get(i);
            action.tick();
            this.gatherHelpReport();
            boolean completed = action.isDone();
            if (completed) {
               Storytelling.LOG.trace("Performer \"" + this.selfKnownName + "\" action done: " + action);

               for (int j = this.actionIndex; j <= i; j++) {
                  this.actions.get(j).stop();
               }

               this.actionIndex = i + 1;
            }

            if (!action.shouldContinue() && !completed) {
               break;
            }
         }

         this.states.tick();
         return this.actionIndex < this.actions.size();
      }
   }

   public String gatherHelpReport() {
      if (this.isCompleted()) {
         return "Скрипт завершён.";
      } else {
         HelpReportBuilder builder = new HelpReportBuilder();

         for (int i = this.actionIndex; i < this.actions.size(); i++) {
            PerformerAction<?> action = this.actions.get(i);
            if (i != this.actionIndex) {
               break;
            }

            action.gatherHelpReport(builder);
         }

         String report = builder.build();
         if (!report.isEmpty()) {
            this.helpReports.put(this.actionIndex, report);
         }

         if (report.isEmpty()) {
            report = "Нет уточняющей информации.";
         }

         return report;
      }
   }

   public String gatherInstructionReport() {
      if (this.isCompleted()) {
         return "";
      } else {
         HelpReportBuilder builder = new HelpReportBuilder();

         for (int i = this.actionIndex; i < this.actions.size(); i++) {
            PerformerAction<?> action = this.actions.get(i);
            if (i != this.actionIndex) {
               break;
            }

            if (this.getName().contains("_main")) {
               action.gatherInstruction(builder);
            }
         }

         return builder.build();
      }
   }

   @Override
   public boolean isCompleted() {
      return this.actionIndex >= this.actions.size();
   }

   @Override
   public <T> void handleEvent(EventType<T> type, T event) {
      this.states.handleEvent((P)this, type, event);

      for (int i = this.actionIndex; i < this.actions.size(); i++) {
         PerformerAction<?> action = this.actions.get(i);
         action.handleEvent(type, event);
         if (!action.shouldContinue()) {
            break;
         }
      }
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = IAutoNBTSerializable.super.serializeNBT();
      nbt.putString("stage", this.stage);
      nbt.putInt("actionIndex", this.actionIndex);
      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      Storytelling.LOG.info("Deserialization of performer \"" + this.selfKnownName + "\"...");
      String stage = nbt.getString("stage");
      if (!stage.equals("start")) {
         String oldStage = this.stage;

         try {
            this.stage = stage;
         } catch (IllegalArgumentException var5) {
            Storytelling.LOG.fatal("Failed to deserilalize performer: " + var5.getMessage());
         }

         if (!this.stage.equals(oldStage)) {
            this.dirtyDeserializedStage = stage;
         }
      }

      IAutoNBTSerializable.super.deserializeNBT(nbt);
      if (nbt.contains("actionIndex")) {
         this.actionIndex = Mth.clamp(nbt.getInt("actionIndex"), 0, this.actions.size());
         this.dirtyDeserializedStage = null;
         for (int i = 0; i < this.actionIndex; i++) {
            if (this.actions.get(i) instanceof SpeechSetEntityNameAction speaker) {
               speaker.fastForwardToStage();
            }
         }
      }
   }

   public void onScriptInstanceCreated() {
      for (PerformerAction<?> action : this.actions) {
         action.onScriptInstanceCreated();
      }
   }

   public void onScriptInstanceDestroyed() {
      this.states.onScriptInstanceDestroyed();
   }

   @Override
   public void onStart() {
      this.actionIndex = 0;
      this.savePointsNames.clear();
      this.labelPointsNames.clear();

      for (PerformerAction<?> action : this.actions) {
         action.onInit();
         if (action instanceof SavePointAction<?> savePointAction) {
            this.savePointsNames.add(savePointAction.getName());
         }

         if (action instanceof LabelAction<?> savePointAction) {
            this.labelPointsNames.add(savePointAction.getName());
         }
      }
   }

   @Override
   public void onStop() {
      this.savePointsNames.clear();
      this.labelPointsNames.clear();

      for (PerformerAction<?> action : this.actions) {
         action.onScriptStop();
      }

      this.states.onScriptStop();
   }

   public boolean containsStage(String name) {
      return name == null ? false : this.getStageActionIndex0(SavePointAction.class, name) != -1 || "start".equals(name) || "final".equals(name);
   }

   public boolean containsLabel(String name) {
      return name == null ? false : this.getStageActionIndex0(LabelAction.class, name) != -1 || "start".equals(name) || "final".equals(name);
   }

   private int getStageActionIndex0(@Nonnull Class<? extends LabelAction> clazz, @Nonnull String name) {
      for (int i = 0; i < this.actions.size(); i++) {
         PerformerAction<?> action = this.actions.get(i);
         if (clazz.isInstance(action) && name.equals(((LabelAction)action).getName())) {
            return i;
         }
      }

      return -1;
   }

   private int getSaveStateActionIndex(@Nonnull String name) {
      if ("start".equals(name)) {
         return 0;
      } else if ("final".equals(name)) {
         return this.actions.size();
      } else {
         int index = this.getStageActionIndex0(SavePointAction.class, name);
         if (index == -1) {
            throw new IllegalArgumentException("Save state '" + name + "' not found.");
         } else {
            return index;
         }
      }
   }

   private int getLabelActionIndex(@Nonnull String name) {
      if ("start".equals(name)) {
         return 0;
      } else if ("final".equals(name)) {
         return this.actions.size();
      } else {
         int index = this.getStageActionIndex0(LabelAction.class, name);
         if (index == -1) {
            throw new IllegalArgumentException("Label '" + name + "' not found.");
         } else {
            return index;
         }
      }
   }

   public boolean isBefore(String label) {
      int actionIndex = this.getLabelActionIndex(label);
      return this.actionIndex < actionIndex;
   }

   public boolean isAfter(String label) {
      int actionIndex = this.getLabelActionIndex(label);
      return this.actionIndex >= actionIndex;
   }

   public boolean isPassed(String label) {
      return this.isAfter(label);
   }

   public boolean isBetween(String label1, String label2) {
      int action1Index = this.getLabelActionIndex(label1);
      int action2Index = this.getLabelActionIndex(label2);
      int min = Math.min(action1Index, action2Index);
      int max = Math.max(action1Index, action2Index);
      return this.actionIndex >= min && this.actionIndex < max;
   }

   public String getStage() {
      return this.stage;
   }

   @Deprecated
   public void setStage(String stage) {
      this.setStage(stage, false);
   }

   @Deprecated
   public void setStage(String stage, boolean force) {
      if (force || !this.stage.equals(stage)) {
         int stageIndex = this.getSaveStateActionIndex(stage);
         this.stage = stage;
         this.actionIndex = stageIndex;
      }
   }

   public int getActionsAmount() {
      return this.actions.size();
   }

   @Override
   public int getActionIndex(PerformerAction<P> action) {
      return this.actions.indexOf(action);
   }

   public ArrayList<PerformerAction<P>> getActions() {
      return this.actions;
   }

   public void jumpTo(int dest, boolean withoutFastRun) {
      Preconditions.checkArgument(
         dest >= 0 && dest <= this.actions.size(), "Can't perform rewind. Save state '" + this.stage + "' located after current state."
      );
      int rewindFrom = Mth.clamp(this.actionIndex, 0, this.actions.size() - 1);

      for (int i = rewindFrom; i >= dest; i--) {
         PerformerAction<?> action = this.actions.get(i);
         action.reset0();
         if (this instanceof ScriptPerformer sp) {
            WebMessageHandler.removeInstructionLine(this.getName() + "_" + i, sp.script.getId());
         }

         if (!withoutFastRun) {
            action.rewindToStage();
         }
      }

      boolean wasRewound = rewindFrom >= dest;

      for (int i = wasRewound ? 0 : this.actionIndex; i < dest; i++) {
         PerformerAction<?> actionx = this.actions.get(i);
         if (!withoutFastRun) {
            this.actionIndex = i;
            actionx.fastForwardToStage();
            if (this instanceof ScriptPerformer sp) {
               HelpReportBuilder builder = new HelpReportBuilder();
               actionx.gatherHelpReport(builder);
               String data = builder.build();
               WebMessageHandler.sendInstructionLine(this.getName() + "_" + i, sp.script.getId(), data.isEmpty() ? "Нет уточняющей информации" : data, true);
            }
         }

         if (actionx instanceof SavePointAction savePointAction) {
            this.onStagePass(savePointAction.getName());
         }
      }

      this.actionIndex = dest;
      if (this instanceof ScriptPerformer sp) {
         StoryServerHooks.updateScript(sp.script, true, (double)dest / (double)this.actions.size() * 100.0);
      }
   }

   public void onStagePass(String labelName) {
      this.stage = labelName;
   }

   public final void gotoSavepoint(String name) {
      this.gotoSavepoint(name, false);
   }

   public void gotoSavepoint(String name, boolean withoutFastRun) {
      this.jumpTo(this.getSaveStateActionIndex(name), withoutFastRun);
   }

   public void gotoLabel(String name) {
      this.jumpTo(this.getLabelActionIndex(name), false);
   }

   public void gotoLabel(String name, boolean withoutFastRun) {
      this.jumpTo(this.getLabelActionIndex(name), withoutFastRun);
   }

   public void inheritFrom(P parent) {
      this.setStateRepository(parent.getStateRepository());
   }

   @Generated
   public int getActionIndex() {
      return this.actionIndex;
   }

   @Generated
   public Map<Integer, String> getHelpReports() {
      return this.helpReports;
   }
}



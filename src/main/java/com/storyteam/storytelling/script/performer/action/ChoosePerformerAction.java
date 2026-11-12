package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.performer.state.StateHolder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.Optional;
import java.util.Random;
import org.zeith.hammerlib.util.java.Cast;

public class ChoosePerformerAction<P extends Performer<P>> extends PerformerAction<P> {
   private static final Random RANDOM = new Random();
   private final Performer<P>[] performers;
   private Performer<P> chosen;

   public ChoosePerformerAction(P performer, Performer<P>[] performers) {
      super(performer);
      this.performers = (Performer<P>[])performers.clone();
   }

   public void resetChosen() {
      this.chosen = this.performers[RANDOM.nextInt(this.performers.length)];
   }

   @Override
   public void globalFastForward() {
      this.performers[RANDOM.nextInt(this.performers.length)].getActions().forEach(PerformerAction::globalFastForward);
   }

   @Override
   public void tick() {
      if (this.chosen == null) {
         this.resetChosen();
         this.chosen.inheritFrom(this.performer);
         this.chosen.onStart();
      }

      this.chosen.tick();
   }

   @Override
   public void fastForwardToStage() {
      if (this.chosen == null) {
         this.resetChosen();
         this.chosen.inheritFrom(this.performer);
         this.chosen.onStart();
      }

      this.chosen.gotoSavepoint("final");
   }

   @Override
   public void rewindToStage() {
      if (this.chosen == null) {
         this.resetChosen();
      }

      this.chosen.gotoSavepoint("start");
   }

   @Override
   public void reset() {
      this.chosen = null;
   }

   @Override
   public void onInit() {
      super.onInit();
      this.chosen = null;
   }

   @Override
   public void onScriptInstanceCreated() {
      int n = 1;

      for (Performer<?> innerPerformer : this.performers) {
         innerPerformer.setName(this.performer.getName() + "_" + this.performer.getActionIndex(this) + "_inner" + n++);
         ((Performer)innerPerformer).setStateRepository((StateHolder)this.performer.getStateRepository());
         innerPerformer.onScriptInstanceCreated();
      }
   }

   @Override
   public boolean isDone() {
      return this.chosen != null && this.chosen.isCompleted();
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (this.chosen == null) {
         this.resetChosen();
      }

      this.chosen.handleEvent(eventType, event);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append(
            "performers",
            Optional.ofNullable(this.chosen)
               .map(p -> (String)(p.getActions().isEmpty() ? "[Пустой перформер]" : (p.isCompleted() ? "[Завершен]" : p.getActions().get(p.getActionIndex()))))
               .orElse("[Перформер не инициалищирован]")
         )
         .build();
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      if (this.performers.length == 1 && this.performers[0] instanceof ScriptPerformer) {
         builder.append(this.performers[0].gatherHelpReport());
      } else if (this.chosen != null) {
         builder.append("Выполнение случайного перформера: " + this.chosen.gatherHelpReport());
      }

      return builder;
   }
}

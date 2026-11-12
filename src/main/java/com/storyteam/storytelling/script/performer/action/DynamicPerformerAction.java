package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.performer.state.StateHolder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Consumer;
import org.zeith.hammerlib.util.java.Cast;

public class DynamicPerformerAction extends PerformerAction<ScriptPerformer> {
   private Consumer<ScriptPerformerBuilder> builderConsumer;
   private boolean started;
   private ScriptPerformer dPerformer;

   public DynamicPerformerAction(ScriptPerformer performer, Consumer<ScriptPerformerBuilder> builderConsumer) {
      super(performer);
      this.builderConsumer = builderConsumer;
   }

   @Override
   public void onInit() {
      this.started = false;
   }

   @Override
   public void globalFastForward() {
      ScriptPerformerBuilder builder = new ScriptPerformerBuilder(this.performer.script);
      this.builderConsumer.accept(builder);

      for (PerformerAction<ScriptPerformer> action : builder.build().getActions()) {
         action.globalFastForward();
      }
   }

   @Override
   public void tick() {
      if (!this.started) {
         this.started = true;
         ScriptPerformerBuilder builder = new ScriptPerformerBuilder(this.performer.script);
         this.builderConsumer.accept(builder);
         this.dPerformer = builder.build();
         this.dPerformer.setName(this.performer.getName() + "_" + this.performer.getActionIndex(this) + "_inner1");
         this.dPerformer.setStateRepository((StateHolder<ScriptPerformer>)Cast.cast(this.performer.getStateRepository()));
         this.dPerformer.onScriptInstanceCreated();
         this.dPerformer.onStart();
      }

      this.dPerformer.tick();
   }

   @Override
   public void fastForwardToStage() {
      if (!this.started) {
         this.started = true;
         ScriptPerformerBuilder builder = new ScriptPerformerBuilder(this.performer.script);
         this.builderConsumer.accept(builder);
         this.dPerformer = builder.build();
         this.dPerformer.setName(this.performer.getName() + "_" + this.performer.getActionIndex(this) + "_inner1");
         this.dPerformer.setStateRepository((StateHolder<ScriptPerformer>)Cast.cast(this.performer.getStateRepository()));
         this.dPerformer.onScriptInstanceCreated();
      }

      this.dPerformer.gotoSavepoint("final");
   }

   @Override
   public void rewindToStage() {
      this.started = false;
      if (this.dPerformer != null) {
         this.dPerformer.gotoSavepoint("start");
      }
   }

   @Override
   public void reset() {
      this.started = false;
      this.dPerformer = null;
   }

   @Override
   public boolean isDone() {
      return this.started && this.dPerformer.isCompleted();
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (this.dPerformer != null) {
         this.dPerformer.handleEvent(eventType, event);
      }
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("dPerformer", this.dPerformer).build();
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      if (this.dPerformer == null) {
         builder.append("Динамический перформер не собран");
      } else {
         builder.append(this.dPerformer.gatherHelpReport());
      }

      return builder;
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      if (this.dPerformer != null) {
         builder.append(this.dPerformer.gatherInstructionReport());
      }

      return builder;
   }
}

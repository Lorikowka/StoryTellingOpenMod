package com.storyteam.storytelling.script.performer.action.modificators;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;

public abstract class PerformerActionDecorator<P extends Performer<P>> extends PerformerAction<P> {
   PerformerAction<P> innerAction;

   public PerformerActionDecorator(P performer, PerformerAction<P> innerAction) {
      super(performer);
      this.innerAction = innerAction;
   }

   @Override
   public void onScriptInstanceCreated() {
      this.innerAction.onScriptInstanceCreated();
   }

   @Override
   public void onScriptStop() {
      this.innerAction.onScriptStop();
   }

   @Override
   public boolean isDone() {
      return this.innerAction.isDone();
   }

   @Override
   public void tick() {
      this.innerAction.tick();
   }

   @Override
   public void onInit() {
      this.innerAction.onInit();
   }

   @Override
   public void fastForwardToStage() {
      this.innerAction.fastForwardToStage();
   }

   @Override
   public void rewindToStage() {
      this.innerAction.rewindToStage();
   }

   @Override
   public void reset() {
      this.innerAction.reset();
   }

   @Override
   public void stop() {
      this.innerAction.stop();
   }

   @Override
   public String toString() {
      return this.innerAction.toString();
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return this.innerAction.gatherHelpReport(builder);
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      return this.innerAction.gatherInstruction(builder);
   }

   @Override
   public boolean shouldContinue() {
      return this.innerAction.shouldContinue();
   }

   @Override
   public String getId() {
      return this.innerAction.getId();
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      this.innerAction.handleEvent(eventType, event);
   }
}

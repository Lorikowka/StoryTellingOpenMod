package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;

public class F10PressAction extends ScriptPerformerAction {
   private boolean isDone = false;

   public F10PressAction(ScriptPerformer performer) {
      super(performer);
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void reset() {
      this.isDone = false;
   }

   @Override
   public void fastForwardToStage() {
      this.isDone = true;
   }

   @Override
   public void rewindToStage() {
      this.isDone = false;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Клавиша F10.");
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      return builder.append("Клавиша F10.");
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (eventType.equals(EventType.F10_PRESS)) {
         this.isDone = true;
      }
   }
}

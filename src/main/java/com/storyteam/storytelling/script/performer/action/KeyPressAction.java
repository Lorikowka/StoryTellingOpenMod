package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;

public class KeyPressAction extends ScriptPerformerAction {
   private boolean isDone = false;
   private final int key;

   public KeyPressAction(ScriptPerformer performer, int key) {
      super(performer);
      this.key = key;
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
      return builder.append("Клавиша \"" + this.key + "\".");
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      return builder.append("Клавиша \"" + this.key + "\".");
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (eventType.equals(EventType.KEY_PRESS) && this.key == (Integer)event) {
         this.isDone = true;
      }
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;

public class TriggerAction extends ScriptPerformerAction {
   private boolean isDone;
   private final int delay;
   private boolean triggered;
   private int ticks = 0;

   public TriggerAction(ScriptPerformer performer, int delay) {
      super(performer);
      this.delay = delay;
   }

   @Override
   public void tick() {
      this.sendInstructionLine("Ожидание триггера: <span style=\"color: wheat\">/script trigger " + this.script.getId() + "</span>");
      if (this.triggered && !this.isDone) {
         if (this.ticks <= 0) {
            this.isDone = true;
         } else {
            this.ticks--;
         }
      }
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void reset() {
      this.isDone = false;
      this.triggered = false;
      this.ticks = 0;
   }

   @Override
   public void fastForwardToStage() {
      this.sendInstructionLine("Ожидание триггера: <span style=\"color: wheat\">/script trigger " + this.script.getId() + "</span>");
      this.isDone = true;
      this.ticks = 0;
      this.triggered = true;
   }

   @Override
   public void rewindToStage() {
      this.isDone = false;
      this.ticks = 0;
      this.triggered = false;
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (eventType.equals(EventType.TRIGGER) && (Integer)event == this.performer.script.getId()) {
         if (this.delay > 0) {
            this.ticks = this.delay;
            this.triggered = true;
         } else {
            this.isDone = true;
         }
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Вызов триггера.");
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      return builder.append("Ожидание триггера");
   }
}

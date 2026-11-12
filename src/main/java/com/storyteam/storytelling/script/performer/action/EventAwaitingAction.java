package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EventAwaitingAction<T> extends ScriptPerformerAction {
   private final EventType<T> type;
   private final Predicate<T> eventFilter;
   private final Supplier<String> stHelpInfo;
   private boolean completed;

   public EventAwaitingAction(ScriptPerformer performer, EventType<T> type, Supplier<String> stHelpInfo) {
      this(performer, type, null, stHelpInfo);
   }

   public EventAwaitingAction(ScriptPerformer performer, EventType<T> type, Predicate<T> eventFilter, Supplier<String> stHelpInfo) {
      super(performer);
      this.type = type;
      this.eventFilter = eventFilter;
      this.stHelpInfo = stHelpInfo;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      if (this.stHelpInfo != null) {
         String i = this.stHelpInfo.get();
         if (i != null && !i.isEmpty()) {
            builder.append(i);
         }
      } else {
         builder.append("Ожидание события " + this.type.eventType() + " (" + this.type.type().getSimpleName() + ")");
      }

      return builder;
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      if (this.stHelpInfo != null) {
         String i = this.stHelpInfo.get();
         if (i != null && !i.isEmpty()) {
            builder.append(i);
         }
      } else {
         builder.append("Ожидание события " + this.type.eventType() + " (" + this.type.type().getSimpleName() + ")");
      }

      return builder;
   }

   @Override
   public void fastForwardToStage() {
      this.completed = true;
   }

   @Override
   public void reset() {
      this.completed = false;
   }

   @Override
   public void rewindToStage() {
      this.completed = false;
   }

   @Override
   public <E> void handleEvent(EventType<E> type, E event) {
      if (!this.completed && type == this.type && (this.eventFilter == null || this.eventFilter.test((T)event))) {
         this.completed = true;
      }
   }

   @Override
   public boolean isDone() {
      return this.completed;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("type", this.type.eventType()).append("eventFilter", this.eventFilter).build();
   }
}

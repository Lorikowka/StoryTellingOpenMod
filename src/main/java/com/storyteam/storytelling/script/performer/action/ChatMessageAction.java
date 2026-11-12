package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ChatMessageAction extends ScriptPerformerAction {
   private final Predicate<String> filter;
   private final Supplier<String> filterInfo;
   private final boolean wait;
   private boolean isDone;
   private int delay = 0;

   public ChatMessageAction(ScriptPerformer performer, Predicate<String> filter, Supplier<String> filterInfo, boolean wait) {
      super(performer);
      this.filter = filter;
      this.filterInfo = filterInfo;
      this.wait = wait;
   }

   public ChatMessageAction(ScriptPerformer performer, Predicate<String> filter) {
      this(performer, filter, null, true);
   }

   @Override
   public void tick() {
      String in = this.filter != null ? (this.filterInfo != null ? this.filterInfo.get() : null) : "";
      if (in != null) {
         this.sendInstructionLine("Ввод текста в чат: <span style=\"color: wheat\">" + in + "</span>.");
      }
   }

   @Override
   public boolean isDone() {
      if (this.isDone) {
         if (this.delay > 0) {
            this.delay--;
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void reset() {
      this.isDone = false;
      this.delay = 0;
   }

   @Override
   public void fastForwardToStage() {
      String in = this.filter != null ? (this.filterInfo != null ? this.filterInfo.get() : null) : "";
      if (in != null) {
         this.sendInstructionLine("Ввод текста в чат: <span style=\"color: wheat\">" + in + "</span>.");
      }

      this.isDone = true;
      this.delay = 0;
   }

   @Override
   public void rewindToStage() {
      this.isDone = false;
      this.delay = 0;
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (eventType.equals(EventType.CHAT_MESSAGE_SENT) && (this.filter == null || this.filter.test(String.valueOf(event)))) {
         this.isDone = true;
         if (this.wait) {
            this.delay = 10;
         }
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      String in = this.filter != null ? (this.filterInfo != null ? this.filterInfo.get() : null) : "";
      builder.append("Ввод текста в чат" + (in != null ? ": " + in : "") + ".");
      return builder;
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      String in = this.filter != null ? (this.filterInfo != null ? this.filterInfo.get() : null) : "";
      builder.append("Ввод текста в чат" + (in != null ? ": " + in : "") + ".");
      return builder;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("filter", this.filter).build();
   }
}

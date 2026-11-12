package com.storyteam.storytelling.system.time.performer.task;

import com.google.common.base.Preconditions;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;

public class WaitUntilAction<P extends Performer<P>> extends PerformerAction<P> {
   private final Supplier<Boolean> condition;
   private final int checkInterval;
   private boolean completed;
   private Supplier<String> stHelpInfo;

   public WaitUntilAction(P performer, Supplier<Boolean> condition, int checkInterval) {
      super(performer);
      Preconditions.checkArgument(checkInterval >= 0, "The interval can't be negative.");
      this.condition = condition;
      this.checkInterval = checkInterval;
   }

   public WaitUntilAction(P performer, Supplier<Boolean> condition, int checkInterval, Supplier<String> stHelpInfo) {
      this(performer, condition, checkInterval);
      this.stHelpInfo = stHelpInfo;
   }

   @Override
   public void tick() {
      if (!this.completed && (this.checkInterval == 0 || ScriptUtils.server().getTickCount() % (this.checkInterval + 1) == 0)) {
         this.completed = this.condition.get();
      }
   }

   @Override
   public void reset() {
      this.completed = false;
   }

   @Override
   public void fastForwardToStage() {
      this.completed = true;
   }

   @Override
   public void rewindToStage() {
      this.completed = false;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      if (this.stHelpInfo != null) {
         String i = this.stHelpInfo.get();
         if (i != null && !i.isEmpty()) {
            builder.append(i);
         }
      } else {
         builder.append("Ожидание действия.");
      }

      return builder;
   }

   @Override
   public boolean isDone() {
      return this.completed;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("checkInterval", this.checkInterval).append("condition", this.condition).build();
   }
}

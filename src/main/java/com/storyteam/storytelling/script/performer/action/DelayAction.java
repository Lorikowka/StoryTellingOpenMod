package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.IntSupplier;

public class DelayAction<P extends Performer<P>> extends PerformerAction<P> {
   private final int delay;
   private int ticks;

   public DelayAction(P performer, IntSupplier ticks) {
      super(performer);
      this.delay = ticks.getAsInt();
      this.ticks = ticks.getAsInt();
   }

   @Override
   public void tick() {
      this.ticks--;
   }

   @Override
   public void reset() {
      this.ticks = this.delay;
   }

   @Override
   public void rewindToStage() {
      this.ticks = this.delay;
   }

   @Override
   public boolean isDone() {
      return this.ticks < 0;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Ожидание %d тиков. Осталось: %d", this.delay, this.ticks);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("delay", this.delay).build();
   }
}

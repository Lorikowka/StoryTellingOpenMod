package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.IntSupplier;

public class RandomDelayAction<P extends Performer<P>> extends PerformerAction<P> {
   private int delay;
   private int ticks;
   IntSupplier ticksSup;
   boolean init;

   public RandomDelayAction(P performer, IntSupplier ticks) {
      super(performer);
      this.delay = ticks.getAsInt();
      this.ticks = ticks.getAsInt();
      this.ticksSup = ticks;
      this.init = false;
   }

   @Override
   public void tick() {
      if (!this.init) {
         this.delay = this.ticksSup.getAsInt();
         this.ticks = this.ticksSup.getAsInt();
         this.init = true;
      }

      this.ticks--;
   }

   @Override
   public void reset() {
      this.delay = this.ticksSup.getAsInt();
      this.ticks = this.ticksSup.getAsInt();
      this.init = true;
   }

   @Override
   public void rewindToStage() {
      this.delay = this.ticksSup.getAsInt();
      this.ticks = this.ticksSup.getAsInt();
      this.init = true;
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

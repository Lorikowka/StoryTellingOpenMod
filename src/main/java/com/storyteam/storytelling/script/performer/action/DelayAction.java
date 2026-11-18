package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.IntSupplier;
import net.minecraft.nbt.CompoundTag;

public class DelayAction<P extends Performer<P>> extends PerformerAction<P> implements ActionCheckpoint {
   private int delay;
   private int ticks;

   public DelayAction(P performer, IntSupplier ticks) {
      super(performer);
      this.delay = ticks.getAsInt();
      this.ticks = this.delay;
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

   @Override
   public CompoundTag saveActionState() {
      CompoundTag state = new CompoundTag();
      state.putInt("delay", this.delay);
      state.putInt("remaining", this.ticks);
      return state;
   }

   @Override
   public void restoreActionState(CompoundTag state) {
      if (state.contains("delay") && state.contains("remaining")) {
         this.delay = state.getInt("delay");
         this.ticks = state.getInt("remaining");
      }
   }
}

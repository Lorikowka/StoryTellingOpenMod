package com.storyteam.storytelling.system.simulator.me_tab.fragment.data.time;

public class DurationData {
   private final int days;
   private final int hours;
   private final int minutes;

   public DurationData(int days, int hours, int minutes) {
      if (hours >= 0 && minutes >= 0 && days >= 0) {
         this.days = days;
         this.hours = hours;
         this.minutes = minutes;
      } else {
         throw new IllegalArgumentException("Не негативь время");
      }
   }

   public long toTotalTicks() {
      long ticksPerMinute = 16L;
      return (long)this.days * 24000L + (long)this.hours * 1000L + (long)Math.round((float)((long)this.minutes * ticksPerMinute));
   }

   public static DurationData fromTotalTicks(long ticks) {
      long days = ticks / 24000L;
      ticks %= 24000L;
      long hours = ticks / 1000L;
      ticks %= 1000L;
      long minutes = Math.round((double)ticks * 60.0 / 1000.0);
      return new DurationData((int)days, (int)hours, (int)minutes);
   }

   @Override
   public String toString() {
      return this.days + "d " + this.hours + "h " + this.minutes + "m";
   }
}

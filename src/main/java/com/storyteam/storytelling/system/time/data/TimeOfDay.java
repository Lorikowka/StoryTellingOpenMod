package com.storyteam.storytelling.system.time.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;

public final class TimeOfDay implements Comparable<TimeOfDay>, IAutoNBTSerializable {
   public static final int TICKS_PER_DAY = 24000;
   public static final int DAY_START_OFFSET = 6000;
   private static final Pattern TIME_PATTERN = Pattern.compile("\\d{1,2}:\\d{2}");
   public static final TimeOfDay DAY_START = parse("00:00");
   public static final TimeOfDay DAY_END = parse("23:59");
   private final long time;

   public TimeOfDay(long time) {
      this.time = (long)Math.floorMod(time, 24000);
   }

   public static TimeOfDay parse(String time) {
      Preconditions.checkArgument(TIME_PATTERN.matcher(time).matches(), "Time must have format hh:mm.");
      String[] parts = time.split(":");
      int hours = Integer.parseInt(parts[0]);
      int minutes = Integer.parseInt(parts[1]);
      long ticks = (long)((double)((long)hours * 1000L) + (double)minutes * 16.666666666666668);
      ticks -= 6000L;
      if (ticks < 0L) {
         ticks += 24000L;
      }

      return new TimeOfDay(ticks);
   }

   public static boolean between(long ticks, TimeOfDay before, TimeOfDay after, boolean inclusive) {
      return between(new TimeOfDay(ticks), before, after, inclusive);
   }

   public static boolean between(TimeOfDay time, TimeOfDay before, TimeOfDay after, boolean inclusive) {
      boolean reversed = before.time > after.time;
      if (reversed) {
         inclusive = !inclusive;
         TimeOfDay tmp = before;
         before = after;
         after = tmp;
      }

      Range<TimeOfDay> range = inclusive ? Range.closed(before, after) : Range.open(before, after);
      return !reversed == range.contains(time);
   }

   public TimeOfDay add(long ticks) {
      return new TimeOfDay(this.time + ticks);
   }

   public TimeOfDay add(TimeOfDay other) {
      return this.add(other.time);
   }

   public TimeOfDay sub(long ticks) {
      return new TimeOfDay(this.time - ticks);
   }

   public TimeOfDay sub(TimeOfDay other) {
      return this.sub(other.time);
   }

   public TimeOfDay diff(TimeOfDay other) {
      TimeOfDay forward = this.sub(other);
      TimeOfDay backward = other.sub(this);
      return Math.abs(backward.time) > Math.abs(forward.time) ? forward : backward;
   }

   public String getAsString() {
      long ticks = (this.time + 6000L) % 24000L;
      long hours = ticks / 1000L;
      long minutes = (long)((int)(ticks % 1000L * 60L / 1000L));
      return String.format("%02d:%02d", hours, minutes);
   }

   public String getAsStringWithSeconds() {
      long ticks = (this.time + 6000L) % 24000L;
      long hours = ticks / 1000L;
      long minutes = ticks % 1000L * 60L / 1000L;
      long seconds = ticks % 1000L * 3600L / 1000L % 60L;
      seconds = Math.round((double)seconds / 10.0) * 10L;
      if (seconds == 60L) {
         seconds = 0L;
         if (++minutes == 60L) {
            minutes = 0L;
            hours = (hours + 1L) % 24L;
         }
      }

      return String.format("%02d:%02d:%02d", hours, minutes, seconds);
   }

   public long time() {
      return this.time;
   }

   public int compareTo(@NotNull TimeOfDay o) {
      return Long.compare(this.time, o.time);
   }

   @Override
   public int hashCode() {
      return Long.hashCode(this.time);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else {
         if (obj instanceof TimeOfDay other && this.time == other.time) {
            return true;
         }

         return false;
      }
   }

   @Override
   public String toString() {
      return "TimeOfDay[time=" + this.time + "]";
   }
}

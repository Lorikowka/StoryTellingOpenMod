package com.storyteam.storytelling.system.simulator.me_tab.fragment.data.time;

import com.storyteam.storytelling.system.time.data.TimeOfDay;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class DayTimeData implements Comparable<DayTimeData>, IAutoNBTSerializable {
   @NBTSerializable
   private int day;
   private TimeOfDay time;

   public DayTimeData(int day, TimeOfDay time) {
      this.day = day;
      this.time = time;
   }

   public long toTotalTicks() {
      return (long)this.day * 24000L + this.time.time();
   }

   public static DayTimeData fromTotalTicks(long ticks) {
      int d = (int)(ticks / 24000L);
      long t = ticks % 24000L;
      return new DayTimeData(d, new TimeOfDay(t));
   }

   public DayTimeData add(DurationData duration) {
      return fromTotalTicks(this.toTotalTicks() + duration.toTotalTicks());
   }

   public long diffInTicks(DayTimeData other) {
      return Math.abs(this.toTotalTicks() - other.toTotalTicks());
   }

   public int compareTo(DayTimeData o) {
      return Long.compare(this.toTotalTicks(), o.toTotalTicks());
   }

   @Override
   public String toString() {
      return "Day " + this.day + " " + this.time.getAsString();
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = IAutoNBTSerializable.super.serializeNBT();
      tag.putLong("time", this.time.time());
      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      IAutoNBTSerializable.super.deserializeNBT(nbt);
      this.time = new TimeOfDay(nbt.getLong("time"));
   }

   @Generated
   public int getDay() {
      return this.day;
   }

   @Generated
   public TimeOfDay getTime() {
      return this.time;
   }
}

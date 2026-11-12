package com.storyteam.storytelling.system.time.core;

import com.storyteam.storytelling.system.time.data.TimeOfDay;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.Range;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;

public class TimeCondition implements IAutoNBTSerializable {
   private TimeCondition.Type type;
   private long value;
   private long argumentTime;

   public TimeCondition(@Nullable TimeController controller) {
      this(controller, 0L, TimeCondition.Type.RELATIVE_TIME);
   }

   public TimeCondition(@Nullable TimeController controller, @Nullable Long time, @NotNull TimeCondition.Type type) {
      long baseTime = time != null ? time : (controller != null ? controller.getTime() : 0L);
      this.argumentTime = baseTime;
      this.type = type;
      switch (type) {
         case RELATIVE_TIME:
            this.value = requireController(controller).getTime() + baseTime;
            break;
         case DAY_TIME:
            this.value = new TimeOfDay(baseTime).time();
            break;
         case ABSOLUTE_TIME:
            this.value = baseTime;
            break;
         case INFINITY:
            this.value = 0L;
            break;
         default:
            throw new IllegalArgumentException("Unsupported type: " + type);
      }
   }

   public TimeCondition(long time, TimeCondition.Type type) {
      this(null, time, type);
   }

   public TimeCondition(@Nullable Long time) {
      this(null, time, TimeCondition.Type.DAY_TIME);
   }

   public TimeCondition(TimeOfDay time) {
      this(time.time(), TimeCondition.Type.DAY_TIME);
   }

   public TimeCondition(String time) {
      this(TimeOfDay.parse(time));
   }

   public static TimeCondition infinity(TimeController controller) {
      return new TimeCondition(controller, 0L, TimeCondition.Type.INFINITY);
   }

   public static TimeCondition infinity() {
      return new TimeCondition(0L, TimeCondition.Type.INFINITY);
   }

   private static TimeController requireController(@Nullable TimeController controller) {
      if (controller == null) {
         throw new IllegalStateException("TimeController required for RELATIVE_TIME");
      } else {
         return controller;
      }
   }

   public boolean test(long prevTime, long time) {
      return switch (this.type) {
         case DAY_TIME -> this.testDayTime(time);
         case INFINITY -> false;
         default -> this.testRange(prevTime, time);
      };
   }

   private boolean testDayTime(long time) {
      if (time < TimeController.get().getTime()) {
         return false;
      } else {
         return Math.abs(time - TimeController.get().getTime()) >= 24000L
            ? true
            : TimeOfDay.between(this.value, new TimeOfDay(TimeController.get().getTime()), new TimeOfDay(time), true);
      }
   }

   private boolean testRange(long prevTime, long time) {
      return prevTime < time == Range.between(prevTime, time).contains(this.value);
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      if (this.type != null) {
         tag.putString("Type", this.type.name());
      }

      tag.putLong("Value", this.value);
      tag.putLong("ArgumentTime", this.argumentTime);
      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      if (nbt.contains("Type")) {
         try {
            this.type = TimeCondition.Type.valueOf(nbt.getString("Type"));
         } catch (IllegalArgumentException var3) {
            this.type = TimeCondition.Type.ABSOLUTE_TIME;
         }
      } else {
         this.type = TimeCondition.Type.ABSOLUTE_TIME;
      }

      if (nbt.contains("Value")) {
         this.value = nbt.getLong("Value");
      } else {
         this.value = 0L;
      }

      if (nbt.contains("ArgumentTime")) {
         this.argumentTime = nbt.getLong("ArgumentTime");
      } else {
         this.argumentTime = 0L;
      }
   }

   @Generated
   public TimeCondition.Type getType() {
      return this.type;
   }

   @Generated
   public long getArgumentTime() {
      return this.argumentTime;
   }

   public static enum Type {
      DAY_TIME,
      ABSOLUTE_TIME,
      RELATIVE_TIME,
      INFINITY;
   }
}

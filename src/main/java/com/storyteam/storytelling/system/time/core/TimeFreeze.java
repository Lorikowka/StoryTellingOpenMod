package com.storyteam.storytelling.system.time.core;

import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class TimeFreeze implements IAutoNBTSerializable {
   @NBTSerializable
   @NotNull
   private final TimeCondition lockCondition;
   @NotNull
   private TimeController controller = TimeController.get();
   @NBTSerializable
   private boolean locked;
   @NBTSerializable
   private Long savedTime;
   @NBTSerializable
   private long startTime;

   public TimeFreeze(@Nullable TimeController controller, @NotNull CompoundTag tag) {
      this.lockCondition = TimeCondition.infinity();
      if (controller != null) {
         this.controller = controller;
      }

      IAutoNBTSerializable.super.deserializeNBT(tag);
   }

   public TimeFreeze(@Nullable TimeController controller) {
      if (controller != null) {
         this.controller = controller;
      }

      this.locked = true;
      this.lockCondition = new TimeCondition(this.controller);
      this.startTime = this.controller.getTime();
   }

   public TimeFreeze(@Nullable TimeController controller, boolean locked, @Nullable TimeCondition lockCondition) {
      if (controller != null) {
         this.controller = controller;
      }

      this.locked = locked;
      this.lockCondition = Objects.requireNonNullElseGet(lockCondition, () -> new TimeCondition(this.controller));
      this.startTime = this.controller.getTime();
   }

   public void onEnd(boolean restoreTime) {
      if (restoreTime) {
         this.controller.setTime(this.startTime);
      }

      this.controller.setDayTimeRule(false);
   }

   public void tick() {
      if (this.locked) {
         if (this.savedTime == null) {
            this.savedTime = this.controller.getTime();
         }

         this.controller.setDayTimeRule(true);
         this.controller.setTime(this.savedTime);
      }

      int offerDay = TributeOfferStorage.getOfferDay();
      int currentDay = TimeController.get().getCurrentDay();
      boolean isTributeDeadline = offerDay <= currentDay;
      this.locked = this.lockCondition.test(this.startTime, this.controller.getTime()) || isTributeDeadline;
      if (this.locked) {
         this.savedTime = this.controller.getTime();
      }
   }

   @Generated
   public static TimeFreeze.TimeFreezeBuilder builder() {
      return new TimeFreeze.TimeFreezeBuilder();
   }

   @NotNull
   @Generated
   public TimeCondition getLockCondition() {
      return this.lockCondition;
   }

   @NotNull
   @Generated
   public TimeController getController() {
      return this.controller;
   }

   @Generated
   public boolean isLocked() {
      return this.locked;
   }

   @Generated
   public Long getSavedTime() {
      return this.savedTime;
   }

   @Generated
   public long getStartTime() {
      return this.startTime;
   }

   @Generated
   public void setController(@NotNull TimeController controller) {
      if (controller == null) {
         throw new NullPointerException("controller is marked non-null but is null");
      } else {
         this.controller = controller;
      }
   }

   @Generated
   public void setLocked(boolean locked) {
      this.locked = locked;
   }

   @Generated
   public void setSavedTime(Long savedTime) {
      this.savedTime = savedTime;
   }

   @Generated
   public void setStartTime(long startTime) {
      this.startTime = startTime;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TimeFreeze other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.isLocked() != other.isLocked()) {
         return false;
      } else if (this.getStartTime() != other.getStartTime()) {
         return false;
      } else {
         Object this$savedTime = this.getSavedTime();
         Object other$savedTime = other.getSavedTime();
         if (this$savedTime == null ? other$savedTime == null : this$savedTime.equals(other$savedTime)) {
            Object this$lockCondition = this.getLockCondition();
            Object other$lockCondition = other.getLockCondition();
            if (this$lockCondition == null ? other$lockCondition == null : this$lockCondition.equals(other$lockCondition)) {
               Object this$controller = this.getController();
               Object other$controller = other.getController();
               return this$controller == null ? other$controller == null : this$controller.equals(other$controller);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof TimeFreeze;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + (this.isLocked() ? 79 : 97);
      long $startTime = this.getStartTime();
      result = result * 59 + (int)($startTime >>> 32 ^ $startTime);
      Object $savedTime = this.getSavedTime();
      result = result * 59 + ($savedTime == null ? 43 : $savedTime.hashCode());
      Object $lockCondition = this.getLockCondition();
      result = result * 59 + ($lockCondition == null ? 43 : $lockCondition.hashCode());
      Object $controller = this.getController();
      return result * 59 + ($controller == null ? 43 : $controller.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "TimeFreeze(lockCondition="
         + this.getLockCondition()
         + ", controller="
         + this.getController()
         + ", locked="
         + this.isLocked()
         + ", savedTime="
         + this.getSavedTime()
         + ", startTime="
         + this.getStartTime()
         + ")";
   }

   @Generated
   public static class TimeFreezeBuilder {
      @Generated
      private TimeController controller;
      @Generated
      private boolean locked;
      @Generated
      private TimeCondition lockCondition;

      @Generated
      TimeFreezeBuilder() {
      }

      @Generated
      public TimeFreeze.TimeFreezeBuilder controller(@Nullable TimeController controller) {
         this.controller = controller;
         return this;
      }

      @Generated
      public TimeFreeze.TimeFreezeBuilder locked(boolean locked) {
         this.locked = locked;
         return this;
      }

      @Generated
      public TimeFreeze.TimeFreezeBuilder lockCondition(@Nullable TimeCondition lockCondition) {
         this.lockCondition = lockCondition;
         return this;
      }

      @Generated
      public TimeFreeze build() {
         return new TimeFreeze(this.controller, this.locked, this.lockCondition);
      }

      @Generated
      @Override
      public String toString() {
         return "TimeFreeze.TimeFreezeBuilder(controller=" + this.controller + ", locked=" + this.locked + ", lockCondition=" + this.lockCondition + ")";
      }
   }
}

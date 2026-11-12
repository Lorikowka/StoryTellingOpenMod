package com.storyteam.storytelling.system.time.core;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.system.time.data.TimeOfDay;
import com.storyteam.storytelling.system.time.event.NewDayStartEvent;
import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class TimeTask implements IAutoNBTSerializable {
   @NBTSerializable
   private final TimeCondition checker;
   private final TimeController controller;
   @NBTSerializable
   private final boolean sendUpdateEvent;
   @NBTSerializable
   private final boolean lockOnEnd;
   private double speedFactor;
   private double speed;
   @NBTSerializable
   private boolean stopped;
   @NBTSerializable
   private long creationTime;
   @NBTSerializable
   private double partialTime;
   @NBTSerializable
   private int priority;

   public TimeTask(TimeController controller, CompoundTag tag) {
      this.controller = controller;
      this.checker = TimeCondition.infinity();
      this.sendUpdateEvent = false;
      this.lockOnEnd = false;
      this.deserializeNBT(tag);
   }

   private TimeTask(
      TimeController controller, TimeCondition checker, double speed, double speedFactor, boolean sendUpdateEvent, boolean lockOnEnd, int priority
   ) {
      this.controller = controller;
      this.checker = checker;
      this.speed = speed;
      this.speedFactor = speedFactor;
      this.sendUpdateEvent = sendUpdateEvent;
      this.lockOnEnd = lockOnEnd;
      this.priority = priority;
   }

   public static TimeTask.Builder builder(TimeController controller) {
      return new TimeTask.Builder(controller);
   }

   public static TimeTask.Builder builder() {
      return new TimeTask.Builder();
   }

   public void tick() {
      if (!this.stopped) {
         long previousTime = this.controller.getTime();
         int offerDay = TributeOfferStorage.getOfferDay();
         int currentDay = TimeController.get().getCurrentDay();
         boolean isTributeDeadline = offerDay <= currentDay && !TributeOfferStorage.getOffer().isEmpty();
         this.partialTime = this.partialTime + this.speed * this.speedFactor;
         if (this.sendUpdateEvent) {
            this.triggerUpdateEvent();
         }

         int wholeTicks = (int)this.partialTime;
         this.partialTime -= (double)wholeTicks;
         long newTime = previousTime + (long)wholeTicks;
         long previousTimeWithOffset = (previousTime + 6000L) % 24000L;
         long newTimeWithOffset = (newTime + 6000L) % 24000L;
         this.controller.setTime(newTime);
         if (previousTimeWithOffset > newTimeWithOffset) {
            if (isTributeDeadline) {
               this.controller.setTime(previousTime);
               return;
            }

            this.onNewDayStarted();
         }

         if (this.sendUpdateEvent) {
            this.triggerUpdateEvent();
         }
      }
   }

   private void onNewDayStarted() {
      int previousDay = TimeController.get().getCurrentDay();
      int currentDay = previousDay + 1;
      TimeController.get().incrementCurrentDay(1);
      NewDayStartEvent event = new NewDayStartEvent(previousDay, currentDay);
      MinecraftForge.EVENT_BUS.post(event);
      IStoryServer.get().postStoryEvent(EventType.NEW_DAY_START, event);
   }

   public void timeOverride(long newTime) {
      if (!this.isStopped() && this.checker.test(this.creationTime, newTime)) {
         this.stopped = true;
         this.controller.lockTime();
      }
   }

   private void triggerUpdateEvent() {
   }

   @Generated
   public TimeCondition getChecker() {
      return this.checker;
   }

   @Generated
   public TimeController getController() {
      return this.controller;
   }

   @Generated
   public double getSpeedFactor() {
      return this.speedFactor;
   }

   @Generated
   public void setSpeedFactor(double speedFactor) {
      this.speedFactor = speedFactor;
   }

   @Generated
   public double getSpeed() {
      return this.speed;
   }

   @Generated
   public void setSpeed(double speed) {
      this.speed = speed;
   }

   @Generated
   public boolean isStopped() {
      return this.stopped;
   }

   @Generated
   public void setStopped(boolean stopped) {
      this.stopped = stopped;
   }

   @Generated
   public long getCreationTime() {
      return this.creationTime;
   }

   @Generated
   public void setCreationTime(long creationTime) {
      this.creationTime = creationTime;
   }

   @Generated
   public int getPriority() {
      return this.priority;
   }

   public static class Builder {
      private TimeController controller;
      private TimeCondition stopCondition;
      private double speed = 0.1;
      private double speedFactor = 0.3;
      private boolean sendUpdateEvent = true;
      private boolean lockOnEnd = true;
      private int priority = 0;

      private Builder(TimeController controller) {
         this.controller = controller;
         this.stopCondition = TimeCondition.infinity(controller);
      }

      private Builder() {
         this(TimeController.get());
         this.stopCondition = TimeCondition.infinity();
      }

      public TimeTask.Builder controller(TimeController controller) {
         this.controller = controller;
         return this;
      }

      public TimeTask.Builder stopAt(long absoluteTicks) {
         this.stopCondition = new TimeCondition(absoluteTicks, TimeCondition.Type.ABSOLUTE_TIME);
         return this;
      }

      public TimeTask.Builder stopAt(TimeCondition condition) {
         this.stopCondition = condition;
         return this;
      }

      public TimeTask.Builder lockOnEnd(boolean lock) {
         this.lockOnEnd = lock;
         return this;
      }

      public TimeTask.Builder stopAtRelative(long deltaTicks) {
         this.stopCondition = new TimeCondition(deltaTicks, TimeCondition.Type.RELATIVE_TIME);
         return this;
      }

      public TimeTask.Builder stopAtDayTime(TimeOfDay timeOfDay) {
         this.stopCondition = new TimeCondition(timeOfDay);
         return this;
      }

      public TimeTask.Builder speed(double value) {
         this.speed = value;
         return this;
      }

      public TimeTask.Builder speedFactor(double factor) {
         this.speedFactor = factor;
         return this;
      }

      public TimeTask.Builder updateEvent(boolean enabled) {
         this.sendUpdateEvent = enabled;
         return this;
      }

      public TimeTask.Builder priority(int priority) {
         this.priority = priority;
         return this;
      }

      public TimeTask build() {
         return new TimeTask(this.controller, this.stopCondition, this.speed, this.speedFactor, this.sendUpdateEvent, this.lockOnEnd, this.priority);
      }
   }
}

package com.storyteam.storytelling.system.time.event;

import lombok.Generated;
import net.minecraftforge.eventbus.api.Event;

public class NewDayStartEvent extends Event {
   private final int previousDay;
   private final int currentDay;

   public NewDayStartEvent(int previousDay, int currentDay) {
      this.previousDay = previousDay;
      this.currentDay = currentDay;
   }

   @Generated
   public int getPreviousDay() {
      return this.previousDay;
   }

   @Generated
   public int getCurrentDay() {
      return this.currentDay;
   }
}

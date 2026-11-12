package com.storyteam.storytelling.system.time.data;

import lombok.Generated;

public class TimeMark {
   private final TimeOfDay timeOfDay;
   private final boolean setTime;

   public TimeMark(TimeOfDay timeOfDay, boolean setTime) {
      this.timeOfDay = timeOfDay;
      this.setTime = setTime;
   }

   @Generated
   public TimeOfDay getTimeOfDay() {
      return this.timeOfDay;
   }

   @Generated
   public boolean isSetTime() {
      return this.setTime;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TimeMark other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.isSetTime() != other.isSetTime()) {
         return false;
      } else {
         Object this$timeOfDay = this.getTimeOfDay();
         Object other$timeOfDay = other.getTimeOfDay();
         return this$timeOfDay == null ? other$timeOfDay == null : this$timeOfDay.equals(other$timeOfDay);
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof TimeMark;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + (this.isSetTime() ? 79 : 97);
      Object $timeOfDay = this.getTimeOfDay();
      return result * 59 + ($timeOfDay == null ? 43 : $timeOfDay.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "TimeMark(timeOfDay=" + this.getTimeOfDay() + ", setTime=" + this.isSetTime() + ")";
   }
}

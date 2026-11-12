package com.storyteam.storytelling.content.entities.configurations;

public enum MovementActionType {
   RUNNING(0.42),
   FAST_WALKING(0.32),
   WALKING(0.0),
   IDLE(-1.0);

   final double minSpeedExclusive;

   private MovementActionType(double minSpeedExclusive) {
      this.minSpeedExclusive = minSpeedExclusive;
   }

   public boolean test(double speed) {
      return speed > this.minSpeedExclusive;
   }

   public static MovementActionType getBySpeed(double speed) {
      for (MovementActionType type : values()) {
         if (type.test(speed)) {
            return type;
         }
      }

      return IDLE;
   }
}

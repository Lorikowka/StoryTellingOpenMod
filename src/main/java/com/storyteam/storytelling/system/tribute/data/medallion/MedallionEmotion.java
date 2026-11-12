package com.storyteam.storytelling.system.tribute.data.medallion;

import lombok.Generated;

public enum MedallionEmotion {
   CALM(75, 100),
   SERIOUS(50, 75),
   FROWN(25, 50),
   AGGRESSIVE(0, 25);

   private final int minPercent;
   private final int maxPercent;

   public static MedallionEmotion byPercent(float percent) {
      if (percent < 0.0F) {
         percent = 0.0F;
      }

      if (percent > 100.0F) {
         percent = 100.0F;
      }

      for (MedallionEmotion e : values()) {
         if (percent >= (float)e.minPercent && percent <= (float)e.maxPercent) {
            return e;
         }
      }

      return CALM;
   }

   @Generated
   public int getMinPercent() {
      return this.minPercent;
   }

   @Generated
   public int getMaxPercent() {
      return this.maxPercent;
   }

   @Generated
   private MedallionEmotion(int minPercent, int maxPercent) {
      this.minPercent = minPercent;
      this.maxPercent = maxPercent;
   }
}

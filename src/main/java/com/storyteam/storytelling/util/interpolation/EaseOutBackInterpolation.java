package com.storyteam.storytelling.util.interpolation;

public class EaseOutBackInterpolation extends Interpolation {
   private static final float c1 = 1.70158F;
   private static final float c3 = 2.70158F;

   @Override
   public float interpolate(float x) {
      return 1.0F + 2.70158F * (float)Math.pow((double)(x - 1.0F), 3.0) + 1.70158F * (float)Math.pow((double)(x - 1.0F), 2.0);
   }
}

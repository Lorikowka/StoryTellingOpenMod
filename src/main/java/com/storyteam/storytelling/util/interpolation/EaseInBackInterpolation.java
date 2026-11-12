package com.storyteam.storytelling.util.interpolation;

public class EaseInBackInterpolation extends Interpolation {
   private static final float c1 = 1.70158F;
   private static final float c3 = 2.70158F;

   @Override
   public float interpolate(float x) {
      return 2.70158F * x * x * x - 1.70158F * x * x;
   }
}

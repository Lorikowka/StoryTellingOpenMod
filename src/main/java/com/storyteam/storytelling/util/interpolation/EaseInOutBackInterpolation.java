package com.storyteam.storytelling.util.interpolation;

public class EaseInOutBackInterpolation extends Interpolation {
   private static final float c1 = 1.70158F;
   private static final float c2 = 2.5949094F;

   @Override
   public float interpolate(float x) {
      return x < 0.5F
         ? (float)Math.pow((double)(2.0F * x), 2.0) * (7.189819F * x - 2.5949094F) / 2.0F
         : ((float)Math.pow((double)(2.0F * x - 2.0F), 2.0) * (3.5949094F * (x * 2.0F - 2.0F) + 2.5949094F) + 2.0F) / 2.0F;
   }
}

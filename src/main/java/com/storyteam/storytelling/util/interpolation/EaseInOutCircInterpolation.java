package com.storyteam.storytelling.util.interpolation;

public class EaseInOutCircInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x < 0.5F
         ? (1.0F - (float)Math.sqrt(1.0 - Math.pow((double)(2.0F * x), 2.0))) / 2.0F
         : ((float)Math.sqrt(1.0 - Math.pow((double)(-2.0F * x + 2.0F), 2.0)) + 1.0F) / 2.0F;
   }
}

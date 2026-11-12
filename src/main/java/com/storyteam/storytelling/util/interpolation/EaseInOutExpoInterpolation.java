package com.storyteam.storytelling.util.interpolation;

public class EaseInOutExpoInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x == 0.0F
         ? 0.0F
         : (
            x == 1.0F
               ? 1.0F
               : (x < 0.5F ? (float)Math.pow(2.0, (double)(20.0F * x - 10.0F)) / 2.0F : (2.0F - (float)Math.pow(2.0, (double)(-20.0F * x + 10.0F))) / 2.0F)
         );
   }
}

package com.storyteam.storytelling.util.interpolation;

public class EaseOutExpoInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x == 1.0F ? 1.0F : 1.0F - (float)Math.pow(2.0, (double)(-10.0F * x));
   }
}

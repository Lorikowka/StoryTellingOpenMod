package com.storyteam.storytelling.util.interpolation;

public class EaseInOutCubicInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x < 0.5F ? 4.0F * x * x * x : 1.0F - (float)Math.pow((double)(-2.0F * x + 2.0F), 3.0) / 2.0F;
   }
}

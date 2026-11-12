package com.storyteam.storytelling.util.interpolation;

public class EaseInOutQuartInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x < 0.5F ? 8.0F * x * x * x * x : 1.0F - (float)Math.pow((double)(-2.0F * x + 2.0F), 4.0) / 2.0F;
   }
}

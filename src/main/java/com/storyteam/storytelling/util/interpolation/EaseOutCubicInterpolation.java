package com.storyteam.storytelling.util.interpolation;

public class EaseOutCubicInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      float f = x - 1.0F;
      return f * f * f + 1.0F;
   }
}

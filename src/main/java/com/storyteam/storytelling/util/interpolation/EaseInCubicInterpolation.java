package com.storyteam.storytelling.util.interpolation;

public class EaseInCubicInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x * x * x;
   }
}

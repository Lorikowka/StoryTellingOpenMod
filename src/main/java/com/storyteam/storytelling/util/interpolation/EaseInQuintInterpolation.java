package com.storyteam.storytelling.util.interpolation;

public class EaseInQuintInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x * x * x * x * x;
   }
}

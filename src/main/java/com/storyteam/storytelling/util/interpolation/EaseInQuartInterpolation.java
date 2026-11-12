package com.storyteam.storytelling.util.interpolation;

public class EaseInQuartInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x * x * x * x;
   }
}

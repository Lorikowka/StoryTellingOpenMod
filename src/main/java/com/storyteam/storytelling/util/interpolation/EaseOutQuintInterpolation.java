package com.storyteam.storytelling.util.interpolation;

public class EaseOutQuintInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return 1.0F - (float)Math.pow((double)(1.0F - x), 5.0);
   }
}

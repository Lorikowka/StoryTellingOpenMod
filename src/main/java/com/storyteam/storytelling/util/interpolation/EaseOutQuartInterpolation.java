package com.storyteam.storytelling.util.interpolation;

public class EaseOutQuartInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return 1.0F - (float)Math.pow((double)(1.0F - x), 4.0);
   }
}

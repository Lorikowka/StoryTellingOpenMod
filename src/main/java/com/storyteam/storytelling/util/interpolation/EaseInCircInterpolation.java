package com.storyteam.storytelling.util.interpolation;

public class EaseInCircInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return 1.0F - (float)Math.sqrt(1.0 - Math.pow((double)x, 2.0));
   }
}

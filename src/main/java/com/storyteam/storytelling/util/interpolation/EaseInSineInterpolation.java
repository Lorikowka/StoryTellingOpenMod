package com.storyteam.storytelling.util.interpolation;

public class EaseInSineInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return 1.0F - (float)Math.cos((double)x * Math.PI / 2.0);
   }
}

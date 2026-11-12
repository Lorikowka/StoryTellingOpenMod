package com.storyteam.storytelling.util.interpolation;

public class EaseOutCircInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return (float)Math.sqrt(1.0 - Math.pow((double)(x - 1.0F), 2.0));
   }
}

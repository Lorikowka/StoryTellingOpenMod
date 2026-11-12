package com.storyteam.storytelling.util.interpolation;

public class EaseOutSineInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return (float)Math.sin((double)x * Math.PI / 2.0);
   }
}

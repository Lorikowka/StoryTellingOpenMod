package com.storyteam.storytelling.util.interpolation;

public class EaseOutQuadInterpolation extends Interpolation {
   @Override
   public float interpolate(float p) {
      return 1.0F - (1.0F - p) * (1.0F - p);
   }
}

package com.storyteam.storytelling.util.interpolation;

public class EaseInQuadInterpolation extends Interpolation {
   @Override
   public float interpolate(float p) {
      return p * p;
   }
}

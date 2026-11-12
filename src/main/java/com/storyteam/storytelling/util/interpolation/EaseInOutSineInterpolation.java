package com.storyteam.storytelling.util.interpolation;

public class EaseInOutSineInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return -((float)(Math.cos(Math.PI * (double)x) - 1.0)) / 2.0F;
   }
}

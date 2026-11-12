package com.storyteam.storytelling.util.interpolation;

public class EaseInExpoInterpolation extends Interpolation {
   @Override
   public float interpolate(float x) {
      return x == 0.0F ? 0.0F : (float)Math.pow(2.0, (double)(10.0F * x - 10.0F));
   }
}

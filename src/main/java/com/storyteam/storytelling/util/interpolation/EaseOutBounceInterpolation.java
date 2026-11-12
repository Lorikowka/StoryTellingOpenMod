package com.storyteam.storytelling.util.interpolation;

public class EaseOutBounceInterpolation extends Interpolation {
   @Override
   public float interpolate(float p) {
      float n1 = 7.5625F;
      float d1 = 2.75F;
      if (p < 1.0F / d1) {
         return n1 * p * p;
      } else if (p < 2.0F / d1) {
         float var6;
         return (float)((double)(n1 * (var6 = p - (float)(1.5 / (double)d1)) * var6) + 0.75);
      } else {
         float var4;
         float var5;
         return (double)p < 2.5 / (double)d1
            ? (float)((double)(n1 * (var4 = p - (float)(2.25 / (double)d1)) * var4) + 0.9375)
            : (float)((double)(n1 * (var5 = p - (float)(2.625 / (double)d1)) * var5) + 0.984375);
      }
   }
}

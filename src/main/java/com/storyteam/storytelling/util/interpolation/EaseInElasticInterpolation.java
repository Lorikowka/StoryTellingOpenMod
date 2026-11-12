package com.storyteam.storytelling.util.interpolation;

public class EaseInElasticInterpolation extends Interpolation {
   private static final float c4 = (float) (Math.PI * 2.0 / 3.0);

   @Override
   public float interpolate(float x) {
      return x == 0.0F
         ? 0.0F
         : (
            x == 1.0F
               ? 1.0F
               : -((float)Math.pow(2.0, (double)(10.0F * x - 10.0F))) * (float)Math.sin(((double)(x * 10.0F) - 10.75) * (float) (Math.PI * 2.0 / 3.0))
         );
   }
}

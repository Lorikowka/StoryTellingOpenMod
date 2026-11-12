package com.storyteam.storytelling.util.interpolation;

public class EaseInOutElasticInterpolation extends Interpolation {
   private static final float c5 = (float) Math.PI * 4.0F / 9.0F;

   @Override
   public float interpolate(float x) {
      return x == 0.0F
         ? 0.0F
         : (
            x == 1.0F
               ? 1.0F
               : (
                  x < 0.5F
                     ? -((float)Math.pow(2.0, (double)(20.0F * x - 10.0F)))
                        * (float)Math.sin((double)((20.0F * x - 11.125F) * ((float) Math.PI * 4.0F / 9.0F)))
                        / 2.0F
                     : (float)Math.pow(2.0, (double)(-20.0F * x + 10.0F))
                           * (float)Math.sin((double)((20.0F * x - 11.125F) * ((float) Math.PI * 4.0F / 9.0F)))
                           / 2.0F
                        + 1.0F
               )
         );
   }
}

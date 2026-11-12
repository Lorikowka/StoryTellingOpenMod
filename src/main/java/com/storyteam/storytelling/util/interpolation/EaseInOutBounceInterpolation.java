package com.storyteam.storytelling.util.interpolation;

import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;

public class EaseInOutBounceInterpolation extends Interpolation {
   @Override
   public float interpolate(float p) {
      Interpolation interpolation = InterpolationType.EASE_OUT_BOUNCE.interpolationFactory.get();
      return (double)p < 0.5 ? (1.0F - interpolation.interpolate(1.0F - 2.0F * p)) / 2.0F : (1.0F + interpolation.interpolate(2.0F * p - 1.0F)) / 2.0F;
   }
}

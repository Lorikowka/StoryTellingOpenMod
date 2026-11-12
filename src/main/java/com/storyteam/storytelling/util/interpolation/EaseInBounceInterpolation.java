package com.storyteam.storytelling.util.interpolation;

import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;

public class EaseInBounceInterpolation extends Interpolation {
   @Override
   public float interpolate(float p) {
      return 1.0F - InterpolationType.EASE_OUT_BOUNCE.interpolationFactory.get().interpolate(1.0F - p);
   }
}

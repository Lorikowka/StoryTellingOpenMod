package com.storyteam.storytelling.system._core.st_cutscene;

import com.storyteam.storytelling.util.interpolation.EaseInBackInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInBounceInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInCircInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInCubicInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInElasticInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInExpoInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutBackInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutBounceInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutCircInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutCubicInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutElasticInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutExpoInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutQuadInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutQuartInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutQuintInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInOutSineInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInQuadInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInQuartInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInQuintInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseInSineInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutBackInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutBounceInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutCircInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutCubicInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutElasticInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutExpoInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutQuadInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutQuartInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutQuintInterpolation;
import com.storyteam.storytelling.util.interpolation.EaseOutSineInterpolation;
import com.storyteam.storytelling.util.interpolation.Interpolation;
import com.storyteam.storytelling.util.interpolation.LinearInterpolation;
import java.util.function.Supplier;

public enum InterpolationType {
   LINEAR(LinearInterpolation::new),
   EASE_IN_SINE(EaseInSineInterpolation::new),
   EASE_OUT_SINE(EaseOutSineInterpolation::new),
   EASE_IN_OUT_SINE(EaseInOutSineInterpolation::new),
   EASE_IN_QUAD(EaseInQuadInterpolation::new),
   EASE_OUT_QUAD(EaseOutQuadInterpolation::new),
   EASE_IN_OUT_QUAD(EaseInOutQuadInterpolation::new),
   EASE_IN_CUBIC(EaseInCubicInterpolation::new),
   EASE_OUT_CUBIC(EaseOutCubicInterpolation::new),
   EASE_IN_OUT_CUBIC(EaseInOutCubicInterpolation::new),
   EASE_IN_QUART(EaseInQuartInterpolation::new),
   EASE_OUT_QUART(EaseOutQuartInterpolation::new),
   EASE_IN_OUT_QUART(EaseInOutQuartInterpolation::new),
   EASE_IN_QUINT(EaseInQuintInterpolation::new),
   EASE_OUT_QUINT(EaseOutQuintInterpolation::new),
   EASE_IN_OUT_QUINT(EaseInOutQuintInterpolation::new),
   EASE_IN_EXPO(EaseInExpoInterpolation::new),
   EASE_OUT_EXPO(EaseOutExpoInterpolation::new),
   EASE_IN_OUT_EXPO(EaseInOutExpoInterpolation::new),
   EASE_IN_CIRC(EaseInCircInterpolation::new),
   EASE_OUT_CIRC(EaseOutCircInterpolation::new),
   EASE_IN_OUT_CIRC(EaseInOutCircInterpolation::new),
   EASE_IN_BACK(EaseInBackInterpolation::new),
   EASE_OUT_BACK(EaseOutBackInterpolation::new),
   EASE_IN_OUT_BACK(EaseInOutBackInterpolation::new),
   EASE_IN_ELASTIC(EaseInElasticInterpolation::new),
   EASE_OUT_ELASTIC(EaseOutElasticInterpolation::new),
   EASE_IN_OUT_ELASTIC(EaseInOutElasticInterpolation::new),
   EASE_IN_BOUNCE(EaseInBounceInterpolation::new),
   EASE_OUT_BOUNCE(EaseOutBounceInterpolation::new),
   EASE_IN_OUT_BOUNCE(EaseInOutBounceInterpolation::new);

   public Supplier<Interpolation> interpolationFactory;

   private InterpolationType(Supplier<Interpolation> interpolationFactory) {
      this.interpolationFactory = interpolationFactory;
   }

   public static float lerp(float start_value, float end_value, float pct) {
      return start_value + (end_value - start_value) * pct;
   }
}

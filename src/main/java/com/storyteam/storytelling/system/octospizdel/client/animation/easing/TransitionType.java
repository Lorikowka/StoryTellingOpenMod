package com.storyteam.storytelling.system.octospizdel.client.animation.easing;

import java.util.function.Function;
import net.minecraft.util.Mth;

public enum TransitionType {
   LINEAR(t -> t),
   SINE(t -> 1.0 - Math.cos(t * Math.PI / 2.0)),
   QUAD(t -> t * t),
   CUBIC(t -> t * t * t),
   QUART(t -> t * t * t * t),
   QUINT(t -> t * t * t * t * t),
   EXPO(t -> t == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * t - 10.0)),
   CIRC(t -> 1.0 - Math.sqrt(1.0 - Math.pow(t, 2.0))),
   BACK(t -> {
      double c1 = 1.70158;
      double c3 = c1 + 1.0;
      return c3 * t * t * t - c1 * t * t;
   }),
   ELASTIC(t -> {
      double c4 = Math.PI * 2.0 / 3.0;
      return t == 0.0 ? 0.0 : (t == 1.0 ? 1.0 : -Math.pow(2.0, 10.0 * t - 10.0) * Math.sin((t * 10.0 - 10.75) * c4));
   }),
   BOUNCE(t -> 1.0 - applyEaseOutBounce(1.0 - t));

   private final Function<Double, Double> function;

   private TransitionType(Function<Double, Double> function) {
      this.function = function;
   }

   public double apply(EaseType ease, double t) {
      return ease.apply(this.function, t);
   }

   public double runEquation(EaseType easeType, double time, double initial, double delta, double duration) {
      if (duration == 0.0) {
         return initial + delta;
      } else {
         double progress = Mth.clamp(time / duration, 0.0, 1.0);
         double factor = this.apply(easeType, progress);
         return initial + delta * factor;
      }
   }

   private static double applyEaseOutBounce(double t) {
      double n1 = 7.5625;
      double d1 = 2.75;
      if (t < 1.0 / d1) {
         return n1 * t * t;
      } else if (t < 2.0 / d1) {
         t -= 1.5 / d1;
         return n1 * t * t + 0.75;
      } else if (t < 2.5 / d1) {
         t -= 2.25 / d1;
         return n1 * t * t + 0.9375;
      } else {
         t -= 2.625 / d1;
         return n1 * t * t + 0.984375;
      }
   }
}

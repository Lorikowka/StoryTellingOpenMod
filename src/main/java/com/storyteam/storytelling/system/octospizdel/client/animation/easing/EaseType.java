package com.storyteam.storytelling.system.octospizdel.client.animation.easing;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum EaseType {
   EASE_IN(Function::apply),
   EASE_OUT((f, d) -> 1.0 - f.apply(1.0 - d)),
   EASE_IN_OUT((f, d) -> d < 0.5 ? 0.5 * f.apply(2.0 * d) : 0.5 * (1.0 - f.apply(2.0 * (1.0 - d))) + 0.5),
   LINEAR((f, t) -> t);

   private final BiFunction<Function<Double, Double>, Double, Double> function;

   private EaseType(BiFunction<Function<Double, Double>, Double, Double> function) {
      this.function = function;
   }

   public double apply(Function<Double, Double> function, double t) {
      return this.function.apply(function, t);
   }
}

package com.storyteam.storytelling.system.octospizdel.client.animation.easing;

@FunctionalInterface
public interface Interpolator<T> {
   T lerp(T var1, T var2, double var3);
}

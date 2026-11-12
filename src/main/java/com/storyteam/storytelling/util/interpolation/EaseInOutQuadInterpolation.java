package com.storyteam.storytelling.util.interpolation;

import net.minecraft.util.Mth;

public class EaseInOutQuadInterpolation extends Interpolation {
   @Override
   public float interpolate(float p) {
      return (double)p < 0.5 ? Mth.clamp(2.0F * p * p, 0.0F, 1.0F) : Mth.clamp(-2.0F * (float)Math.pow((double)(p - 1.0F), 2.0) + 1.0F, 0.0F, 1.0F);
   }
}

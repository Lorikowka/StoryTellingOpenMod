package com.storyteam.storytelling.client.core;

import org.zeith.hammeranims.api.animation.IAnimationContainer;

public interface AnimationStorage {
   SidedAnimationSet registerSidedSeparate(String var1, String var2);

   SidedAnimationSet registerSided(String var1, String var2, String var3);

   IAnimationContainer register(String var1);
}

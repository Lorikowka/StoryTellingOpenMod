package com.storyteam.storytelling.mixins._client.minecraft;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Particle.class})
public interface ParticleAccessor {
   @Accessor
   void setAlpha(float var1);

   @Accessor
   double getXo();

   @Accessor
   double getYo();

   @Accessor
   double getZo();

   @Accessor
   double getX();

   @Accessor
   double getY();

   @Accessor
   double getZ();
}

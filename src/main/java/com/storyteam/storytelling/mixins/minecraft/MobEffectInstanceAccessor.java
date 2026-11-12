package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MobEffectInstance.class})
public interface MobEffectInstanceAccessor {
   @Accessor
   void setDuration(int var1);

   @Accessor
   void setAmplifier(int var1);
}

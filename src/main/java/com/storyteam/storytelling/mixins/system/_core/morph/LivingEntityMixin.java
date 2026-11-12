package com.storyteam.storytelling.mixins.system._core.morph;

import com.storyteam.storytelling.init.seasonal.MorphsST8;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public class LivingEntityMixin {
   @Inject(
      method = {"onClimbable"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onClimbable(CallbackInfoReturnable<Boolean> cir) {
      LivingEntity deez = (LivingEntity)(Object)this;
      if (deez instanceof IAnimatedPlayer anime && anime.getMorph().equals(MorphsST8.SPOODER)) {
         cir.setReturnValue(false);
      }
   }
}

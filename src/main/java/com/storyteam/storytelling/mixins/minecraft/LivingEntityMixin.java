package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.events.base.CheckHurtEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.util.java.Cast;

@Mixin({LivingEntity.class})
public class LivingEntityMixin {
   @Inject(
      method = {"getHurtSound"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getHurtSound(DamageSource pSource, CallbackInfoReturnable<SoundEvent> cir) {
      if (pSource != null && Storytelling.BUS.post(new CheckHurtEvent((LivingEntity)Cast.cast(this), CheckHurtEvent.TestCase.HURT_SOUND))) {
         cir.setReturnValue(null);
         cir.cancel();
      }
   }
}

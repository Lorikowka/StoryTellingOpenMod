package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.events.base.CheckHurtEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public abstract class GameRendererMixin_BobHurt {
   @Shadow
   @Final
   Minecraft minecraft;

   @Inject(
      method = {"bobHurt"},
      cancellable = true,
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;getLastDamageSource()Lnet/minecraft/world/damagesource/DamageSource;"
      )}
   )
   public void bobHurt(PoseStack pPoseStack, float pPartialTicks, CallbackInfo ci) {
      if (this.minecraft.getCameraEntity() instanceof LivingEntity living
         && Storytelling.BUS.post(new CheckHurtEvent(living, CheckHurtEvent.TestCase.BOB_HURT))) {
         ci.cancel();
      }
   }
}

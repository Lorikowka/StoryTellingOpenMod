package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.proxy.STClientProxy;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ParticleEngine.class})
public class ParticleEngineMixin {
   @Inject(
      method = {"add"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void add_ST(Particle pEffect, CallbackInfo ci) {
      if (STClientProxy.isFreezeParticles() && pEffect != null && !STClientProxy.UNFREEZE_PARTICLES.contains(pEffect.getClass())) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"tickParticle"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tick_ST(Particle pParticle, CallbackInfo ci) {
      if (STClientProxy.isFreezeParticles() && pParticle != null && !STClientProxy.UNFREEZE_PARTICLES.contains(pParticle.getClass())) {
         ci.cancel();
      }
   }

   @ModifyVariable(
      method = {"render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V"},
      at = @At("HEAD"),
      index = 5,
      argsOnly = true,
      remap = false
   )
   private float render_ST(float value) {
      return STClientProxy.isFreezeParticles() ? 1.0F : value;
   }

   @Redirect(
      method = {"render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"
      )
   )
   private void renderParticleRedirect(Particle instance, VertexConsumer vertexConsumer, Camera camera, float v) {
      if (STClientProxy.isFreezeParticles() && STClientProxy.UNFREEZE_PARTICLES.contains(instance.getClass())) {
         v = Minecraft.getInstance().getPartialTick();
      }

      instance.render(vertexConsumer, camera, v);
   }
}

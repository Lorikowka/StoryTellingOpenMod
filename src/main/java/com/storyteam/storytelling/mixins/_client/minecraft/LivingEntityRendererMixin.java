package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.render.entity.IAlphaRenderer;
import com.storyteam.storytelling.events.base.CheckHurtEvent;
import com.storyteam.storytelling.system._core.st_cutscene.ClientsideCutsceneHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin({LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<M extends EntityModel, T extends LivingEntity> {
   @Shadow
   protected M model;

   @Redirect(
      method = {"render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
      )
   )
   public void renderAlpha(
      EntityModel instance, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLightIn, int packetOverlayOut, float r, float g, float b, float a
   ) {
      if (this instanceof IAlphaRenderer) {
         ((IAlphaRenderer)this).renderAlpha(instance, poseStack, vertexConsumer, packedLightIn, packetOverlayOut, r, g, b, a);
      } else {
         instance.renderToBuffer(poseStack, vertexConsumer, packedLightIn, packetOverlayOut, r, g, b, a);
      }
   }

   @Inject(
      method = {"getModel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderEntityInInventoryST(CallbackInfoReturnable<M> cir) {
      if (IStoryServer.optionally().isPresent() && (Object)this instanceof PlayerRenderer && IStoryServer.get().getScriptEnv().scriptInstances.containsKey(-1320)) {
      }
   }

   @Inject(
      method = {"getOverlayCoords"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void getOverlayCoords(LivingEntity living, float pU, CallbackInfoReturnable<Integer> cir) {
      if (living.getLastDamageSource() != null && Storytelling.BUS.post(new CheckHurtEvent(living, CheckHurtEvent.TestCase.HURT_OVERLAY))) {
         cir.setReturnValue(OverlayTexture.NO_OVERLAY);
      }
   }

   @Inject(
      method = {"render*"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRender(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
      if (!(entity instanceof Player)) {
         if (Minecraft.getInstance().player != null && ClientsideCutsceneHandler.currentData != null && entity.isInvisible()) {
            ci.cancel();
         }
      }
   }
}

package com.storyteam.storytelling.mixins.system._core.morph;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.system._core.morph.client.IPlayerMultiBufferRenderer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
   @Shadow
   protected M model;
   @Final
   @Shadow
   @Mutable
   protected List<RenderLayer<T, M>> layers;
   @Unique
   protected List<RenderLayer<T, M>> st$tmpLayers;

   @Shadow
   protected abstract float getWhiteOverlayProgress(T var1, float var2);

   @Shadow
   public static int getOverlayCoords(LivingEntity pLivingEntity, float pU) {
      return 0;
   }

   @Shadow
   protected abstract boolean isBodyVisible(T var1);

   protected LivingEntityRendererMixin(Context pContext) {
      super(pContext);
   }

   @Inject(
      method = {"render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
      )}
   )
   private void Minitelling_renderCustom(
      T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci
   ) {
      if (this.model instanceof IPlayerMultiBufferRenderer mb && pEntity instanceof Player pl) {
         Minecraft minecraft = Minecraft.getInstance();
         boolean flag = this.isBodyVisible(pEntity);
         boolean flag1 = !flag && !pEntity.isInvisibleTo(minecraft.player);
         int i = getOverlayCoords(pEntity, this.getWhiteOverlayProgress(pEntity, pPartialTicks));
         mb.mtRenderToBuffer(pMatrixStack, pBuffer, pPackedLight, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F, pl, true);
         if (!mb.renderLayers()) {
            this.st$tmpLayers = this.layers;
            this.layers = List.of();
         }
      }
   }

   @Inject(
      method = {"render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"
      )}
   )
   private void Storytelling_resetSpectator(
      T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci
   ) {
      if (this.st$tmpLayers != null) {
         this.layers = this.st$tmpLayers;
         this.st$tmpLayers = null;
      }
   }
}

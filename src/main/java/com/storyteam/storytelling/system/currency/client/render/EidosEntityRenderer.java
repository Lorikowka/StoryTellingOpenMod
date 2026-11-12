package com.storyteam.storytelling.system.currency.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.render.entity.CommonRenderer;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.system.currency.content.entities.EidosEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class EidosEntityRenderer extends CommonRenderer<EidosEntity> {
   public EidosEntityRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, boolean alwaysRenderer) {
      super(manager, new SimpleBedrockModel(modelConfiguration), shadowSize, texture);
   }

   protected int getBlockLightLevel(EidosEntity p_114606_, BlockPos p_114607_) {
      return Mth.clamp(super.getBlockLightLevel(p_114606_, p_114607_) + 9, 0, 15);
   }

   public void render(EidosEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      poseStack.pushPose();
      Quaternionf cameraOrientation = this.entityRenderDispatcher.cameraOrientation();
      poseStack.mulPose(cameraOrientation);
      poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
      poseStack.translate(-0.015F, 0.0F, 0.2F);
      poseStack.scale(0.4F, 0.4F, 0.4F);
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
      poseStack.popPose();
   }

   protected void renderNameTag(EidosEntity entity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
   }

   public RenderType getRenderType(EidosEntity p_114597_) {
      return RenderType.entityTranslucentEmissive(this.getTextureLocation(p_114597_));
   }

   protected RenderType getRenderType(EidosEntity pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
      return this.getRenderType(pLivingEntity);
   }
}

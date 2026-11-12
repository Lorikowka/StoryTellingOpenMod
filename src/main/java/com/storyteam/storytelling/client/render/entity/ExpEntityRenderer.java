package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system.exp.entities.ExpEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class ExpEntityRenderer extends EntityRenderer<ExpEntity> {
   public ExpEntityRenderer(Context p_174110_) {
      super(p_174110_);
      this.shadowRadius = 0.0F;
      this.shadowStrength = 0.0F;
   }

   protected int getBlockLightLevel(ExpEntity p_114606_, BlockPos p_114607_) {
      return Mth.clamp(super.getBlockLightLevel(p_114606_, p_114607_) + 9, 0, 15);
   }

   public void render(ExpEntity p_114599_, float p_114600_, float p_114601_, PoseStack poseStack, MultiBufferSource p_114603_, int p_114604_) {
      poseStack.pushPose();
      float f8 = ((float)p_114599_.tickCount + p_114601_) / 8.0F;
      float scale = 1.0F + 0.1F * Mth.sin(f8 + (float)p_114599_.getId());
      poseStack.scale(scale, scale, scale);
      poseStack.translate(0.0F, 0.1F, 0.0F);
      poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
      poseStack.scale(0.4F, 0.4F, 0.4F);
      VertexConsumer vertexconsumer = p_114603_.getBuffer(this.getRenderType(p_114599_));
      Pose posestack$pose = poseStack.last();
      Matrix4f matrix4f = posestack$pose.pose();
      Matrix3f matrix3f = posestack$pose.normal();
      float minU = 0.0F;
      float maxU = 1.0F;
      float minV = 0.0F;
      float maxV = 1.0F;
      int r = 255;
      int g = 255;
      int b = 255;
      vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, -0.5F, r, g, b, minU, maxV, p_114604_);
      vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, -0.5F, r, g, b, maxU, maxV, p_114604_);
      vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, 0.5F, r, g, b, maxU, minV, p_114604_);
      vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, 0.5F, r, g, b, minU, minV, p_114604_);
      poseStack.popPose();
      super.render(p_114599_, p_114600_, p_114601_, poseStack, p_114603_, p_114604_);
   }

   private static void vertex(
      VertexConsumer vertexConsumer,
      Matrix4f matrix4f,
      Matrix3f matrix3f,
      float p_253952_,
      float p_254066_,
      int p_254283_,
      int p_254566_,
      int p_253882_,
      float p_254434_,
      float p_254223_,
      int p_254372_
   ) {
      vertexConsumer.vertex(matrix4f, p_253952_, p_254066_, 0.0F)
         .color(p_254283_, p_254566_, p_253882_, 255)
         .uv(p_254434_, p_254223_)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(p_254372_)
         .normal(matrix3f, 1.0F, 1.0F, 1.0F)
         .endVertex();
   }

   public ResourceLocation getTextureLocation(ExpEntity p_114597_) {
      return St.id("textures/gui/exp/st_xp" + p_114597_.getStage() + ".png");
   }

   public RenderType getRenderType(ExpEntity p_114597_) {
      return RenderType.entityTranslucentEmissive(this.getTextureLocation(p_114597_));
   }
}

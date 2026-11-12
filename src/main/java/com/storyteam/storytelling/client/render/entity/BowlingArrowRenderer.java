package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.entities.NonPushAnimated;
import lombok.Generated;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

@OnlyIn(Dist.CLIENT)
public class BowlingArrowRenderer<T extends NonPushAnimated & IAnimatedEntity> extends CommonRenderer<T> {
   public static float bowlingBallRot;
   private static float arrowRotationOffset;
   private static boolean isArrowStopped = false;

   public BowlingArrowRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture) {
      super(manager, modelConfiguration, shadowSize, texture, false);
   }

   protected void setupRotations(@NotNull T pEntityLiving, @NotNull PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
      super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
      if (isArrowStopped) {
         pMatrixStack.mulPose(Axis.YP.rotationDegrees(arrowRotationOffset));
      } else {
         float rotationOffset = Mth.sin(pAgeInTicks * 0.3F) * 21.0F;
         pMatrixStack.mulPose(Axis.YP.rotationDegrees(rotationOffset));
         bowlingBallRot = rotationOffset;
      }
   }

   public static void stopArrowRendering() {
      isArrowStopped = true;
   }

   public static void resumeArrowRendering() {
      isArrowStopped = false;
   }

   @Generated
   public static void setArrowRotationOffset(float arrowRotationOffset) {
      BowlingArrowRenderer.arrowRotationOffset = arrowRotationOffset;
   }
}

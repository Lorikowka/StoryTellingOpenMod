package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.entities.NonPushAnimated;
import com.storyteam.storytelling.util.MathUtils;
import lombok.Generated;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

@OnlyIn(Dist.CLIENT)
public class BowlingArrowNewRenderer<T extends NonPushAnimated & IAnimatedEntity> extends CommonRenderer<T> {
   private static final int TOTAL_TICKS = 20;
   public static final int DEGREES = 20;
   private static float arrowRotationOffset = 270.0F;
   private static boolean moving = false;
   private static float tick = 0.0F;
   private static float angle = 20.0F;

   public BowlingArrowNewRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture) {
      super(manager, modelConfiguration, shadowSize, texture, false);
   }

   protected void setupRotations(@NotNull T pEntityLiving, @NotNull PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
      super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
      if (moving) {
         tick = pAgeInTicks;
      }

      float t = tick % 20.0F / 20.0F;
      Float progression = MathUtils.cubicBezier(0.7F, 0.0F, 0.3F, 1.0F).apply(t);
      if (tick / 20.0F % 2.0F >= 1.0F) {
         progression = 1.0F - progression;
      }

      float relativeAngle = progression * 2.0F * 20.0F - 20.0F;
      angle = relativeAngle / 2.0F;
      float offset = arrowRotationOffset + relativeAngle;
      pMatrixStack.mulPose(Axis.YP.rotationDegrees(offset));
   }

   @Generated
   public static void setArrowRotationOffset(float arrowRotationOffset) {
      BowlingArrowNewRenderer.arrowRotationOffset = arrowRotationOffset;
   }

   @Generated
   public static void setMoving(boolean moving) {
      BowlingArrowNewRenderer.moving = moving;
   }

   @Generated
   public static float getAngle() {
      return angle;
   }

   @Generated
   public static void setAngle(float angle) {
      BowlingArrowNewRenderer.angle = angle;
   }
}

package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonEmissiveRenderer<T extends AnimatedEntity> extends CommonRenderer<T> {
   public CommonEmissiveRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture) {
      super(manager, modelConfiguration, shadowSize, texture, false);
   }

   public void render(
      @NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight
   ) {
      matrixStack.pushPose();
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, 15728880);
      matrixStack.popPose();
   }

   @Nullable
   protected RenderType getRenderType(T pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
      return super.getRenderType(pLivingEntity, pBodyVisible, false, true);
   }

   public boolean shouldRender(T pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
      return true;
   }
}

package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

public class CommonScaledRenderer<T extends LivingEntity & IAnimatedEntity> extends CommonRenderer<T> {
   protected float scale;

   public CommonScaledRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale) {
      super(manager, modelConfiguration, shadowSize, texture, false);
      this.scale = scale;
   }

   @Override
   public void render(
      @NotNull T pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight
   ) {
      ((SimpleBedrockModel)this.model).entity = pEntity;
      ((SimpleBedrockModel)this.model).buffers = pBuffer;
      pMatrixStack.pushPose();
      pMatrixStack.scale(this.scale, this.scale, this.scale);
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
      pMatrixStack.popPose();
   }
}

package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

public class AlphaRenderer<T extends LivingEntity & IAnimatedEntity> extends CommonRenderer<T> implements IAlphaRenderer {
   float alpha = 1.0F;

   public AlphaRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float alpha) {
      super(manager, modelConfiguration, shadowSize, texture, false);
      this.alpha = alpha;
   }

   @Override
   public void renderAlpha(
      EntityModel instance, PoseStack matrixStack, VertexConsumer iVertexBuilder, int packedLightIn, int packetOverlayOut, float r, float g, float b, float a
   ) {
      instance.renderToBuffer(matrixStack, iVertexBuilder, packedLightIn, packetOverlayOut, 1.0F, 1.0F, 1.0F, this.alpha);
   }
}

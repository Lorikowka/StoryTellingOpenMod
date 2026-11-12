package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammerlib.util.mcf.Resources;

public class CutoutRenderer<T extends LivingEntity & IAnimatedEntity> extends CommonRenderer<T> {
   public CutoutRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture) {
      super(manager, modelConfiguration, shadowSize, texture);
   }

   @Override
   public void render(
      @NotNull T pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight
   ) {
      VertexConsumer consumer = pBuffer.getBuffer(RenderType.entityCutout(Resources.location("textures/entity/projectiles/arrow.png")));
      ((SimpleBedrockModel)this.model).renderToBuffer(pMatrixStack, consumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
   }
}

package com.storyteam.storytelling.system.currency.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.CommonRenderer;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.system.currency.content.entities.EidosGuiEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EidosGuiRenderer extends CommonRenderer<EidosGuiEntity> {
   public EidosGuiRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, boolean alwaysRenderer) {
      super(manager, new SimpleBedrockModel(modelConfiguration), shadowSize, texture);
   }

   public void render(EidosGuiEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
   }

   protected void renderNameTag(EidosGuiEntity entity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
   }
}

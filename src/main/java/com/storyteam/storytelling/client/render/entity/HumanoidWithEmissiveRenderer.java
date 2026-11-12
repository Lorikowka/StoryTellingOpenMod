package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.mcf.Resources;

public class HumanoidWithEmissiveRenderer extends HumanoidRenderer<HumanoidEntity> {
   protected int renderPass = 1;

   public HumanoidWithEmissiveRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale) {
      super(manager, modelConfiguration, shadowSize, texture, scale, false);
   }

   @Override
   public void render(
      @NotNull HumanoidEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight
   ) {
      this.renderPass = 1;
      float shadow = this.shadowStrength;
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
      this.shadowStrength = 0.0F;
      this.renderPass = 2;
      matrixStack.pushPose();
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
      matrixStack.popPose();
      this.shadowStrength = shadow;
      this.renderPass = 1;
   }

   @Nullable
   protected RenderType getRenderType(HumanoidEntity entity, boolean base, boolean culled, boolean outline) {
      ResourceLocation loc = this.getTextureLocation(entity);
      if (culled) {
         return RenderType.itemEntityTranslucentCull(loc);
      } else if (base) {
         return this.renderPass == 2 ? RenderType.entityTranslucentEmissive(loc) : ((SimpleBedrockModel)this.model).renderType(loc);
      } else {
         return outline ? RenderType.outline(loc) : null;
      }
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull HumanoidEntity entity) {
      ResourceLocation texture = super.getTextureLocation(entity);
      return this.renderPass == 2 ? Resources.location(texture.getNamespace(), texture.getPath().replace(".png", "_emissive.png")) : texture;
   }
}

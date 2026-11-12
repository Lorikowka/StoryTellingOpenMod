package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.layer.MainHandItemRenderLayer;
import com.storyteam.storytelling.client.render.entity.layer.OffHandItemRenderLayer;
import com.storyteam.storytelling.client.render.entity.processor.ViewDirectionProcessor;
import com.storyteam.storytelling.content.entities.AnimatedNoGazeHumanoid;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AnimatedNoGazeHumanoidRenderer<T extends AnimatedNoGazeHumanoid> extends CommonRenderer<T> {
   protected float scale;

   public AnimatedNoGazeHumanoidRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale) {
      super(manager, modelConfiguration, shadowSize, texture, false);
      this.addModelProcessor(new ViewDirectionProcessor("Head", this::doNotApplyHead));
      this.addLayer(new MainHandItemRenderLayer(this, "Right_placeholder"));
      this.addLayer(new OffHandItemRenderLayer(this, "Left_placeholder"));
      this.scale = scale;
   }

   protected float getScale(T entity) {
      return this.scale;
   }

   protected void setupRotations(@NotNull T entityLiving, @NotNull PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      if (entityLiving.rotateModel180) {
         ((SimpleBedrockModel)this.getModel()).getRoot().getRotation().add(0.0F, (float) Math.PI, 0.0F);
         ((SimpleBedrockModel)this.getModel()).getRoot().getTranslation().add(0.0F, 0.0F, 18.0F);
      }

      super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
   }

   public void render(
      @NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight
   ) {
      matrixStack.pushPose();
      float scale = this.getScale(entity);
      matrixStack.scale(scale, scale, scale);
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
      matrixStack.popPose();
   }

   private boolean doNotApplyHead() {
      return false;
   }
}

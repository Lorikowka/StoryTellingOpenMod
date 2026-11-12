package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.layer.MainHandItemRenderLayer;
import com.storyteam.storytelling.client.render.entity.layer.OffHandItemRenderLayer;
import com.storyteam.storytelling.client.render.entity.layer.VanityArmorLayer;
import com.storyteam.storytelling.client.render.entity.processor.EyeGazeProcessor;
import com.storyteam.storytelling.client.render.entity.processor.ViewDirectionProcessor;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EyeRenderer<T extends HumanoidEntity> extends CommonRenderer<T> {
   protected float scale;
   protected final VanityArmorLayer<T, SimpleBedrockModel<T>> vanityLayer;

   public EyeRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale, boolean alwaysRender) {
      super(manager, modelConfiguration, shadowSize, texture, alwaysRender);
      this.addModelProcessor(new ViewDirectionProcessor("Head", this::doNotApplyHead));
      this.addModelProcessor(new EyeGazeProcessor<>());
      this.addLayer(new MainHandItemRenderLayer(this, "Right_placeholder"));
      this.addLayer(new OffHandItemRenderLayer(this, "Left_placeholder"));
      this.addLayer(this.vanityLayer = new VanityArmorLayer<>(this));
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
      double worldX = entity.getX();
      double worldY = entity.getY();
      double worldZ = entity.getZ();
      matrixStack.translate(-worldX + 0.0, -worldY + 300.0, -worldZ + 0.0);
      matrixStack.mulPose(Axis.ZP.rotationDegrees(30.0F));
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, 15728880);
      matrixStack.popPose();
   }

   @Nullable
   protected RenderType getRenderType(T pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
      return RenderType.entityTranslucentCull(this.getTextureLocation(pLivingEntity));
   }

   private boolean doNotApplyHead() {
      return false;
   }

   public boolean shouldRender(T pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
      return true;
   }
}

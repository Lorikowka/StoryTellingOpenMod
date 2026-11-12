package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.def.RenderKey;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.configurations.IDynamicModelEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.util.mcf.Resources;

public class DynamicModelWithEmissiveRenderer<T extends AnimatedEntity & IDynamicModelEntity<T>> extends CommonRenderer<T> {
   private final RenderKey renderKey;
   protected int renderPass = 1;

   public DynamicModelWithEmissiveRenderer(
      Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, RenderKey renderKey
   ) {
      super(manager, modelConfiguration, shadowSize, texture, false);
      this.renderKey = renderKey;
   }

   public SimpleBedrockModel<T> onNewGeometry(IGeometryContainer geometry) {
      SimpleBedrockModel<T> m = new SimpleBedrockModel(new ModelConfiguration(geometry, this.renderKey::getNMC));
      m.model = m.modelContainer.createModel();
      return m;
   }

   public void render(
      @NotNull T pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight
   ) {
      this.renderPass = 1;
      float shadow = this.shadowStrength;
      this.model = pEntity.getModel(this::onNewGeometry);
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
      this.shadowStrength = 0.0F;
      this.renderPass = 2;
      pMatrixStack.pushPose();
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
      pMatrixStack.popPose();
      this.shadowStrength = shadow;
      this.renderPass = 1;
   }

   private ResourceLocation getEmissive(@NotNull T entity) {
      ResourceLocation rl = entity.getTexture();
      return Resources.location(rl.getNamespace(), rl.getPath().replace(".png", "_emissive.png"));
   }

   @Nullable
   protected RenderType getRenderType(T entity, boolean base, boolean culled, boolean outline) {
      ResourceLocation loc = this.getTextureLocation(entity);
      if (culled) {
         return RenderType.itemEntityTranslucentCull(loc);
      } else if (base) {
         return this.renderPass == 2 ? RenderType.entityTranslucentEmissive(this.getEmissive(entity)) : ((SimpleBedrockModel)this.model).renderType(loc);
      } else {
         return outline ? RenderType.outline(loc) : null;
      }
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull T entity) {
      return this.renderPass == 2 ? this.getEmissive(entity) : entity.getTexture();
   }
}

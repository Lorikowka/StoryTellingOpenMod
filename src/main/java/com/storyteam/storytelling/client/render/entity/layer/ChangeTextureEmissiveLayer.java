package com.storyteam.storytelling.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChangeTextureEmissiveLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   final Function<T, ResourceLocation> textureFunction = this::getDynamicTexture;
   final ResourceLocation[] TEXTURES;

   public ChangeTextureEmissiveLayer(RenderLayerParent<T, M> er, ResourceLocation[] texture_emissive) {
      super(er);
      this.TEXTURES = texture_emissive;
   }

   private ResourceLocation getDynamicTexture(T entity) {
      int tickCount = entity.tickCount;
      int textureIndex = tickCount / 2 % this.TEXTURES.length;
      return this.TEXTURES[textureIndex];
   }

   public void render(
      PoseStack matrixStackIn,
      MultiBufferSource bufferIn,
      int packedLightIn,
      T entitylivingbaseIn,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      ResourceLocation texture = this.textureFunction.apply(entitylivingbaseIn);
      VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucentEmissive(texture));
      this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
   }
}

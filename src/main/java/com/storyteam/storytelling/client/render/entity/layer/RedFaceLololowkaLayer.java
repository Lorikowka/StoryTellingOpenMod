package com.storyteam.storytelling.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
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
public class RedFaceLololowkaLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   final Function<T, ResourceLocation> texture;
   public static boolean shouldBeRed = false;
   public static boolean shouldFadeBack = false;
   private static float ticker = 0.0F;
   private static final float FADE_SPEED = 0.05F;
   private static final float TARGET_R = 1.0F;
   private static final float TARGET_G = 0.8F;
   private static final float TARGET_B = 0.8F;

   public RedFaceLololowkaLayer(RenderLayerParent<T, M> er, ResourceLocation texture) {
      this(er, (Function<T, ResourceLocation>)(ent -> texture));
   }

   public RedFaceLololowkaLayer(RenderLayerParent<T, M> er, Function<T, ResourceLocation> texture) {
      super(er);
      this.texture = texture;
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
      if (entitylivingbaseIn instanceof AnimatedEntity animatedEntity) {
         if (ticker > 0.0F) {
            VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(this.texture.apply(entitylivingbaseIn)));
            float r = 1.0F - 0.0F * ticker;
            float g = 1.0F - 0.19999999F * ticker;
            float b = 1.0F - 0.19999999F * ticker;
            this.getParentModel().renderToBuffer(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
         }

         this.updateEffect();
      }
   }

   private void updateEffect() {
      if (shouldBeRed && !shouldFadeBack) {
         ticker = 1.0F;
      }

      if (shouldFadeBack && ticker > 0.0F && shouldFadeBack && ticker > 0.0F) {
         ticker -= 0.001F;
         if (ticker < 0.0F) {
            ticker = 0.0F;
            shouldFadeBack = false;
         }
      }
   }

   public static void setRedEffect() {
      shouldBeRed = true;
      shouldFadeBack = false;
   }

   public static void startFadingBack() {
      shouldBeRed = false;
      shouldFadeBack = true;
   }

   public static void removeRedEffect() {
      shouldBeRed = false;
      shouldFadeBack = false;
      ticker = 0.0F;
   }
}

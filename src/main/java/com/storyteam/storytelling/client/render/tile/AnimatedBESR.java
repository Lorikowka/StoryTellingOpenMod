package com.storyteam.storytelling.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import java.util.function.Function;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.IAnimatedObject;
import org.zeith.hammeranims.api.tile.IAnimatedTile;

public abstract class AnimatedBESR<T extends BlockEntity & IAnimatedTile> implements BlockEntityRenderer<T> {
   protected SimpleBedrockModel<?> model;
   private Function<ResourceLocation, RenderType> renderTypeOverride;

   public AnimatedBESR(ModelConfiguration model) {
      this.model = new SimpleBedrockModel(model);
      this.renderTypeOverride = null;
   }

   public AnimatedBESR(ModelConfiguration model, Function<ResourceLocation, RenderType> renderTypeOverride) {
      this.model = new SimpleBedrockModel(model);
      this.renderTypeOverride = renderTypeOverride;
   }

   protected void processModel(T tile, float partialTicks) {
   }

   public void render(T tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
      ResourceLocation texture = this.getTexture(tile);
      RenderType renderType;
      if (this.renderTypeOverride != null) {
         renderType = this.renderTypeOverride.apply(texture);
      } else {
         renderType = this.model.renderType(texture);
      }

      this.renderWithType(tile, renderType, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
   }

   public void renderWithType(
      T tile, RenderType renderType, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn
   ) {
      AnimationSystem sys = ((IAnimatedObject)tile).getAnimationSystem();
      this.model.applyAnimations(sys, partialTicks);
      this.processModel(tile, partialTicks);
      ResourceLocation texture = this.getTexture(tile);
      matrixStackIn.pushPose();
      matrixStackIn.translate(0.5F, 0.0F, 0.5F);
      this.model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(renderType), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
      matrixStackIn.popPose();
   }

   protected abstract ResourceLocation getTexture(T var1);
}

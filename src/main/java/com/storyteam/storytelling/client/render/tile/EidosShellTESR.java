package com.storyteam.storytelling.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.blocks.EidosShellBlock;
import com.storyteam.storytelling.content.tiles.EidosShellTile;
import com.storyteam.storytelling.init.geo.BlockEntityModels;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EidosShellTESR extends AnimatedBESR<EidosShellTile> {
   private static final List<Function<EidosShellTile, ResourceLocation>> RENDER_LAYERS = List.of(
      tileEntityIn -> EidosShellBlock.getBaseTexture(EidosShellBlock.getType(tileEntityIn.getBlockState())),
      tileEntityIn -> EidosShellBlock.getEmissiveTexture()
   );
   private int renderPass = 0;

   public EidosShellTESR(Context context) {
      super(new ModelConfiguration(BlockEntityModels.EIDOS_SHELL), RenderType::entityTranslucentCull);
   }

   public void render(
      EidosShellTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn
   ) {
      BlockState state = tileEntityIn.getBlockState();
      Direction facing = (Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING);
      matrixStackIn.pushPose();
      matrixStackIn.translate(0.5, 0.0, 0.5);
      matrixStackIn.mulPose(new Quaternionf().fromAxisAngleDeg(new Vector3f(0.0F, 1.0F, 0.0F), 180.0F - facing.toYRot()));
      matrixStackIn.translate(-0.5, 0.0, -0.5);

      for (int i = 0; i < RENDER_LAYERS.size(); i++) {
         this.renderPass = i;
         ResourceLocation texture = this.getTexture(tileEntityIn);
         if (texture != null) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0F, 0.0F, (float)(-i) * 5.0E-4F);
            super.render(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
            matrixStackIn.popPose();
         }
      }

      matrixStackIn.popPose();
      this.renderPass = 0;
   }

   protected ResourceLocation getTexture(EidosShellTile tile) {
      return getLayerTexture(tile, this.renderPass);
   }

   @Nullable
   private static ResourceLocation getLayerTexture(EidosShellTile tile, int index) {
      return index >= 0 && index < RENDER_LAYERS.size() ? RENDER_LAYERS.get(index).apply(tile) : EidosShellBlock.COLORED_TEXTURE;
   }
}

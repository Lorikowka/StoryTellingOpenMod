package com.storyteam.storytelling.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.blocks.PampushkaMushroomBlock;
import com.storyteam.storytelling.content.tiles.PampushkaMushroomTile;
import com.storyteam.storytelling.init.geo.BlockEntityModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PampushkaMushroomTESR extends AnimatedBESR<PampushkaMushroomTile> {
   public PampushkaMushroomTESR(Context context) {
      super(new ModelConfiguration(BlockEntityModels.PAMPUSHKA_MUSHROOM), RenderType::entityTranslucentCull);
   }

   protected ResourceLocation getTexture(PampushkaMushroomTile tileEntityIn) {
      if (tileEntityIn.getBlockState().getBlock() instanceof PampushkaMushroomBlock block) {
         return block.getType() == PampushkaMushroomBlock.BlockType.GRAY ? PampushkaMushroomBlock.GRAY_TEXTURE : PampushkaMushroomBlock.COLORED_TEXTURE;
      } else {
         return PampushkaMushroomBlock.COLORED_TEXTURE;
      }
   }

   public void render(
      PampushkaMushroomTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn
   ) {
      Direction direction = (Direction)tileEntityIn.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
      matrixStackIn.translate(0.5, 0.0, 0.5);
      matrixStackIn.mulPose(new Quaternionf().fromAxisAngleDeg(new Vector3f(0.0F, 1.0F, 0.0F), 180.0F - direction.toYRot()));
      matrixStackIn.translate(-0.5, 0.0, -0.5);
      super.render(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
   }
}

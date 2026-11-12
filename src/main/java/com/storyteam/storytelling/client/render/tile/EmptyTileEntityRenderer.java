package com.storyteam.storytelling.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class EmptyTileEntityRenderer implements BlockEntityRenderer<BlockEntity> {
   public void render(
      @NotNull BlockEntity pBlockEntity,
      float pPartialTick,
      @NotNull PoseStack pPoseStack,
      @NotNull MultiBufferSource pBufferSource,
      int pPackedLight,
      int pPackedOverlay
   ) {
   }
}

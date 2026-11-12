package com.storyteam.storytelling.content.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class RotateBlockTile extends TileLinkable {
   public static boolean particle = false;
   public static boolean down = false;

   public RotateBlockTile(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
      super(pType, pPos, pBlockState);
   }

   public AABB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }
}

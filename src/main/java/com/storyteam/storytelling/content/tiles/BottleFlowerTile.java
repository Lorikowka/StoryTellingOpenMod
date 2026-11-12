package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.init.TilesST;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BottleFlowerTile extends BaseAnimatedTile<BottleFlowerTile> {
   public BottleFlowerTile(BlockPos pos, BlockState state) {
      super(TilesST.BOTTLEFLOWER, pos, state);
   }

   @Override
   public void update() {
      super.update();
   }

   public AABB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }
}

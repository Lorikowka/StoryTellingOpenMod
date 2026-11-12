package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.content.blocks.base.ColoredLightBlock;
import com.storyteam.storytelling.system.shaders.lighting.LightSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

public class ColoredLightBlockTile extends TileSyncableTickable {
   int lastLevel = -1;

   public ColoredLightBlockTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
   }

   public void clientTick() {
      BlockPos pos = this.getBlockPos();
      if (this.lastLevel != (this.lastLevel = (Integer)this.getBlockState().getValue(ColoredLightBlock.LEVEL))) {
         LightSystem.setLight(pos, 0, null, level -> false);
      }

      LightSystem.setLight(
         pos, this.lastLevel, ((ColoredLightBlock)this.getBlockState().getBlock()).getLightColor(), lvl -> lvl != null && lvl.getBlockEntity(pos) == this
      );
   }
}

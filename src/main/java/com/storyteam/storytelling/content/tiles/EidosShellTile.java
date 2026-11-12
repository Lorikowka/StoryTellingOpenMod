package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.init.TilesST;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.zeith.hammeranims.api.animsys.AnimationSystem;

public class EidosShellTile extends BaseAnimatedTile<EidosShellTile> {
   public final AnimationSystem animations = AnimationSystem.create(this);

   public EidosShellTile(BlockPos pos, BlockState state) {
      super(TilesST.EIDOS_SHELL, pos, state);
   }

   public AABB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }
}

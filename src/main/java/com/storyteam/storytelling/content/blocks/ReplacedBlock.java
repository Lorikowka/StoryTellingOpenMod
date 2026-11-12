package com.storyteam.storytelling.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.FluidState;

public class ReplacedBlock extends Block {
   Block remains;

   public ReplacedBlock(Block remains, Properties pProperties) {
      super(pProperties);
      this.remains = remains;
   }

   public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
      this.playerWillDestroy(level, pos, state, player);
      return level.setBlock(pos, this.remains.defaultBlockState(), level.isClientSide ? 11 : 3);
   }
}

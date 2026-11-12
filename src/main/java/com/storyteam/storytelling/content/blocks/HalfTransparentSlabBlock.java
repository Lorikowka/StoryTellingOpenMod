package com.storyteam.storytelling.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.SlabType;

public class HalfTransparentSlabBlock extends SlabBlock {
   public HalfTransparentSlabBlock(Properties pProperties) {
      super(pProperties);
   }

   public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
      return pAdjacentBlockState.is(this) && pState.getValue(TYPE) != SlabType.DOUBLE || super.skipRendering(pState, pAdjacentBlockState, pSide);
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return true;
   }
}

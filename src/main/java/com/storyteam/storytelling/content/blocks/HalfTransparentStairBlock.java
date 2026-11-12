package com.storyteam.storytelling.content.blocks;

import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class HalfTransparentStairBlock extends StairBlock {
   public HalfTransparentStairBlock(Supplier<BlockState> state, Properties properties) {
      super(state, properties);
   }

   public HalfTransparentStairBlock(BlockState pBaseState, Properties pProperties) {
      super(pBaseState, pProperties);
   }

   public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
      return pAdjacentBlockState.is(this) || super.skipRendering(pState, pAdjacentBlockState, pSide);
   }
}

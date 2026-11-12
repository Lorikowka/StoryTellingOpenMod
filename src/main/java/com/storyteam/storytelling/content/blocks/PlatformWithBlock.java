package com.storyteam.storytelling.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlatformWithBlock extends Block {
   public PlatformWithBlock() {
      super(Properties.copy(Blocks.STONE).noOcclusion().emissiveRendering((state, level, pos) -> true));
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return Block.box(2.8, 0.0, 2.8, 13.2, 9.1, 13.2);
   }
}

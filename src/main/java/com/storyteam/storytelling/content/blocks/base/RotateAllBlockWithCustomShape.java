package com.storyteam.storytelling.content.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RotateAllBlockWithCustomShape extends DirectionalBlock {
   VoxelShape shape;

   public RotateAllBlockWithCustomShape(Properties pProperties, VoxelShape shape) {
      super(pProperties);
      this.shape = shape;
   }

   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return this.shape;
   }

   protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder_) {
      super.createBlockStateDefinition(builder_);
      builder_.add(new Property[]{FACING});
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
   }
}

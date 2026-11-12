package com.storyteam.storytelling.content.blocks.base;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RotateBlock extends HorizontalDirectionalBlock {
   public RotateBlock(Properties pProperties) {
      super(pProperties);
   }

   protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder_) {
      super.createBlockStateDefinition(builder_);
      builder_.add(new Property[]{FACING});
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   @NotNull
   public BlockState getStateForPlacement(Entity context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getDirection().getOpposite());
   }
}

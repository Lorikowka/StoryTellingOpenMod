package com.storyteam.storytelling.content.blocks.base;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.mcf.shapes.ShapeUtils;

public class RotateBlockWithCustomShape extends HorizontalDirectionalBlock {
   protected List<VoxelShape> staticShapes;
   VoxelShape shape;

   public RotateBlockWithCustomShape(Properties pProperties, VoxelShape shape) {
      super(pProperties);
      this.shape = shape;
   }

   protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder_) {
      super.createBlockStateDefinition(builder_);
      builder_.add(new Property[]{FACING});
   }

   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      if (this.staticShapes == null) {
         this.staticShapes = List.of(
            ShapeUtils.rotateNorthShape(this.shape, Direction.SOUTH),
            ShapeUtils.rotateNorthShape(this.shape, Direction.WEST),
            ShapeUtils.rotateNorthShape(this.shape, Direction.NORTH),
            ShapeUtils.rotateNorthShape(this.shape, Direction.EAST)
         );
      }

      return this.staticShapes.get(((Direction)pState.getValue(BlockStateProperties.HORIZONTAL_FACING)).get2DDataValue());
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

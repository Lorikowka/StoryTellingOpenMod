package com.storyteam.storytelling.content.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockFlatFace extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock {
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   protected static final VoxelShape CEILING_AABB_X = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
   protected static final VoxelShape CEILING_AABB_Z = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
   protected static final VoxelShape FLOOR_AABB_X = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
   protected static final VoxelShape FLOOR_AABB_Z = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
   protected static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
   protected static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
   protected static final VoxelShape WEST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
   protected static final VoxelShape EAST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);

   public BlockFlatFace(Properties properties) {
      super(properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACE, AttachFace.FLOOR)).setValue(WATERLOGGED, false)
      );
   }

   public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
      return true;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
      FluidState fluidstate = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
      boolean flag = fluidstate.getType() == Fluids.WATER;

      for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
         BlockState blockstate;
         if (direction.getAxis() == Axis.Y) {
            blockstate = (BlockState)((BlockState)((BlockState)this.defaultBlockState()
                     .setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR))
                  .setValue(FACING, blockPlaceContext.getHorizontalDirection()))
               .setValue(WATERLOGGED, flag);
         } else {
            blockstate = (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACE, AttachFace.WALL))
                  .setValue(FACING, direction.getOpposite()))
               .setValue(WATERLOGGED, flag);
         }

         if (blockstate.canSurvive(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos())) {
            return blockstate;
         }
      }

      return null;
   }

   @NotNull
   public VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
      Direction direction = (Direction)blockState.getValue(FACING);
      switch ((AttachFace)blockState.getValue(FACE)) {
         case FLOOR:
            if (direction.getAxis() == Axis.X) {
               return FLOOR_AABB_X;
            }

            return FLOOR_AABB_Z;
         case WALL:
            return switch (direction) {
               case EAST -> EAST_AABB;
               case WEST -> WEST_AABB;
               case SOUTH -> SOUTH_AABB;
               default -> NORTH_AABB;
            };
         case CEILING:
         default:
            return direction.getAxis() == Axis.X ? CEILING_AABB_X : CEILING_AABB_Z;
      }
   }

   public FluidState getFluidState(BlockState blockState) {
      return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> blockStateBuilder) {
      blockStateBuilder.add(new Property[]{FACING, FACE, WATERLOGGED});
   }
}

package com.storyteam.storytelling.content.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockFlatFaceLight extends BlockFlatFace {
   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

   public BlockFlatFaceLight(Properties properties) {
      super(properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACE, AttachFace.FLOOR)).setValue(LIT, Boolean.FALSE))
            .setValue(WATERLOGGED, false)
      );
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
      FluidState fluidstate = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
      boolean flag = fluidstate.getType() == Fluids.WATER;

      for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
         BlockState blockstate;
         if (direction.getAxis() == Axis.Y) {
            blockstate = (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState()
                        .setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR))
                     .setValue(FACING, blockPlaceContext.getHorizontalDirection()))
                  .setValue(LIT, blockPlaceContext.getLevel().hasNeighborSignal(blockPlaceContext.getClickedPos())))
               .setValue(WATERLOGGED, flag);
         } else {
            blockstate = (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACE, AttachFace.WALL))
                     .setValue(FACING, direction.getOpposite()))
                  .setValue(LIT, blockPlaceContext.getLevel().hasNeighborSignal(blockPlaceContext.getClickedPos())))
               .setValue(WATERLOGGED, flag);
         }

         if (blockstate.canSurvive(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos())) {
            return blockstate;
         }
      }

      return null;
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!level.isClientSide && player.getAbilities().mayBuild) {
         boolean isLit = (Boolean)state.getValue(LIT);
         level.setBlock(pos, (BlockState)state.setValue(LIT, !isLit), 2);
         if (!isLit) {
            level.setBlock(pos, (BlockState)state.setValue(LIT, true), 2);
         }
      }

      return InteractionResult.sidedSuccess(level.isClientSide);
   }

   @NotNull
   @Override
   public VoxelShape getShape(BlockState p_51104_, @NotNull BlockGetter p_51105_, @NotNull BlockPos p_51106_, @NotNull CollisionContext p_51107_) {
      Direction direction = (Direction)p_51104_.getValue(FACING);
      switch ((AttachFace)p_51104_.getValue(FACE)) {
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

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> blockStateBuilder) {
      blockStateBuilder.add(new Property[]{FACING, FACE, LIT, WATERLOGGED});
   }
}

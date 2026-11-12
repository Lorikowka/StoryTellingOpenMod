package com.storyteam.storytelling.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class JapanLanternBlock extends Block {
   public static final BooleanProperty LIT = BlockStateProperties.LIT;
   public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

   public JapanLanternBlock() {
      super(Properties.copy(Blocks.GLASS).noOcclusion().lightLevel(state -> state.getValue(LIT) ? 15 : 0));
      this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(LIT, false)).setValue(LOCKED, true));
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return Block.box(3.85, 0.0, 3.85, 12.15, 13.4, 12.15);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{LIT});
      builder.add(new Property[]{LOCKED});
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else if (!player.isCreative() && (Boolean)state.getValue(LOCKED)) {
         return InteractionResult.FAIL;
      } else {
         level.setBlock(pos, (BlockState)state.cycle(LIT), 3);
         return InteractionResult.CONSUME;
      }
   }

   public static void setLocked(Level level, BlockPos pos, boolean locked) {
      BlockState state = level.getBlockState(pos);
      if (state.getBlock() instanceof JapanLanternBlock) {
         level.setBlock(pos, (BlockState)state.setValue(LOCKED, locked), 3);
      }
   }

   public static void setLit(Level level, BlockPos pos, boolean lit) {
      BlockState state = level.getBlockState(pos);
      if (state.getBlock() instanceof JapanLanternBlock) {
         level.setBlock(pos, (BlockState)state.setValue(LIT, lit), 3);
      }
   }
}

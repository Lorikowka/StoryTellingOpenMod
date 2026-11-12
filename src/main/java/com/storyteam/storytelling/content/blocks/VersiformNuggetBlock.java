package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.content.blocks.base.RotateBlockWithCustomShape;
import com.storyteam.storytelling.init.ItemsST;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VersiformNuggetBlock extends RotateBlockWithCustomShape {
   public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

   public VersiformNuggetBlock(Properties pProperties) {
      super(pProperties, makeShape());
      this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LOCKED, false));
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{LOCKED});
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else if ((Boolean)state.getValue(LOCKED)) {
         return InteractionResult.FAIL;
      } else {
         ItemStack drop = new ItemStack(ItemsST.VERSIFORM_RESOURCE, 3);
         if (!player.addItem(drop)) {
            player.drop(drop, false);
         }

         level.removeBlock(pos, false);
         return InteractionResult.CONSUME;
      }
   }

   private static VoxelShape makeShape() {
      return Stream.of(Block.box(6.25, 0.0, 7.25, 10.25, 4.0, 11.25), Block.box(7.25, 0.0, 6.25, 11.25, 3.0, 9.25), Block.box(5.25, 0.0, 5.25, 8.25, 2.0, 8.25))
         .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR))
         .get();
   }

   public static void setLocked(Level level, BlockPos pos, boolean locked) {
      BlockState state = level.getBlockState(pos);
      if (state.getBlock() instanceof VersiformNuggetBlock) {
         level.setBlock(pos, (BlockState)state.setValue(LOCKED, locked), 3);
      }
   }
}

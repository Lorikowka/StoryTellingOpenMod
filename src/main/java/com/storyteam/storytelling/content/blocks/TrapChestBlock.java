package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.content.tiles.TrapChestTile;
import com.storyteam.storytelling.system.emotions.BubbleType;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.emotions.network.S2CAddCharacterEmotion;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.init.FragmentsST;
import com.storyteam.storytelling.util.ServerScheduler;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.net.Network;

public class TrapChestBlock extends ChestBlock {
   public static final BooleanProperty OPENED = BooleanProperty.create("opened");
   public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");

   public TrapChestBlock(Properties pProperties, Supplier<BlockEntityType<? extends ChestBlockEntity>> pBlockEntityType) {
      super(pProperties, pBlockEntityType);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(OPENED, true))
                     .setValue(TRIGGERED, false))
                  .setValue(FACING, Direction.NORTH))
               .setValue(TYPE, ChestType.SINGLE))
            .setValue(WATERLOGGED, false)
      );
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{OPENED, TRIGGERED});
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else if (player.isCreative() && player.isShiftKeyDown()) {
         BlockState newState = (BlockState)((BlockState)state.setValue(OPENED, !(Boolean)state.getValue(OPENED)))
            .setValue(TRIGGERED, !(Boolean)state.getValue(TRIGGERED));
         level.setBlock(pos, newState, 3);
         return InteractionResult.SUCCESS;
      } else if (!(Boolean)state.getValue(TRIGGERED)) {
         BlockState newState = (BlockState)((BlockState)state.setValue(TRIGGERED, true)).setValue(OPENED, false);
         level.setBlock(pos, newState, 3);
         player.hurt(level.damageSources().generic(), 2.0F);
         ServerScheduler.schedule(30, () -> Network.sendToAll(new S2CAddCharacterEmotion("player", Emotion.WEARY_FACE, Vec3.ZERO, BubbleType.SPEECH)));
         ServerScheduler.schedule(60, () -> {
            if (!FragmentInstanceManager.isActive(FragmentsST.PAINFULLY)) {
               FragmentInstanceManager.addActive(FragmentsST.PAINFULLY);
            }
         });
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.FAIL;
      }
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return level.isClientSide ? (lvl, pos, st, te) -> {
         if (te instanceof TrapChestTile trap) {
            trap.tick(level, state, pos);
         }
      } : null;
   }

   @NotNull
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TrapChestTile(pos, state);
   }
}

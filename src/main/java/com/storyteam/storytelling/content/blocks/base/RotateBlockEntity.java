package com.storyteam.storytelling.content.blocks.base;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

public class RotateBlockEntity<T extends TileSyncableTickable> extends HorizontalDirectionalBlock implements EntityBlock {
   protected final Supplier<BlockEntityType<T>> typeSupplier;
   protected boolean doesTick;
   protected boolean oppositeFaceForPlacement;

   public RotateBlockEntity(Properties pProperties, boolean doesTick, Supplier<BlockEntityType<T>> typeSupplier) {
      super(pProperties);
      this.doesTick = doesTick;
      this.typeSupplier = typeSupplier;
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
      return this.typeSupplier.get().create(pPos, pState);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
      super.createBlockStateDefinition(pBuilder);
      pBuilder.add(new Property[]{FACING});
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   @Nullable
   public <E extends BlockEntity> BlockEntityTicker<E> getTicker(
      @NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<E> pBlockEntityType
   ) {
      return BlockAPI.ticker(pLevel);
   }
}

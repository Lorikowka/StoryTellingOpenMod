package com.storyteam.storytelling.system._core.structio.entity.client;

import java.util.Map;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class BlockGetterByMap implements BlockGetter {
   private final BlockState air = Blocks.AIR.defaultBlockState();
   private final Map<BlockPos, BlockState> stateMap;

   @Nullable
   public BlockEntity getBlockEntity(BlockPos pPos) {
      return null;
   }

   public BlockState getBlockState(BlockPos p_45571_) {
      return this.stateMap.getOrDefault(p_45571_, this.air);
   }

   public FluidState getFluidState(BlockPos pPos) {
      return Fluids.EMPTY.defaultFluidState();
   }

   public int getHeight() {
      return 0;
   }

   public int getMinBuildHeight() {
      return 0;
   }

   @Generated
   public BlockGetterByMap(Map<BlockPos, BlockState> stateMap) {
      this.stateMap = stateMap;
   }
}

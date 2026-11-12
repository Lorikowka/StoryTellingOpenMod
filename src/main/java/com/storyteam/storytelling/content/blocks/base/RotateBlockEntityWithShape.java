package com.storyteam.storytelling.content.blocks.base;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

public class RotateBlockEntityWithShape<T extends TileSyncableTickable> extends RotateBlockEntity<T> {
   protected VoxelShape shape;

   public RotateBlockEntityWithShape(VoxelShape shape, Properties pProperties, boolean doesTick, Supplier<BlockEntityType<T>> supplier) {
      super(pProperties, doesTick, supplier);
      this.shape = shape;
   }

   @NotNull
   public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
      return this.shape;
   }
}

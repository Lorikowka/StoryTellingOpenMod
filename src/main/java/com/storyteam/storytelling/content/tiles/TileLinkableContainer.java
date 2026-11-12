package com.storyteam.storytelling.content.tiles;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public abstract class TileLinkableContainer extends TileLinkable {
   private final LazyOptional<IItemHandler> itemHandlerOptional = LazyOptional.of(this::getInventory);

   public TileLinkableContainer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
   }

   public abstract IItemHandler getInventory();

   @NotNull
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
      return capability == ForgeCapabilities.ITEM_HANDLER ? this.itemHandlerOptional.cast() : super.getCapability(capability, direction);
   }
}

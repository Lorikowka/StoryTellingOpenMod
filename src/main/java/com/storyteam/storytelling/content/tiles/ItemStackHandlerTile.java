package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.util.ScriptUtils;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ItemStackHandlerTile extends BlockEntity {
   private final ItemStackHandler itemHandler = new ItemStackHandler(1);
   private final LazyOptional<IItemHandler> optionalHandler = LazyOptional.of(() -> this.itemHandler);

   public ItemStackHandlerTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
   }

   public ItemStackHandler getInventory() {
      return this.itemHandler;
   }

   public void setStackInSlot(int i, ItemStack stack) {
      ItemStackHandler handler = this.getInventory();
      if (handler != null) {
         this.getInventory().setStackInSlot(i, stack);
         if (!this.level.isClientSide) {
            ScriptUtils.updateTile(this);
         }
      }
   }

   public ItemStack getStackInSlot(int i) {
      ItemStackHandler handler = this.getInventory();
      return handler != null && handler.getSlots() != 0 ? handler.getStackInSlot(i) : ItemStack.EMPTY;
   }

   public int getSize() {
      ItemStackHandler handler = this.getInventory();
      return handler == null ? 0 : handler.getSlots();
   }

   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return cap == ForgeCapabilities.ITEM_HANDLER ? this.optionalHandler.cast() : super.getCapability(cap, side);
   }

   public ClientboundBlockEntityDataPacket getUpdatePacket() {
      CompoundTag tag = this.saveWithFullMetadata();
      return ClientboundBlockEntityDataPacket.create(this, tile -> tag);
   }

   public CompoundTag getUpdateTag() {
      CompoundTag tag = super.getUpdateTag();
      tag.put("inventory", this.itemHandler.serializeNBT());
      return tag;
   }

   public void handleUpdateTag(CompoundTag tag) {
      super.handleUpdateTag(tag);
      if (tag.contains("inventory")) {
         this.itemHandler.deserializeNBT(tag.getCompound("inventory"));
      }
   }

   public void load(CompoundTag tag) {
      super.load(tag);
      if (tag.contains("inventory")) {
         this.itemHandler.deserializeNBT(tag.getCompound("inventory"));
      }
   }

   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.put("inventory", this.itemHandler.serializeNBT());
   }
}

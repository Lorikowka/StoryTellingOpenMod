package com.storyteam.storytelling.events.base;

import lombok.Generated;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ContainerSlotClickEvent extends PlayerContainerEvent {
   private final Slot slot;
   private final ClickType type;
   private final ItemStack heldStack;
   private final ItemStack slotStack;
   private final ClickAction action;

   public ContainerSlotClickEvent(
      Player player, AbstractContainerMenu container, Slot slot, ClickAction action, ClickType type, ItemStack heldStack, ItemStack slotStack
   ) {
      super(player, container);
      this.slot = slot;
      this.type = type;
      this.action = action;
      this.heldStack = heldStack;
      this.slotStack = slotStack;
   }

   @Generated
   public Slot getSlot() {
      return this.slot;
   }

   @Generated
   public ClickType getType() {
      return this.type;
   }

   @Generated
   public ItemStack getHeldStack() {
      return this.heldStack;
   }

   @Generated
   public ItemStack getSlotStack() {
      return this.slotStack;
   }

   @Generated
   public ClickAction getAction() {
      return this.action;
   }
}

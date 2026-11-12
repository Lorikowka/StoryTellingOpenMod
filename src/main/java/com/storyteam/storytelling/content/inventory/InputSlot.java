package com.storyteam.storytelling.content.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class InputSlot extends Slot {
   public InputSlot(Container pContainer, int pSlot, int pX, int pY) {
      super(pContainer, pSlot, pX, pY);
   }

   public InputSlot(Container pContainer, int pSlot) {
      this(pContainer, pSlot, 0, 0);
   }

   public boolean mayPlace(ItemStack pStack) {
      return this.container.canPlaceItem(this.index, pStack);
   }
}

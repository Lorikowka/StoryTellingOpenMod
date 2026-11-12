package com.storyteam.storytelling.content.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class FilteredSlot extends Slot {
   protected Ingredient filter;

   public FilteredSlot(Ingredient filter, Container pContainer, int pSlot, int pX, int pY) {
      super(pContainer, pSlot, pX, pY);
      this.filter = filter;
   }

   public FilteredSlot(Ingredient filter, Container pContainer, int pSlot) {
      this(filter, pContainer, pSlot, 0, 0);
   }

   public boolean mayPlace(ItemStack pStack) {
      return this.filter.test(pStack);
   }
}

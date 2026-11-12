package com.storyteam.storytelling.api.itf;

import java.util.function.Consumer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.zeith.hammerlib.util.java.Cast;

public interface IHiddenFromJeiItem {
   IHiddenFromJeiItem EMPTY = new IHiddenFromJeiItem() {
      @Override
      public void getHiddenItems(Consumer<ItemStack> items) {
      }
   };

   default void getHiddenItems(Consumer<ItemStack> items) {
      items.accept(((ItemLike)Cast.cast(this, ItemLike.class)).asItem().getDefaultInstance());
   }

   static IHiddenFromJeiItem get(Item item) {
      if (item instanceof BlockItem bi) {
         Block var3 = bi.getBlock();
         if (var3 instanceof IHiddenFromJeiItem) {
            return (IHiddenFromJeiItem)var3;
         }
      }

      return item instanceof IHiddenFromJeiItem j ? j : EMPTY;
   }
}

package com.storyteam.storytelling.util.data_structure;

import com.storyteam.storytelling.util.UtilST;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemAmount {
   Item item;
   int count;

   public Component getName() {
      return this.item.getDefaultInstance().getDisplayName();
   }

   public static void giveItems(ServerPlayer player, ItemAmount itemAmount) {
      int count = itemAmount.getCount();

      for (int i = 0; i < itemAmount.getCount() / 64; i++) {
         UtilST.giveItemStack(player, new ItemStack(itemAmount.item, 64));
         count -= 64;
      }

      UtilST.giveItemStack(player, new ItemStack(itemAmount.item, count));
   }

   @Generated
   public ItemAmount(Item item, int count) {
      this.item = item;
      this.count = count;
   }

   @Generated
   public Item getItem() {
      return this.item;
   }

   @Generated
   public int getCount() {
      return this.count;
   }

   @Generated
   public void setItem(Item item) {
      this.item = item;
   }

   @Generated
   public void setCount(int count) {
      this.count = count;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ItemAmount other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.getCount() != other.getCount()) {
         return false;
      } else {
         Object this$item = this.getItem();
         Object other$item = other.getItem();
         return this$item == null ? other$item == null : this$item.equals(other$item);
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof ItemAmount;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + this.getCount();
      Object $item = this.getItem();
      return result * 59 + ($item == null ? 43 : $item.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "ItemAmount(item=" + this.getItem() + ", count=" + this.getCount() + ")";
   }
}

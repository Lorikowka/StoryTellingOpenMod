package com.storyteam.storytelling.system.shop.utils;

import com.storyteam.storytelling.system.shop.data.LotInfo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ShopUtils {
   public static boolean hasAll(LotInfo lot, int count, Player player) {
      int leftover = count * lot.getCount();
      int amount = 0;

      for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
         ItemStack stack = player.getInventory().getItem(i);
         if (!stack.isEmpty() && stack.getItem().equals(lot.getStack().getItem()) && lot.getStackValidator().test(stack)) {
            amount += stack.getCount();
         }
      }

      return amount >= leftover;
   }

   public static boolean takeAll(LotInfo lot, int count, Player player) {
      int leftover = count * lot.getCount();
      if (!hasAll(lot, count, player)) {
         return false;
      } else {
         for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem().equals(lot.getStack().getItem()) && lot.getStackValidator().test(stack)) {
               if (stack.getCount() >= leftover) {
                  stack.shrink(leftover);
                  leftover = 0;
               } else {
                  leftover -= stack.getCount();
                  player.getInventory().setItem(i, ItemStack.EMPTY);
               }

               if (leftover <= 0) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}

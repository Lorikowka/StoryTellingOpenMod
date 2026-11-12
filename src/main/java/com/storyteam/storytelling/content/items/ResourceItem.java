package com.storyteam.storytelling.content.items;

import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import com.storyteam.storytelling.system.rts.common.ResourceST;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class ResourceItem extends Item {
   private final ResourceST resauce;

   public ResourceItem(ResourceST resource, Properties pProperties) {
      super(pProperties);
      this.resauce = resource;
   }

   public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
      super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
      if (pEntity instanceof Player player) {
         if (!pLevel.isClientSide()) {
            ResourceCostST.create().add(this.resauce, pStack.getCount()).deposit(player.getName().getString());
         }

         pStack.setCount(0);
      }
   }
}

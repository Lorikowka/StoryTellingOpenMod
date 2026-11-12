package com.storyteam.storytelling.system.clothes.client;

import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class VanityArmRenderingHelper {
   public static ItemStack HEAD_PIECE = ItemStack.EMPTY;
   public static ItemStack CHEST_PIECE = ItemStack.EMPTY;
   public static ItemStack LEGS_PIECE = ItemStack.EMPTY;
   public static ItemStack FEET_PIECE = ItemStack.EMPTY;

   @SubscribeEvent
   public static void renderArm(RenderArmEvent e) {
      AbstractClientPlayer pl = e.getPlayer();
      HEAD_PIECE = pl.getItemBySlot(EquipmentSlot.HEAD);
      CHEST_PIECE = pl.getItemBySlot(EquipmentSlot.CHEST);
      LEGS_PIECE = pl.getItemBySlot(EquipmentSlot.LEGS);
      FEET_PIECE = pl.getItemBySlot(EquipmentSlot.FEET);
   }

   public static boolean is(VanitySlot slot, VanitySet set) {
      return set.has(slot) && get(slot).is(set.get(slot));
   }

   public static ItemStack get(VanitySlot slot) {
      return switch (slot) {
         case HEAD -> HEAD_PIECE;
         case BODY -> CHEST_PIECE;
         case LEGS -> LEGS_PIECE;
         case FEET -> FEET_PIECE;
         default -> ItemStack.EMPTY;
      };
   }
}

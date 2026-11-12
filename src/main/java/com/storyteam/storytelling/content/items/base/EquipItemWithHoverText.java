package com.storyteam.storytelling.content.items.base;

import com.storyteam.storytelling.system.sounds.SoundUtils;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class EquipItemWithHoverText extends Item {
   public static final UnaryOperator<Style> DARK_GRAY_ITALIC = s -> s.withItalic(true).withColor(ChatFormatting.DARK_GRAY);
   private final Component description;
   private final LinkedHashSet<EquipmentSlot> allowedSlots;

   public EquipItemWithHoverText(Properties properties, Component description, EquipmentSlot... slots) {
      super(properties.stacksTo(1));
      this.description = (Component)(description != null ? description : Component.empty());
      this.allowedSlots = new LinkedHashSet<>(Arrays.asList(slots));
   }

   public EquipItemWithHoverText(Properties properties, String description, EquipmentSlot... slots) {
      super(properties.stacksTo(1));
      this.description = description != null && !description.isEmpty() ? Component.literal(description).withStyle(DARK_GRAY_ITALIC) : Component.empty();
      this.allowedSlots = new LinkedHashSet<>(Arrays.asList(slots));
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
      ItemStack heldItem = player.getItemInHand(usedHand);

      for (EquipmentSlot slot : this.allowedSlots) {
         ItemStack currentEquipped = player.getItemBySlot(slot);
         if (!level.isClientSide) {
            ItemStack toEquip = heldItem.copy();
            toEquip.setCount(1);
            if (currentEquipped.isEmpty()) {
               player.setItemSlot(slot, toEquip);
               heldItem.shrink(1);
            } else {
               player.setItemSlot(slot, toEquip);
               player.setItemInHand(usedHand, currentEquipped);
            }

            SoundUtils.playVolumedSound(player, SoundEvents.ARMOR_EQUIP_LEATHER);
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide);
         }
      }

      return InteractionResultHolder.pass(heldItem);
   }

   public boolean canEquip(ItemStack stack, EquipmentSlot slot, Entity entity) {
      return this.allowedSlots.contains(slot);
   }

   public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
      if (this.description != null && !this.description.equals(Component.empty())) {
         tooltipComponents.add(this.description);
      }
   }
}

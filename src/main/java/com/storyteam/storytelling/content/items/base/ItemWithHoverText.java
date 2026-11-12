package com.storyteam.storytelling.content.items.base;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ItemWithHoverText extends Item {
   public static final UnaryOperator<Style> DARK_GRAY_ITALIC = s -> s.withItalic(true).withColor(ChatFormatting.DARK_GRAY);
   private final Component[] description;

   public ItemWithHoverText(Properties pProperties, Component... description) {
      super(pProperties);
      this.description = description;
   }

   public ItemWithHoverText(Properties pProperties, String... description) {
      super(pProperties);
      this.description = new Component[description.length];

      for (int i = 0; i < description.length; i++) {
         this.description[i] = Component.literal(description[i]).withStyle(DARK_GRAY_ITALIC);
      }
   }

   public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pTooltipComponents.addAll(Arrays.asList(this.description));
   }
}

package com.storyteam.storytelling.system.clothes.item;

import com.storyteam.storytelling.system.clothes.ArmorGeometryConfiguration;
import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ArmorWithHoverText extends ItemVanityArmor {
   public static final UnaryOperator<Style> DARK_GRAY_ITALIC = s -> s.withItalic(true).withColor(ChatFormatting.DARK_GRAY);
   private final Component[] description;

   public ArmorWithHoverText(
      VanitySet set, ArmorMaterial material, VanitySlot slot, Properties properties, String texture, ArmorGeometryConfiguration model, Component... description
   ) {
      super(set, material, slot, properties, texture, model);
      this.description = description;
   }

   public ArmorWithHoverText(
      VanitySet set, ArmorMaterial material, VanitySlot slot, Properties properties, String texture, ArmorGeometryConfiguration model, String... description
   ) {
      super(set, material, slot, properties, texture, model);
      this.description = new Component[description.length];

      for (int i = 0; i < description.length; i++) {
         this.description[i] = Component.literal(description[i]).withStyle(DARK_GRAY_ITALIC);
      }
   }

   public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
      tooltipComponents.addAll(Arrays.asList(this.description));
   }
}

package com.storyteam.storytelling.content.blocks.base;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.Nullable;

public class RotateBlockWithHoverText extends RotateBlock {
   public static final UnaryOperator<Style> DARK_GRAY_ITALIC = s -> s.withItalic(true).withColor(ChatFormatting.DARK_GRAY);
   private final Component[] description;

   public RotateBlockWithHoverText(Properties pProperties, String... description) {
      super(pProperties);
      this.description = new Component[description.length];

      for (int i = 0; i < description.length; i++) {
         this.description[i] = Component.literal(description[i]).withStyle(DARK_GRAY_ITALIC);
      }
   }

   public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pTooltipComponents.addAll(Arrays.asList(this.description));
   }
}

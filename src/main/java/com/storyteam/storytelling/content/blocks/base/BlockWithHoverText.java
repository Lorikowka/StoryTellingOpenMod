package com.storyteam.storytelling.content.blocks.base;

import java.util.Arrays;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.Nullable;

public class BlockWithHoverText extends BushBlock {
   public static final Style DARK_GRAY_ITALIC = Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY);
   private final Component[] description;

   public BlockWithHoverText(Properties pProperties, Component... description) {
      super(pProperties);
      this.description = description;
   }

   public BlockWithHoverText(Properties pProperties, String... description) {
      super(pProperties);
      this.description = new Component[description.length];

      for (int i = 0; i < description.length; i++) {
         this.description[i] = Component.literal(description[i]).withStyle(DARK_GRAY_ITALIC);
      }
   }

   public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
      pTooltip.addAll(Arrays.asList(this.description));
   }
}

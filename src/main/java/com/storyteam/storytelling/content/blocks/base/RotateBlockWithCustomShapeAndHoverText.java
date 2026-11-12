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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RotateBlockWithCustomShapeAndHoverText extends RotateBlockWithCustomShape {
   public static final UnaryOperator<Style> DARK_GRAY_ITALIC = s -> s.withItalic(true).withColor(ChatFormatting.DARK_GRAY);
   private final Component[] description;

   public RotateBlockWithCustomShapeAndHoverText(Properties pProperties, VoxelShape shape, String... description) {
      super(pProperties, shape);
      this.description = new Component[description.length];

      for (int i = 0; i < description.length; i++) {
         this.description[i] = Component.literal(description[i]).withStyle(DARK_GRAY_ITALIC);
      }
   }

   public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pTooltipComponents.addAll(Arrays.asList(this.description));
   }
}

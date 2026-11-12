package com.storyteam.storytelling.system.dialog;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.util.RenderTools;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import org.apache.commons.lang3.tuple.Pair;

public class AnswerButton extends Button {
   public final int TOP_AND_BOTTOM_SIZE;
   public final int BUTTON_TEXTURE_WIDTH;
   public final ResourceLocation TOP;
   public final ResourceLocation TOP_S;
   public final ResourceLocation BOTTOM;
   public final ResourceLocation BOTTOM_S;
   public final ResourceLocation MIDDLE;
   public final ResourceLocation MIDDLE_S;
   public final ResourceLocation TOP_SELECTED;
   public final ResourceLocation TOP_SELECTED_S;
   public final ResourceLocation BOTTOM_SELECTED;
   public final ResourceLocation BOTTOM_SELECTED_S;
   public final ResourceLocation MIDDLE_SELECTED;
   public final ResourceLocation MIDDLE_SELECTED_S;
   private final int yTextOffset;
   private int xTextOffsetLeft = 15;
   private static int xTextOffsetRight = 12;
   public DialogScreen screen;
   public Answer answer;
   public List<Pair<FormattedCharSequence, FormattedText>> lines;
   public float textScale = 1.0F;
   public boolean isInCostume = BuiltInRegistries.ITEM
      .getKey(Minecraft.getInstance().player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex()).getItem())
      .toString()
      .contains("protect_suit");

   public AnswerButton(DialogScreen owner, Answer answer, int x, int y, float textScale, OnPress pOnPress) {
      super(x, y, 166, 0, answer.answerText, pOnPress, DEFAULT_NARRATION);
      this.TOP_AND_BOTTOM_SIZE = 16;
      this.BUTTON_TEXTURE_WIDTH = 166;
      this.TOP = St.tx("gui/dialog/answer_top");
      this.TOP_S = St.tx("gui/dialog/answer_top_s");
      this.BOTTOM = St.tx("gui/dialog/answer_bottom");
      this.BOTTOM_S = St.tx("gui/dialog/answer_bottom_s");
      this.MIDDLE = St.tx("gui/dialog/answer_mid");
      this.MIDDLE_S = St.tx("gui/dialog/answer_mid_s");
      this.TOP_SELECTED = St.tx("gui/dialog/answer_top_selected");
      this.TOP_SELECTED_S = St.tx("gui/dialog/answer_top_selected_s");
      this.BOTTOM_SELECTED = St.tx("gui/dialog/answer_bottom_selected");
      this.BOTTOM_SELECTED_S = St.tx("gui/dialog/answer_bottom_selected_s");
      this.MIDDLE_SELECTED = St.tx("gui/dialog/answer_mid_selected");
      this.MIDDLE_SELECTED_S = St.tx("gui/dialog/answer_mid_selected_s");
      this.yTextOffset = 1;
      this.screen = owner;
      this.answer = answer;
      this.setMessage(answer.answerText);
      this.textScale = textScale;
   }

   protected void renderWidget(GuiGraphics graphics, int mx, int my, float pticks) {
      this.renderButton(graphics, mx, my, pticks);
      this.renderLines(graphics, mx, my, pticks);
   }

   public void renderButton(GuiGraphics graphics, int mx, int my, float pticks) {
      if (!this.isHovered()) {
         RenderTools.bindText(this.isInCostume ? this.TOP_S : this.TOP);
         float middleSize = (float)Mth.clamp(this.getHeight() - this.TOP_AND_BOTTOM_SIZE * 2, 0, Integer.MAX_VALUE);
         RenderTools.blitWithBlend(
            graphics,
            (float)this.getX(),
            (float)this.getY(),
            0.0F,
            0.0F,
            (float)this.BUTTON_TEXTURE_WIDTH,
            (float)this.TOP_AND_BOTTOM_SIZE,
            (float)this.BUTTON_TEXTURE_WIDTH,
            (float)this.TOP_AND_BOTTOM_SIZE,
            1.0F
         );
         RenderTools.bindText(this.isInCostume ? this.MIDDLE_S : this.MIDDLE);
         RenderTools.blitWithBlend(
            graphics,
            (float)this.getX(),
            (float)(this.getY() + this.TOP_AND_BOTTOM_SIZE),
            0.0F,
            0.0F,
            (float)this.BUTTON_TEXTURE_WIDTH,
            middleSize,
            (float)this.BUTTON_TEXTURE_WIDTH,
            middleSize,
            1.0F
         );
         RenderTools.bindText(this.isInCostume ? this.BOTTOM_S : this.BOTTOM);
         RenderTools.blitWithBlend(
            graphics,
            (float)this.getX(),
            (float)this.getY() + middleSize + (float)this.TOP_AND_BOTTOM_SIZE,
            0.0F,
            0.0F,
            (float)this.BUTTON_TEXTURE_WIDTH,
            (float)this.TOP_AND_BOTTOM_SIZE,
            (float)this.BUTTON_TEXTURE_WIDTH,
            (float)this.TOP_AND_BOTTOM_SIZE,
            1.0F
         );
      } else {
         RenderTools.bindText(this.isInCostume ? this.TOP_SELECTED_S : this.TOP_SELECTED);
         float middleSize = (float)Mth.clamp(this.getHeight() - this.TOP_AND_BOTTOM_SIZE * 2, 0, Integer.MAX_VALUE);
         RenderTools.blitWithBlend(
            graphics,
            (float)this.getX(),
            (float)this.getY(),
            0.0F,
            0.0F,
            (float)this.BUTTON_TEXTURE_WIDTH,
            (float)this.TOP_AND_BOTTOM_SIZE,
            (float)this.BUTTON_TEXTURE_WIDTH,
            (float)this.TOP_AND_BOTTOM_SIZE,
            1.0F
         );
         RenderTools.bindText(this.isInCostume ? this.MIDDLE_SELECTED_S : this.MIDDLE_SELECTED);
         RenderTools.blitWithBlend(
            graphics,
            (float)this.getX(),
            (float)(this.getY() + this.TOP_AND_BOTTOM_SIZE),
            0.0F,
            0.0F,
            (float)this.BUTTON_TEXTURE_WIDTH,
            middleSize,
            (float)this.BUTTON_TEXTURE_WIDTH,
            middleSize,
            1.0F
         );
         RenderTools.bindText(this.isInCostume ? this.BOTTOM_SELECTED_S : this.BOTTOM_SELECTED);
         RenderTools.blitWithBlend(
            graphics,
            (float)this.getX(),
            (float)this.getY() + middleSize + (float)this.TOP_AND_BOTTOM_SIZE,
            0.0F,
            0.0F,
            (float)this.BUTTON_TEXTURE_WIDTH,
            (float)this.TOP_AND_BOTTOM_SIZE,
            (float)this.BUTTON_TEXTURE_WIDTH,
            (float)this.TOP_AND_BOTTOM_SIZE,
            1.0F
         );
      }
   }

   public void renderLines(GuiGraphics graphics, int mx, int my, float pticks) {
      float lineHeight = 9.0F * this.textScale;

      for (int i = 0; i < this.lines.size(); i++) {
         Pair<FormattedCharSequence, FormattedText> linePair = this.lines.get(i);
         FormattedCharSequence formattedText = (FormattedCharSequence)linePair.getLeft();
         Component component = Component.literal(((FormattedText)linePair.getRight()).getString());
         float costumeOffset = this.isInCostume ? 0.5F : -0.5F;
         if (this.answer.locked && Screen.hasShiftDown()) {
            component = component.copy().withStyle(ChatFormatting.STRIKETHROUGH);
            if (this.answer.centred) {
               float width = (float)Minecraft.getInstance().font.width(formattedText);
               RenderTools.renderScaledText(
                  graphics,
                  (float)this.getX() + (float)this.getWidth() / 2.0F - width / 2.0F,
                  (float)(this.getY() + (this.yTextOffset - 1)) + costumeOffset + (float)i * lineHeight + (float)this.TOP_AND_BOTTOM_SIZE,
                  this.textScale,
                  component,
                  DialogScreen.colorTextDialog
               );
            } else {
               RenderTools.renderScaledText(
                  graphics,
                  (float)(this.getX() + this.xTextOffsetLeft),
                  (float)(this.getY() + (this.yTextOffset - 1)) + costumeOffset + (float)i * lineHeight + (float)this.TOP_AND_BOTTOM_SIZE,
                  this.textScale,
                  component,
                  DialogScreen.colorTextDialog
               );
            }
         } else if (this.answer.centred) {
            float width = (float)Minecraft.getInstance().font.width(formattedText);
            RenderTools.renderScaledText(
               graphics,
               (float)this.getX() + (float)this.getWidth() / 2.0F - width / 2.0F,
               (float)(this.getY() + (this.yTextOffset - 1)) + costumeOffset + (float)i * lineHeight + (float)this.TOP_AND_BOTTOM_SIZE,
               this.textScale,
               formattedText,
               DialogScreen.colorTextDialog
            );
         } else {
            RenderTools.renderScaledText(
               graphics,
               (float)(this.getX() + this.xTextOffsetLeft),
               (float)(this.getY() + (this.yTextOffset - 1)) + costumeOffset + (float)i * lineHeight + (float)this.TOP_AND_BOTTOM_SIZE,
               this.textScale,
               formattedText,
               DialogScreen.colorTextDialog
            );
         }
      }
   }

   public void setMessage(Component message) {
      super.setMessage(message);
      this.answer.answerText = message;
      Font font = Minecraft.getInstance().font;
      int maxTextWidth = Math.round((float)(this.width - this.xTextOffsetLeft - xTextOffsetRight) / this.textScale);
      List<FormattedCharSequence> formattedTexts = font.split(message, maxTextWidth);
      List<FormattedText> rawTexts = font.getSplitter().splitLines(message, maxTextWidth, Style.EMPTY);
      this.lines = IntStream.range(0, formattedTexts.size()).mapToObj(i -> Pair.of(formattedTexts.get(i), rawTexts.get(i))).collect(Collectors.toList());
      this.height = (int)Math.ceil((double)((float)(formattedTexts.size() * 9) * this.textScale)) + (this.yTextOffset - 2) * 2 + this.TOP_AND_BOTTOM_SIZE * 2;
   }

   public void onPress() {
      if (!this.answer.locked) {
         super.onPress();
         if (this.answer.action != null) {
            this.answer.action.run();
         }
      }
   }
}

package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.net.PacketLuaChoiceSelection;
import com.storyteam.storytelling.util.RenderTools;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.zeith.hammerlib.net.Network;

/** Lua choices styled after the original Storytelling dialog, without its old dialog lifecycle. */
public final class LuaChoiceScreen extends Screen {
   private static final ResourceLocation SHADOW = St.tx("gui/dialog/dialog_shadow");
   private static final ResourceLocation NAMEPLATE = St.tx("gui/dialog/dialog_name");
   private static final int TEXT_COLOR = 0x42C7FF;
   private final int scriptId;
   private final int actionIndex;
   private final String[] labels;
   private final List<ChoiceButton> buttons = new ArrayList<>();
   private int contentHeight;
   private int scroll;

   public LuaChoiceScreen(int scriptId, int actionIndex, String prompt, String[] labels) {
      super(Component.literal(prompt));
      this.scriptId = scriptId;
      this.actionIndex = actionIndex;
      this.labels = labels;
   }

   public boolean matches(int scriptId, int actionIndex) {
      return this.scriptId == scriptId && this.actionIndex == actionIndex;
   }

   @Override protected void init() {
      buttons.clear();
      int y = 8;
      for (int i = 0; i < labels.length; i++) {
         if (labels[i] == null) continue;
         int number = i + 1;
         ChoiceButton button = new ChoiceButton(8, y, Component.literal(number + ". " + labels[i]), b -> {
            Network.sendToServer(new PacketLuaChoiceSelection(scriptId, actionIndex, number));
            onClose();
         });
         buttons.add(addRenderableWidget(button));
         y += button.getHeight() + 5;
      }
      contentHeight = y;
      positionButtons();
   }

   private void positionButtons() {
      int y = 8 - scroll;
      for (ChoiceButton button : buttons) {
         button.setY(y);
         button.active = y + button.getHeight() <= height - 80 && y >= 0;
         y += button.getHeight() + 5;
      }
   }

   @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      int maxScroll = Math.max(0, contentHeight - (height - 80));
      scroll = Math.max(0, Math.min(maxScroll, scroll - (int) Math.signum(amount) * 20));
      positionButtons();
      return true;
   }

   @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      int shadowHeight = Math.min(270, height);
      RenderTools.bindText(SHADOW);
      RenderTools.blitWithBlend(graphics, 0, height - shadowHeight, width, shadowHeight,
         0, 0, 480, 270, 480, 270, 1);
      RenderTools.bindText(NAMEPLATE);
      RenderTools.blitWithBlend(graphics, 28, height - 62, 103, 3,
         0, 0, 103, 3, 103, 3, 1);
      graphics.drawCenteredString(font, Component.literal("ВЫБОР"), 80, height - 72, TEXT_COLOR);
      List<FormattedCharSequence> prompt = font.split(title, Math.max(100, width - 160));
      for (int i = 0; i < prompt.size() && i < 4; i++) {
         graphics.drawString(font, prompt.get(i), 36, height - 50 + i * 9, TEXT_COLOR, false);
      }
      graphics.enableScissor(0, 0, width, Math.max(0, height - 80));
      super.render(graphics, mouseX, mouseY, partialTick);
      graphics.disableScissor();
   }

   @Override public boolean isPauseScreen() { return false; }

   private static final class ChoiceButton extends Button {
      private static final ResourceLocation TOP = St.tx("gui/dialog/answer_top");
      private static final ResourceLocation MID = St.tx("gui/dialog/answer_mid");
      private static final ResourceLocation BOTTOM = St.tx("gui/dialog/answer_bottom");
      private static final ResourceLocation TOP_SELECTED = St.tx("gui/dialog/answer_top_selected");
      private static final ResourceLocation MID_SELECTED = St.tx("gui/dialog/answer_mid_selected");
      private static final ResourceLocation BOTTOM_SELECTED = St.tx("gui/dialog/answer_bottom_selected");
      private final List<FormattedCharSequence> lines;

      ChoiceButton(int x, int y, Component label, OnPress onPress) {
         super(x, y, 166, buttonHeight(label), label, onPress, DEFAULT_NARRATION);
         lines = Minecraft.getInstance().font.split(label, 139);
      }

      private static int buttonHeight(Component label) {
         return 30 + Minecraft.getInstance().font.split(label, 139).size() * 9;
      }

      @Override protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
         boolean selected = isHoveredOrFocused();
         drawSegment(graphics, selected ? TOP_SELECTED : TOP, getY(), 16);
         drawSegment(graphics, selected ? MID_SELECTED : MID, getY() + 16, getHeight() - 32);
         drawSegment(graphics, selected ? BOTTOM_SELECTED : BOTTOM, getY() + getHeight() - 16, 16);
         int textY = getY() + 15;
         for (FormattedCharSequence line : lines) {
            graphics.drawString(Minecraft.getInstance().font, line, getX() + 15, textY, TEXT_COLOR, false);
            textY += 9;
         }
      }

      private void drawSegment(GuiGraphics graphics, ResourceLocation texture, int y, int segmentHeight) {
         if (segmentHeight <= 0) return;
         RenderTools.bindText(texture);
         RenderTools.blitWithBlend(graphics, getX(), y, 166, segmentHeight,
            0, 0, 166, 16, 166, 16, 1);
      }
   }
}

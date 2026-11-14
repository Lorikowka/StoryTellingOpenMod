package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.St;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

/** Read-only quest journal; the server sends a fresh snapshot on each opening. */
public final class QuestJournalScreen extends Screen {
   private static final ResourceLocation BACKGROUND = St.tx("gui/quests/bg");
   private final List<String> lines;
   private int scroll;
   private int contentHeight;

   public QuestJournalScreen(List<String> lines) {
      super(Component.literal("Журнал заданий"));
      this.lines = lines;
   }

   @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      renderBackground(graphics);
      int panelX = (width - 240) / 2;
      int panelY = (height - 225) / 2;
      graphics.blit(BACKGROUND, panelX, panelY, 0, 0, 240, 225, 240, 225);
      int left = panelX + 20;
      int right = panelX + 220;
      graphics.drawCenteredString(font, title, width / 2, panelY + 9, 0x42C7FF);
      graphics.enableScissor(left, panelY + 28, right, panelY + 198);
      int y = panelY + 32 - scroll;
      if (lines.isEmpty()) graphics.drawString(font, "Заданий пока нет", left, y, 0xAAAAAA);
      for (String line : lines) {
         if (line.isEmpty()) { y += 6; continue; }
         for (FormattedCharSequence part : font.split(Component.literal(line), right - left - 8)) {
            graphics.drawString(font, part, left, y, 0xFFFFFF, false);
            y += 11;
         }
      }
      contentHeight = y + scroll - (panelY + 32);
      graphics.disableScissor();
      graphics.drawCenteredString(font, "Esc — закрыть", width / 2, panelY + 204, 0x888888);
   }

   @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      scroll = Math.max(0, Math.min(Math.max(0, contentHeight - 166), scroll - (int) Math.signum(amount) * 24));
      return true;
   }

   @Override public boolean isPauseScreen() { return false; }
}

package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.script.story.QuestJournal;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

/** Quest list and details using the quest textures from the original mod. */
public final class QuestJournalScreen extends Screen {
   private static final ResourceLocation BACKGROUND = St.tx("gui/quests/bg");
   private static final ResourceLocation DETAILS = St.tx("gui/quests/bg_2");
   private static final ResourceLocation BUTTON = St.tx("gui/quests/button");
   private static final ResourceLocation CURRENT = St.tx("gui/quests/quests_current");
   private static final ResourceLocation COMPLETED = St.tx("gui/quests/quests_completed");
   private static final ResourceLocation FAILED = St.tx("gui/quests/quests_failed");
   private static final int PANEL_WIDTH = 384;
   private static final int PANEL_HEIGHT = 230;
   private static final int ROW_HEIGHT = 35;
   private final List<QuestJournal.Entry> entries;
   private QuestJournal.Status tab = QuestJournal.Status.ACTIVE;
   private int selected;
   private int listScroll;
   private int detailScroll;

   public QuestJournalScreen(List<QuestJournal.Entry> entries) {
      super(Component.literal("Журнал заданий"));
      this.entries = List.copyOf(entries);
      if (count(tab) == 0 && count(QuestJournal.Status.COMPLETED) > 0) tab = QuestJournal.Status.COMPLETED;
      else if (count(tab) == 0 && count(QuestJournal.Status.FAILED) > 0) tab = QuestJournal.Status.FAILED;
   }

   private int count(QuestJournal.Status status) {
      return (int) entries.stream().filter(entry -> entry.status() == status).count();
   }

   private List<QuestJournal.Entry> visible() {
      return entries.stream().filter(entry -> entry.status() == tab).toList();
   }

   private int panelX() { return (width - PANEL_WIDTH) / 2; }
   private int panelY() { return (height - PANEL_HEIGHT) / 2; }

   @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      renderBackground(graphics);
      int x = panelX();
      int y = panelY();
      graphics.blit(BACKGROUND, x, y + 2, 0, 0, 240, 225, 240, 225);
      graphics.blit(DETAILS, x + 240, y, 0, 0, 144, 230, 144, 230);
      graphics.drawCenteredString(font, title, x + 120, y + 10, 0x42C7FF);

      drawTab(graphics, QuestJournal.Status.ACTIVE, "Текущие", x + 16, y + 31);
      drawTab(graphics, QuestJournal.Status.COMPLETED, "Готовые", x + 91, y + 31);
      drawTab(graphics, QuestJournal.Status.FAILED, "Провалены", x + 160, y + 31);

      List<QuestJournal.Entry> shown = visible();
      int maxListScroll = Math.max(0, shown.size() - 4);
      listScroll = Math.min(listScroll, maxListScroll);
      selected = Math.max(0, Math.min(selected, shown.size() - 1));
      graphics.enableScissor(x + 10, y + 55, x + 228, y + 198);
      for (int row = 0; row < 4 && row + listScroll < shown.size(); row++) {
         QuestJournal.Entry entry = shown.get(row + listScroll);
         int rowY = y + 55 + row * ROW_HEIGHT;
         int state = row + listScroll == selected ? 70 : 0;
         graphics.blit(BUTTON, x + 18, rowY, 0, state, 131, 35, 131, 105);
         List<FormattedCharSequence> label = font.split(Component.literal(entry.title()), 112);
         if (!label.isEmpty()) graphics.drawString(font, label.get(0), x + 25, rowY + 10, 0xE5F5FF, false);
         graphics.blit(icon(entry.status()), x + 166, rowY + 6, 0, 0, 23, 23, 23, 69);
      }
      graphics.disableScissor();
      if (shown.isEmpty()) {
         graphics.drawCenteredString(font, "Заданий нет", x + 120, y + 111, 0xA6B8C6);
      } else if (shown.size() > 4) {
         graphics.drawString(font, (listScroll + 1) + "–" + Math.min(listScroll + 4, shown.size())
            + " / " + shown.size(), x + 164, y + 202, 0x7E9AAA, false);
      }

      if (!shown.isEmpty()) drawDetails(graphics, shown.get(selected), x + 240, y);
      graphics.drawCenteredString(font, "Esc — закрыть", x + 120, y + 209, 0x98AAB5);
   }

   private void drawTab(GuiGraphics graphics, QuestJournal.Status status, String name, int x, int y) {
      int color = tab == status ? 0x42C7FF : 0x9EACB7;
      graphics.drawString(font, name, x, y, color, false);
      if (tab == status) graphics.fill(x, y + 12, x + font.width(name), y + 13, 0xFF42C7FF);
   }

   private static ResourceLocation icon(QuestJournal.Status status) {
      return switch (status) {
         case ACTIVE, AVAILABLE -> CURRENT;
         case COMPLETED -> COMPLETED;
         case FAILED -> FAILED;
      };
   }

   private void drawDetails(GuiGraphics graphics, QuestJournal.Entry entry, int x, int y) {
      graphics.enableScissor(x + 10, y + 20, x + 134, y + 207);
      int lineY = y + 24 - detailScroll;
      lineY = drawWrapped(graphics, entry.title(), x + 13, lineY, 112, 0x42C7FF) + 8;
      if (!entry.description().isBlank()) {
         lineY = drawWrapped(graphics, entry.description(), x + 13, lineY, 112, 0xE5F5FF) + 11;
      }
      for (QuestJournal.TaskEntry task : entry.tasks()) {
         graphics.blit(task.done() ? COMPLETED : CURRENT, x + 11, lineY - 4, 0, 0, 23, 23, 23, 69);
         int end = drawWrapped(graphics, task.title(), x + 36, lineY, 93,
            task.done() ? 0x9CCFC2 : 0xC8D5DE);
         lineY = Math.max(lineY + 23, end + 4);
      }
      graphics.disableScissor();
   }

   private int drawWrapped(GuiGraphics graphics, String text, int x, int y, int width, int color) {
      for (FormattedCharSequence line : font.split(Component.literal(text), width)) {
         graphics.drawString(font, line, x, y, color, false);
         y += 11;
      }
      return y;
   }

   @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
      int x = panelX();
      int y = panelY();
      if (mouseY >= y + 28 && mouseY < y + 48) {
         QuestJournal.Status next = null;
         if (mouseX >= x + 12 && mouseX < x + 83) next = QuestJournal.Status.ACTIVE;
         else if (mouseX >= x + 86 && mouseX < x + 155) next = QuestJournal.Status.COMPLETED;
         else if (mouseX >= x + 156 && mouseX < x + 230) next = QuestJournal.Status.FAILED;
         if (next != null) {
            tab = next;
            selected = listScroll = detailScroll = 0;
            return true;
         }
      }
      if (mouseX >= x + 18 && mouseX < x + 207 && mouseY >= y + 55 && mouseY < y + 195) {
         int index = listScroll + ((int) mouseY - y - 55) / ROW_HEIGHT;
         if (index < visible().size()) {
            selected = index;
            detailScroll = 0;
            return true;
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      int x = panelX();
      int y = panelY();
      int delta = (int) Math.signum(amount);
      if (mouseX >= x + 240 && mouseX < x + 384 && mouseY >= y + 20 && mouseY < y + 207) {
         detailScroll = Math.max(0, detailScroll - delta * 18);
      } else {
         listScroll = Math.max(0, Math.min(Math.max(0, visible().size() - 4), listScroll - delta));
      }
      return true;
   }

   @Override public boolean isPauseScreen() { return false; }
}

package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.script.story.QuestJournal;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

/** Two-pane quest journal based on the original Storytelling quest textures. */
public final class QuestJournalScreen extends Screen {
   private static final ResourceLocation BACKGROUND = St.tx("gui/quests/bg");
   private static final ResourceLocation BUTTON = St.tx("gui/quests/button");
   private static final ResourceLocation TASK_LINE = St.tx("gui/quests/quest_task_line");
   private static final ResourceLocation CURRENT = St.tx("gui/quests/quests_current");
   private static final ResourceLocation COMPLETED = St.tx("gui/quests/quests_completed");
   private static final ResourceLocation FAILED = St.tx("gui/quests/quests_failed");
   private static final int DESIGN_WIDTH = 640;
   private static final int DESIGN_HEIGHT = 360;
   private static final int PANEL_X = 282;
   private static final int PANEL_Y = 15;
   private static final int PANEL_WIDTH = 346;
   private static final int PANEL_HEIGHT = 340;
   private static final int LIST_X = 10;
   private static final int LIST_Y = 54;
   private static final int LIST_WIDTH = 188;
   private static final int LIST_HEIGHT = 41;
   private static final int LIST_STEP = 45;
   private static final int VISIBLE_ROWS = 6;
   private static final int CONTENT_TOP = 82;
   private static final int CONTENT_BOTTOM = 327;
   private static final float BODY_SCALE = 1.18F;
   private final List<QuestJournal.Entry> entries;
   private QuestJournal.Status tab = QuestJournal.Status.ACTIVE;
   private int selected;
   private int listScroll;
   private int detailScroll;
   private int detailMaxScroll;
   private float screenScale;
   private float screenX;
   private float screenY;

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

   private void layout() {
      screenScale = Math.min(width / (float) DESIGN_WIDTH, height / (float) DESIGN_HEIGHT);
      screenX = (width - DESIGN_WIDTH * screenScale) / 2F;
      screenY = (height - DESIGN_HEIGHT * screenScale) / 2F;
   }

   private double logicalX(double mouseX) { return (mouseX - screenX) / screenScale; }
   private double logicalY(double mouseY) { return (mouseY - screenY) / screenScale; }

   @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      renderBackground(graphics);
      layout();
      double pointerX = logicalX(mouseX);
      double pointerY = logicalY(mouseY);
      graphics.pose().pushPose();
      graphics.pose().translate(screenX, screenY, 0);
      graphics.pose().scale(screenScale, screenScale, 1);

      graphics.fill(0, 0, 206, DESIGN_HEIGHT, 0x55000000);
      sprite(graphics, BACKGROUND, PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT,
         0, 0, 240, 225, 240, 225);
      graphics.fill(15, 40, 190, 42, 0xDD050607);
      graphics.fill(190, 40, 197, 42, 0xBB44494B);
      drawTabs(graphics, pointerX, pointerY);
      drawList(graphics);
      List<QuestJournal.Entry> shown = visible();
      if (!shown.isEmpty()) drawDetails(graphics, shown.get(selected));
      graphics.pose().popPose();
   }

   private void drawTabs(GuiGraphics graphics, double mouseX, double mouseY) {
      QuestJournal.Status[] tabs = {
         QuestJournal.Status.ACTIVE, QuestJournal.Status.COMPLETED, QuestJournal.Status.FAILED
      };
      for (int i = 0; i < tabs.length; i++) {
         int x = 64 + i * 36;
         boolean hover = mouseX >= x && mouseX < x + 28 && mouseY >= 0 && mouseY < 30;
         int state = tabs[i] == tab ? 46 : hover ? 23 : 0;
         sprite(graphics, icon(tabs[i]), x, 0, 28, 30, 0, state, 23, 23, 23, 69);
      }
   }

   private void drawList(GuiGraphics graphics) {
      List<QuestJournal.Entry> shown = visible();
      listScroll = Math.min(listScroll, Math.max(0, shown.size() - VISIBLE_ROWS));
      selected = Math.max(0, Math.min(selected, shown.size() - 1));
      for (int row = 0; row < VISIBLE_ROWS && row + listScroll < shown.size(); row++) {
         QuestJournal.Entry entry = shown.get(row + listScroll);
         int rowY = LIST_Y + row * LIST_STEP;
         int state = row + listScroll == selected ? 70 : 0;
         sprite(graphics, BUTTON, LIST_X, rowY, LIST_WIDTH, LIST_HEIGHT,
            0, state, 131, 35, 131, 105);
         drawText(graphics, firstLine(entry.title(), 160, 1.18F),
            LIST_X + 14, rowY + 8, 1.18F, 0xF3F3F3, false);
         String source = entry.source().isBlank() ? "Сюжет" : entry.source();
         drawText(graphics, firstLine(source, 158, 1.05F),
            LIST_X + 16, rowY + 23, 1.05F, 0xA5A1A1, false);
      }
      if (shown.isEmpty()) {
         drawText(graphics, "Заданий нет", 34, LIST_Y + 13, 1.12F, 0xBDC7CC, false);
      }
   }

   private static ResourceLocation icon(QuestJournal.Status status) {
      return switch (status) {
         case ACTIVE, AVAILABLE -> CURRENT;
         case COMPLETED -> COMPLETED;
         case FAILED -> FAILED;
      };
   }

   private void drawDetails(GuiGraphics graphics, QuestJournal.Entry entry) {
      List<FormattedCharSequence> heading = font.split(Component.literal(entry.title()), 218);
      for (int i = 0; i < Math.min(2, heading.size()); i++) {
         drawText(graphics, heading.get(i), PANEL_X + PANEL_WIDTH / 2,
            45 + i * 16, 1.45F, 0xFFFFFF, true);
      }
      int focus = 0;
      if (entry.status() == QuestJournal.Status.ACTIVE) {
         for (int i = 0; i < entry.tasks().size(); i++) {
            if (!entry.tasks().get(i).done()) { focus = i; break; }
         }
      }
      int contentHeight = 8;
      for (QuestJournal.TaskEntry task : entry.tasks()) {
         contentHeight += Math.max(23, wrappedHeight(task.title(), 290, BODY_SCALE) + 5);
      }
      contentHeight += 9 + wrappedHeight(entry.description(), 300, BODY_SCALE);
      detailMaxScroll = Math.max(0, contentHeight - (CONTENT_BOTTOM - CONTENT_TOP));
      detailScroll = Math.min(detailScroll, detailMaxScroll);

      int y = CONTENT_TOP - detailScroll;
      for (int i = 0; i < entry.tasks().size(); i++) {
         QuestJournal.TaskEntry task = entry.tasks().get(i);
         if (i == focus && y + 18 >= CONTENT_TOP && y < CONTENT_BOTTOM) {
            sprite(graphics, TASK_LINE, PANEL_X + 21, y, 305, 18,
               0, 0, 225, 16, 225, 16);
         }
         if (y >= CONTENT_TOP && y < CONTENT_BOTTOM - 14) {
            drawText(graphics, "›", PANEL_X + 24, y + 2, 1.4F,
               i == focus ? 0xE7F5FF : 0x86A1AD, false);
         }
         int end = drawWrapped(graphics, task.title(), PANEL_X + 38, y + 2,
            290, BODY_SCALE, i == focus ? 0xF4F8FC : 0xB6C6CF);
         y = Math.max(y + 23, end + 3);
      }
      y += 9;
      drawWrapped(graphics, entry.description(), PANEL_X + 23, y, 300, BODY_SCALE, 0xF2F2F2);
   }

   private int wrappedHeight(String text, int width, float textScale) {
      int lines = font.split(Component.literal(text), (int) (width / textScale)).size();
      return lines * 14;
   }

   private int drawWrapped(GuiGraphics graphics, String text, int x, int y,
                           int width, float textScale, int color) {
      for (FormattedCharSequence line : font.split(Component.literal(text), (int) (width / textScale))) {
         if (y >= CONTENT_TOP && y < CONTENT_BOTTOM - 12) {
            drawText(graphics, line, x, y, textScale, color, false);
         }
         y += 14;
      }
      return y;
   }

   private String firstLine(String text, int width, float textScale) {
      String value = text;
      while (!value.isEmpty() && font.width(value) * textScale > width) {
         value = value.substring(0, value.length() - 1);
      }
      return value.equals(text) ? value : value + "…";
   }

   private void drawText(GuiGraphics graphics, String text, int x, int y,
                         float textScale, int color, boolean centered) {
      drawText(graphics, Component.literal(text).getVisualOrderText(), x, y, textScale, color, centered);
   }

   private void drawText(GuiGraphics graphics, FormattedCharSequence text, int x, int y,
                         float textScale, int color, boolean centered) {
      graphics.pose().pushPose();
      graphics.pose().translate(x, y, 0);
      graphics.pose().scale(textScale, textScale, 1);
      if (centered) graphics.drawCenteredString(font, text, 0, 0, color);
      else graphics.drawString(font, text, 0, 0, color, false);
      graphics.pose().popPose();
   }

   private static void sprite(GuiGraphics graphics, ResourceLocation texture,
                              int x, int y, int width, int height,
                              int u, int v, int sourceWidth, int sourceHeight,
                              int textureWidth, int textureHeight) {
      graphics.pose().pushPose();
      graphics.pose().translate(x, y, 0);
      graphics.pose().scale(width / (float) sourceWidth, height / (float) sourceHeight, 1);
      graphics.blit(texture, 0, 0, u, v, sourceWidth, sourceHeight, textureWidth, textureHeight);
      graphics.pose().popPose();
   }

   @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
      layout();
      double x = logicalX(mouseX);
      double y = logicalY(mouseY);
      if (y >= 0 && y < 30) {
         QuestJournal.Status[] tabs = {
            QuestJournal.Status.ACTIVE, QuestJournal.Status.COMPLETED, QuestJournal.Status.FAILED
         };
         for (int i = 0; i < tabs.length; i++) {
            int tabX = 64 + i * 36;
            if (x >= tabX && x < tabX + 28) {
               tab = tabs[i];
               selected = listScroll = detailScroll = 0;
               return true;
            }
         }
      }
      if (x >= LIST_X && x < LIST_X + LIST_WIDTH && y >= LIST_Y
         && y < LIST_Y + VISIBLE_ROWS * LIST_STEP) {
         int index = listScroll + ((int) y - LIST_Y) / LIST_STEP;
         if (index < visible().size()) {
            selected = index;
            detailScroll = 0;
            return true;
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      layout();
      double x = logicalX(mouseX);
      double y = logicalY(mouseY);
      int direction = (int) Math.signum(amount);
      if (x >= PANEL_X && x < PANEL_X + PANEL_WIDTH && y >= CONTENT_TOP && y < CONTENT_BOTTOM) {
         detailScroll = Math.max(0, Math.min(detailMaxScroll, detailScroll - direction * 28));
      } else if (x >= 0 && x < 210) {
         listScroll = Math.max(0, Math.min(Math.max(0, visible().size() - VISIBLE_ROWS),
            listScroll - direction));
      }
      return true;
   }

   @Override public boolean isPauseScreen() { return false; }
}

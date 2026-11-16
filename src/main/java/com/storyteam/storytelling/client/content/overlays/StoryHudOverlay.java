package com.storyteam.storytelling.client.content.overlays;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/** Compact quest notices at the upper left and author hints left of the crosshair. */
@EventBusSubscriber(Dist.CLIENT)
public final class StoryHudOverlay implements IGuiOverlay {
   private record Notice(String text, int ticks) { }
   private static final Deque<Notice> QUEUE = new ArrayDeque<>();
   private static Notice current;
   private static int noticeTicks;
   private static String hint;
   private static int hintTicks;

   public static void show(boolean isHint, String text, int ticks) {
      if (text == null || text.isBlank()) return;
      if (isHint) {
         hint = text;
         hintTicks = ticks;
      } else if (QUEUE.size() < 16) {
         QUEUE.addLast(new Notice(text, ticks));
      }
   }

   @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || !event.player.level().isClientSide
         || event.player != Minecraft.getInstance().player) return;
      if (hintTicks > 0 && --hintTicks == 0) hint = null;
      if (noticeTicks > 0 && --noticeTicks == 0) current = null;
      if (current == null && !QUEUE.isEmpty()) {
         current = QUEUE.removeFirst();
         noticeTicks = current.ticks;
      }
   }

   @Override public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
      if (Minecraft.getInstance().options.hideGui) return;
      if (current != null) drawNotice(gui, graphics, width);
      if (hint != null && hintTicks > 0) drawHint(gui, graphics, width, height);
   }

   private static void drawNotice(ForgeGui gui, GuiGraphics graphics, int width) {
      int boxWidth = Math.min(320, width - 16);
      if (boxWidth < 80) return;
      List<FormattedCharSequence> lines = gui.getFont().split(Component.literal(current.text), boxWidth - 34);
      int boxHeight = 10 + 10 * Math.min(2, lines.size());
      graphics.fill(7, 7, boxWidth + 7, 7 + boxHeight, 0xCC10131A);
      graphics.fill(7, 7, 9, 7 + boxHeight, 0xFF2E9ABE);
      graphics.fill(13, 12, 27, 26, 0xFF202020);
      graphics.fill(15, 14, 25, 24, 0xFFE1BB31);
      graphics.drawString(gui.getFont(), "!", 19, 14, 0xFF151515, false);
      for (int i = 0; i < Math.min(2, lines.size()); i++)
         graphics.drawString(gui.getFont(), lines.get(i), 32, 12 + 10 * i, 0xFFF2F2F2, false);
   }

   private static void drawHint(ForgeGui gui, GuiGraphics graphics, int width, int height) {
      int boxWidth = Math.min(170, Math.max(110, width / 3));
      int x = Math.max(8, width / 2 - boxWidth - 38);
      int y = height / 2 - 20;
      List<FormattedCharSequence> lines = gui.getFont().split(Component.literal(hint), boxWidth - 16);
      int boxHeight = 12 + 10 * Math.min(5, lines.size());
      graphics.fill(x, y, x + boxWidth, y + boxHeight, 0xD9101620);
      graphics.fill(x, y, x + 2, y + boxHeight, 0xFF2E9ABE);
      for (int i = 0; i < Math.min(5, lines.size()); i++)
         graphics.drawString(gui.getFont(), lines.get(i), x + 8, y + 6 + 10 * i, 0xFFF2F2F2, false);
   }
}

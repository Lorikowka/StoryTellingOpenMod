package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.net.PacketLuaChoiceSelection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.net.Network;

/** Story choice panel; Escape leaves the chat fallback available. */
public final class LuaChoiceScreen extends Screen {
   private final int scriptId;
   private final int actionIndex;
   private final String[] labels;

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
      int visible = 0;
      for (String label : labels) if (label != null) visible++;
      int y = (height - visible * 24) / 2 + 22;
      for (int i = 0; i < labels.length; i++) {
         if (labels[i] == null) continue;
         int number = i + 1;
         addRenderableWidget(Button.builder(Component.literal(number + ". " + labels[i]), button -> {
            Network.sendToServer(new PacketLuaChoiceSelection(scriptId, actionIndex, number));
            onClose();
         }).bounds(width / 2 - 125, y, 250, 20).build());
         y += 24;
      }
   }

   @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      renderBackground(graphics);
      graphics.drawCenteredString(font, title, width / 2, (height - labels.length * 24) / 2 - 8, 0xFFFFFF);
      super.render(graphics, mouseX, mouseY, partialTick);
   }

   @Override public boolean isPauseScreen() { return false; }
}

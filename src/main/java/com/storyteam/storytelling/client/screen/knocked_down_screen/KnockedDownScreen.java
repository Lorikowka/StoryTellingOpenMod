package com.storyteam.storytelling.client.screen.knocked_down_screen;

import com.storyteam.storytelling.net.SetFlagClientside;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button.CreateNarration;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.Network;

public class KnockedDownScreen extends Screen {
   protected static final CreateNarration DEFAULT_NARRATION = Supplier::get;
   private int relX;
   private int relY;
   private boolean withButton = true;
   private int id;

   public KnockedDownScreen(int id) {
      super(Component.empty());
      this.id = id;
   }

   public KnockedDownScreen(boolean withButton) {
      super(Component.empty());
      this.withButton = withButton;
   }

   protected void init() {
      int width = this.minecraft.getWindow().getWidth();
      int height = this.minecraft.getWindow().getHeight();
      int scale = (int)this.minecraft.getWindow().getGuiScale();
      this.relX = (width / scale - 183) / 2 - 40;
      this.relY = (height - 218 * scale) / 2 / scale;
      if (this.withButton) {
         this.addRenderableWidget(new ButtonKnock(this.relX + 90, this.relY + 200, 80, 20, Component.literal("Встать"), btn -> {
            Minecraft mc = Minecraft.getInstance();
            Player p = Minecraft.getInstance().player;
            Minecraft.getInstance().setScreen(null);
            Network.sendToServer(new SetFlagClientside(this.id, "woke_up", false));
         }, DEFAULT_NARRATION));
      }

      super.init();
   }

   public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      if (pMouseX < 10.0 && pMouseY < 10.0) {
         Player p = Minecraft.getInstance().player;
         Minecraft.getInstance().setScreen(null);
      }

      return super.mouseClicked(pMouseX, pMouseY, pButton);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean isPauseScreen() {
      return false;
   }
}

package com.storyteam.storytelling.client.screen;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class DefaultScreen extends Screen {
   protected int relX;
   protected int relY;

   public DefaultScreen() {
      super(Component.literal(""));
   }

   protected void init() {
      super.init();
      Window window = Minecraft.getInstance().getWindow();
      int height = window.getGuiScaledHeight();
      int width = window.getGuiScaledWidth();
      this.relX = width / 2 - this.getScreenWidth() / 2;
      this.relY = height / 2 - this.getScreenHeight() / 2;
   }

   public abstract int getScreenWidth();

   public abstract int getScreenHeight();
}

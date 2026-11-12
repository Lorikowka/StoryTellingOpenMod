package com.storyteam.storytelling.client.screen.base;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public abstract class DefaultMenuScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
   protected int relX;
   protected int relY;

   public DefaultMenuScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
      super(pMenu, pPlayerInventory, pTitle);
   }

   protected void init() {
      super.init();
      Window window = Minecraft.getInstance().getWindow();
      int height = window.getGuiScaledHeight();
      int width = window.getGuiScaledWidth();
      this.relX = width / 2 - this.getScreenWidth() / 2;
      this.relY = height / 2 - this.getScreenHeight() / 2;
   }

   protected void renderBg(@NotNull GuiGraphics pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
   }

   public abstract int getScreenWidth();

   public abstract int getScreenHeight();
}

package com.storyteam.storytelling.system.currency.overlay;

import com.storyteam.storytelling.system.currency.CurrencySystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class CurrencyOverlay implements IGuiOverlay {
   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      CurrencySystem.EIDOS_CURRENCY.render(guiGraphics, partialTick);
   }
}

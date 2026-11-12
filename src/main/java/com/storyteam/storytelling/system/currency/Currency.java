package com.storyteam.storytelling.system.currency;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public abstract class Currency {
   public abstract String getId();

   public abstract String getDisplayName();

   public abstract String getFormatted(int var1);

   public void render(GuiGraphics guiGraphics, float partialTick) {
   }

   protected ResourceLocation getTexture() {
      return CurrencySystem.guiTx("type/" + this.getId());
   }
}

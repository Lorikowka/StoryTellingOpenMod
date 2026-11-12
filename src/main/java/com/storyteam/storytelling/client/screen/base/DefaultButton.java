package com.storyteam.storytelling.client.screen.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.util.RenderTools;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultButton extends AbstractButton {
   @Nullable
   protected final ResourceLocation rl;
   protected final Runnable onClick;

   public DefaultButton(int pX, int pY, int pWidth, int pHeight, @Nullable ResourceLocation rl, Runnable onClick) {
      super(pX, pY, pWidth, pHeight, Component.empty());
      this.rl = rl;
      this.onClick = onClick;
   }

   public void renderWidget(@NotNull GuiGraphics gfx, int pMouseX, int pMouseY, float pPartialTick) {
      int x = this.getX();
      int y = this.getY();
      this.isHovered = pMouseX >= x && pMouseY >= y && pMouseX < x + this.width && pMouseY < y + this.height;
      if (this.rl != null) {
         RenderSystem.setShaderTexture(0, this.rl);
         RenderTools.blitWithBlend(gfx, (float)x, (float)y, 0.0F, 0.0F, (float)this.width, (float)this.height, (float)this.width, (float)this.height, 1.0F);
      }
   }

   public void onPress() {
      this.onClick.run();
   }

   protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
   }
}

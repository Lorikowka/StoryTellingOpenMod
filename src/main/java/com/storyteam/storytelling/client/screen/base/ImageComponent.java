package com.storyteam.storytelling.client.screen.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.util.RenderTools;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ImageComponent extends AbstractWidget {
   ResourceLocation loc;

   public ImageComponent(int pX, int pY, int pWidth, int pHeight, ResourceLocation icon) {
      super(pX, pY, pWidth, pHeight, Component.empty());
      this.loc = icon;
   }

   public void renderWidget(@NotNull GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
      RenderSystem.setShaderTexture(0, this.loc);
      RenderTools.blitWithBlend(
         pPoseStack, (float)this.getX(), (float)this.getY(), 0.0F, 0.0F, (float)this.width, (float)this.height, (float)this.width, (float)this.height, 1.0F
      );
   }

   protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
   }
}

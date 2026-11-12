package com.storyteam.storytelling.client.content.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.St;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FlashbackOverlay implements IGuiOverlay {
   public static boolean isEnabled = false;
   private static final ResourceLocation bgRL = St.id("textures/gui/overlay/flashback_screen.png");
   private static final float ALPHA = 1.0F;

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (isEnabled) {
         guiGraphics.pose().pushPose();
         RenderSystem.enableBlend();
         RenderSystem.setShaderTexture(0, bgRL);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         Minecraft.getInstance().getTextureManager().getTexture(bgRL).bind();
         guiGraphics.blit(bgRL, 0, 0, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);
         RenderSystem.disableBlend();
         guiGraphics.pose().popPose();
      }
   }

   @Generated
   public static void setEnabled(boolean isEnabled) {
      FlashbackOverlay.isEnabled = isEnabled;
   }
}

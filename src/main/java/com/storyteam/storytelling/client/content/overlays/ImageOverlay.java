package com.storyteam.storytelling.client.content.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.joml.Matrix4f;

public class ImageOverlay implements IGuiOverlay {
   public static int NO_BG = -1;
   private static ImageOverlay.Image currentImage = null;
   private static ImageOverlay.Image nextImage = null;
   private static int ticker = 0;
   private static int actionTime = 0;

   public static void setImage(ResourceLocation tx, int txWidth, int txHeight) {
      setImage(tx, txWidth, txHeight, NO_BG, 100);
   }

   public static void setImage(ResourceLocation tx, int txWidth, int txHeight, int bgColor, int inTime) {
      currentImage = nextImage;
      ticker = inTime;
      actionTime = inTime;
      nextImage = new ImageOverlay.Image(tx, txWidth, txHeight, bgColor);
   }

   public static void clear() {
      clear(100);
   }

   public static void clear(int outTime) {
      currentImage = nextImage;
      ticker = outTime;
      actionTime = outTime;
      nextImage = null;
   }

   public static void reset() {
      currentImage = null;
      nextImage = null;
      ticker = 0;
      actionTime = 0;
   }

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      RenderSystem.enableBlend();
      int poses = 0;
      float alpha = getAlphaAndUpdateTicker();
      if (alpha == 0.0F && nextImage == null) {
         currentImage = null;
      }

      if (currentImage != null && currentImage.bgColor != NO_BG) {
         guiGraphics.pose().pushPose();
         poses++;
         drawColor(currentImage.bgColor, alpha);
      }

      if (nextImage != null && nextImage.bgColor != NO_BG) {
         guiGraphics.pose().pushPose();
         poses++;
         drawColor(nextImage.bgColor, 1.0F - alpha);
      }

      if (currentImage != null && currentImage.tx != null) {
         guiGraphics.pose().pushPose();
         poses++;
         drawImage(guiGraphics, currentImage.tx, currentImage.txWidth, currentImage.txHeight, alpha);
      }

      if (nextImage != null && nextImage.tx != null) {
         guiGraphics.pose().pushPose();
         poses++;
         drawImage(guiGraphics, nextImage.tx, nextImage.txWidth, nextImage.txHeight, 1.0F - alpha);
      }

      for (int i = 0; i < poses; i++) {
         guiGraphics.pose().popPose();
      }

      RenderSystem.disableBlend();
   }

   private static void drawColor(int bgColor, float alpha) {
      int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
      int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
      PoseStack matrix = new PoseStack();
      Matrix4f m = matrix.last().pose();
      BufferBuilder vertex = Tesselator.getInstance().getBuilder();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      float r = (float)(bgColor >> 16) / 255.0F;
      float g = (float)(bgColor >> 8 & 0xFF) / 255.0F;
      float b = (float)(bgColor & 0xFF) / 255.0F;
      vertex.vertex(m, 0.0F, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
      vertex.vertex(m, (float)width, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
      vertex.vertex(m, (float)width, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
      vertex.vertex(m, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
      BufferUploader.drawWithShader(vertex.end());
   }

   private static void drawImage(GuiGraphics guiGraphics, ResourceLocation tx, int txWidth, int txHeight, float alpha) {
      int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
      int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
      RenderSystem.setShaderTexture(0, tx);
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      Minecraft.getInstance().getTextureManager().getTexture(tx).bind();
      int drawWidth = txWidth;
      int drawHeight = txHeight;
      if (txWidth > width) {
         drawHeight = txHeight * width / txWidth;
         drawWidth = width;
      }

      if (drawHeight > height) {
         drawWidth = drawWidth * height / drawHeight;
         drawHeight = height;
      }

      guiGraphics.blit(tx, (width - drawWidth) / 2, (height - drawHeight) / 2, 0.0F, 0.0F, drawWidth, drawHeight, drawWidth, drawHeight);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private static float getAlphaAndUpdateTicker() {
      if (ticker <= 0) {
         return 0.0F;
      } else {
         float alpha = 1.0F - (float)(actionTime - ticker) / (float)actionTime;
         ticker--;
         return alpha;
      }
   }

   static record Image(ResourceLocation tx, int txWidth, int txHeight, int bgColor) {
   }
}

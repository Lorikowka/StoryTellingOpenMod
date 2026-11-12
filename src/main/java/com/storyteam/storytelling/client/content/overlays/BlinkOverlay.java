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
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.joml.Matrix4f;

public class BlinkOverlay implements IGuiOverlay {
   public static BlinkOverlay.Blink currentBlackout = null;
   public static int ticker;
   public static int ticker_close;
   public static int ticker_open;

   private static float getAlpha() {
      int t = currentBlackout.inTime + currentBlackout.time + currentBlackout.outTime - ticker;
      float tick = Mth.lerp(Minecraft.getInstance().getDeltaFrameTime(), (float)t, (float)(t + 1));
      if (tick < (float)currentBlackout.inTime && currentBlackout.inTime != 0) {
         return tick / (float)currentBlackout.inTime;
      } else {
         return t - currentBlackout.inTime < currentBlackout.time
            ? 1.0F
            : 1.0F - (tick - (float)currentBlackout.inTime - (float)currentBlackout.time) / (float)currentBlackout.outTime;
      }
   }

   public static void setCurrentBlackout(int close, int time, int open, int color) {
      currentBlackout = new BlinkOverlay.Blink(close, time, open, color);
      ticker = close + time + open;
      ticker_close = close;
      ticker_open = open;
   }

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (ticker == 0) {
         currentBlackout = null;
      }

      if (currentBlackout != null) {
         PoseStack matrix = new PoseStack();
         Matrix4f m = matrix.last().pose();
         BufferBuilder vertex = Tesselator.getInstance().getBuilder();
         RenderSystem.enableBlend();
         RenderSystem.setShader(GameRenderer::getPositionColorShader);
         vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
         float r = (float)(currentBlackout.color >> 16) / 255.0F;
         float g = (float)(currentBlackout.color >> 8 & 0xFF) / 255.0F;
         float b = (float)(currentBlackout.color & 0xFF) / 255.0F;
         int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
         int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
         if (ticker > currentBlackout.outTime) {
            int totalTime = currentBlackout.inTime;
            float progress;
            if (currentBlackout.inTime == 0) {
               progress = 1.0F;
            } else {
               progress = 1.0F - (float)ticker_close / (float)totalTime;
            }

            float alpha = Mth.clamp(progress, 0.01F, 1.0F);
            int startY = (int)((float)(height / 2) * progress);
            int endY = (int)((float)height - (float)(height / 2) * progress);
            vertex.vertex(m, 0.0F, (float)(startY + 5), 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, (float)width, (float)(startY + 5), 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, (float)width, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, 0.0F, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, (float)width, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, (float)width, (float)(endY - 5), 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, 0.0F, (float)(endY - 5), 0.0F).color(r, g, b, alpha).endVertex();
         } else {
            int totalTime = currentBlackout.outTime;
            float progress;
            if (currentBlackout.outTime == 0) {
               progress = 1.0F;
            } else {
               progress = 1.0F - (float)ticker_open / (float)totalTime;
            }

            float alpha = (float)ticker_open / (float)totalTime;
            int startY = height / 2 - (int)((float)(height / 2) * progress);
            int endY = height / 2 + (int)((float)(height / 2) * progress);
            vertex.vertex(m, 0.0F, (float)(startY + 5), 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, (float)width, (float)(startY + 5), 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, (float)width, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, 0.0F, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, (float)width, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, (float)width, (float)(endY - 5), 0.0F).color(r, g, b, alpha).endVertex();
            vertex.vertex(m, 0.0F, (float)(endY - 5), 0.0F).color(r, g, b, alpha).endVertex();
         }

         BufferUploader.drawWithShader(vertex.end());
      }
   }

   public static record Blink(int inTime, int time, int outTime, int color) {
   }
}

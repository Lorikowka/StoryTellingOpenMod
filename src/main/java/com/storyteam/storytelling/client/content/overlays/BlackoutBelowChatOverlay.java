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
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix4f;

public class BlackoutBelowChatOverlay implements IGuiOverlay {
   public static BlackoutBelowChatOverlay.Blackout currentBlackout = null;
   public static int ticker;

   @SubscribeEvent
   public static void renderTick(RenderTickEvent event) {
      if (ticker == 0) {
         currentBlackout = null;
      }

      if (currentBlackout != null) {
         float alpha = getAlpha();
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
         vertex.vertex(m, 0.0F, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
         vertex.vertex(m, (float)width, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
         vertex.vertex(m, (float)width, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
         vertex.vertex(m, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
         BufferUploader.drawWithShader(vertex.end());
      }
   }

   public static float getAlpha() {
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

   public static void setCurrentBlackout(int inTime, int time, int outTime, int color) {
      currentBlackout = new BlackoutBelowChatOverlay.Blackout(inTime, time, outTime, color);
      ticker = inTime + time + outTime;
   }

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (ticker == 0) {
         currentBlackout = null;
      }

      if (currentBlackout != null) {
         float alpha = getAlpha();
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
         vertex.vertex(m, 0.0F, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
         vertex.vertex(m, (float)width, (float)height, 0.0F).color(r, g, b, alpha).endVertex();
         vertex.vertex(m, (float)width, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
         vertex.vertex(m, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
         BufferUploader.drawWithShader(vertex.end());
      }
   }

   public static record Blackout(int inTime, int time, int outTime, int color) {
   }
}

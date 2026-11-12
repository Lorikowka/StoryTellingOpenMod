package com.storyteam.storytelling.system.simulator.cognition_tab.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class DrawUtils {
   private static final Font font;
   private static ShaderInstance LINE_SHADER = null;

   public static void blit(PoseStack poseStack, ResourceLocation loc, float x, float y, float width, float height) {
      RenderSystem.enableBlend();
      Matrix4f pMatrix = poseStack.last().pose();
      RenderSystem.setShaderTexture(0, loc);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      float corrU = 1.0F / width;
      float corrV = 1.0F / height;
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
      bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      bufferbuilder.vertex(pMatrix, x, y + height, 0.0F).uv(0.0F, height * corrV).endVertex();
      bufferbuilder.vertex(pMatrix, x + width, y + height, 0.0F).uv(width * corrU, height * corrV).endVertex();
      bufferbuilder.vertex(pMatrix, x + width, y, 0.0F).uv(width * corrU, 0.0F).endVertex();
      bufferbuilder.vertex(pMatrix, x, y, 0.0F).uv(0.0F, 0.0F).endVertex();
      BufferUploader.drawWithShader(bufferbuilder.end());
      RenderSystem.disableBlend();
   }

   public static void blit(PoseStack poseStack, ResourceLocation texture, float x, float y, float width, float height, float textureX, float textureY) {
      innerBlit(
         poseStack,
         texture,
         x,
         x + width,
         y,
         y + height,
         (textureX + 0.0F) / 256.0F,
         (textureX + width) / 256.0F,
         (textureY + 0.0F) / 256.0F,
         (textureY + height) / 256.0F
      );
   }

   private static void innerBlit(
      PoseStack poseStack, ResourceLocation texture, float x1, float x2, float y1, float y2, float minU, float maxU, float minV, float maxV
   ) {
      RenderSystem.setShaderTexture(0, texture);
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      Matrix4f matrix4f = poseStack.last().pose();
      BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
      bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      bufferbuilder.vertex(matrix4f, x1, y1, 0.0F).uv(minU, minV).endVertex();
      bufferbuilder.vertex(matrix4f, x1, y2, 0.0F).uv(minU, maxV).endVertex();
      bufferbuilder.vertex(matrix4f, x2, y2, 0.0F).uv(maxU, maxV).endVertex();
      bufferbuilder.vertex(matrix4f, x2, y1, 0.0F).uv(maxU, minV).endVertex();
      BufferUploader.drawWithShader(bufferbuilder.end());
   }

   public static void blit(
      PoseStack poseStack,
      ResourceLocation texture,
      float x,
      float y,
      float width,
      float height,
      float textureX,
      float textureY,
      float textureWidth,
      float textureHeight
   ) {
      innerBlit(poseStack, texture, x, y, width, height, textureX, textureY, width, height, textureWidth, textureHeight);
   }

   public static void innerBlit(
      PoseStack poseStack,
      ResourceLocation texture,
      float x,
      float y,
      float width,
      float height,
      float textureX,
      float textureY,
      float spriteWidth,
      float spriteHeight,
      float textureWidth,
      float textureHeight
   ) {
      float x2 = x + width;
      float y2 = y + height;
      float u1 = textureX / textureWidth;
      float u2 = (textureX + spriteWidth) / textureWidth;
      float v1 = textureY / textureHeight;
      float v2 = (textureY + spriteHeight) / textureHeight;
      Matrix4f matrix4f = poseStack.last().pose();
      RenderSystem.enableBlend();
      RenderSystem.setShaderTexture(0, texture);
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
      bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      bufferbuilder.vertex(matrix4f, x, y, 0.0F).uv(u1, v1).endVertex();
      bufferbuilder.vertex(matrix4f, x, y2, 0.0F).uv(u1, v2).endVertex();
      bufferbuilder.vertex(matrix4f, x2, y2, 0.0F).uv(u2, v2).endVertex();
      bufferbuilder.vertex(matrix4f, x2, y, 0.0F).uv(u2, v1).endVertex();
      BufferUploader.drawWithShader(bufferbuilder.end());
   }

   public static void drawString(PoseStack poseStack, String text, float x, float y, float scale, int color, boolean shadow) {
      poseStack.pushPose();
      poseStack.scale(scale, scale, 1.0F);
      font.drawInBatch(
         text,
         x / scale,
         y / scale,
         color,
         shadow,
         poseStack.last().pose(),
         Minecraft.getInstance().renderBuffers().bufferSource(),
         DisplayMode.NORMAL,
         0,
         15728880,
         font.isBidirectional()
      );
      poseStack.popPose();
   }

   public static void fill(PoseStack pPoseStack, float x, float y, float width, float height, Color pColor) {
      innerFill(pPoseStack.last().pose(), x, y, x + width, y + height, pColor);
   }

   private static void innerFill(Matrix4f pMatrix, float x1, float y1, float x2, float y2, Color pColor) {
      if (x1 < x2) {
         float i = x1;
         x1 = x2;
         x2 = i;
      }

      if (y1 < y2) {
         float j = y1;
         y1 = y2;
         y2 = j;
      }

      int f = pColor.getRed();
      int f1 = pColor.getGreen();
      int f2 = pColor.getBlue();
      int f3 = pColor.getAlpha();
      BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      bufferbuilder.vertex(pMatrix, x1, y2, 0.0F).color(f, f1, f2, f3).endVertex();
      bufferbuilder.vertex(pMatrix, x2, y2, 0.0F).color(f, f1, f2, f3).endVertex();
      bufferbuilder.vertex(pMatrix, x2, y1, 0.0F).color(f, f1, f2, f3).endVertex();
      bufferbuilder.vertex(pMatrix, x1, y1, 0.0F).color(f, f1, f2, f3).endVertex();
      BufferUploader.drawWithShader(bufferbuilder.end());
      RenderSystem.disableBlend();
   }

   public static void drawLine(PoseStack poseStack, float x1, float y1, float x2, float y2, float thickness, Color color) {
      drawLine(poseStack, x1, y1, x2, y2, thickness, 0.2F, color);
   }

   public static void drawLine(PoseStack poseStack, float x1, float y1, float x2, float y2, float thickness, float smoothness, Color color) {
      if (LINE_SHADER == null) {
         try {
            LINE_SHADER = new ShaderInstance(Minecraft.getInstance().getResourceManager(), St.id("line"), DefaultVertexFormat.POSITION_COLOR_TEX);
         } catch (IOException var20) {
            Storytelling.LOG.warn(var20);
         }
      }

      if (LINE_SHADER != null) {
         float[] c = getColorComps(color);
         float half = thickness * 0.5F + 2.0F;
         float minX = Math.min(x1, x2) - half;
         float minY = Math.min(y1, y2) - half;
         float maxX = Math.max(x1, x2) + half;
         float maxY = Math.max(y1, y2) + half;
         float width = maxX - minX;
         float height = maxY - minY;
         float localX1 = x1 - minX;
         float localY1 = y1 - minY;
         float localX2 = x2 - minX;
         float localY2 = y2 - minY;
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.setShader(() -> LINE_SHADER);
         LINE_SHADER.getUniform("size").set(width, height);
         LINE_SHADER.getUniform("pos1").set(localX1, localY1);
         LINE_SHADER.getUniform("pos2").set(localX2, localY2);
         LINE_SHADER.getUniform("color").set(c[0], c[1], c[2], c[3]);
         LINE_SHADER.getUniform("thickness").set(thickness);
         LINE_SHADER.getUniform("smoothness").set(smoothness);
         drawQuad(poseStack, minX, minY, width, height);
         RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
         RenderSystem.disableBlend();
      }
   }

   public static float[] getColorComps(Color color) {
      return new float[]{(float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F};
   }

   public static void drawQuad(PoseStack poseStack, float x, float y, float width, float height) {
      Matrix4f matrix = poseStack.last().pose();
      BufferBuilder buffer = Tesselator.getInstance().getBuilder();
      buffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
      buffer.vertex(matrix, x, y + height, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 1.0F).endVertex();
      buffer.vertex(matrix, x + width, y + height, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 1.0F).endVertex();
      buffer.vertex(matrix, x + width, y, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0.0F).endVertex();
      buffer.vertex(matrix, x, y, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 0.0F).endVertex();
      BufferUploader.drawWithShader(buffer.end());
   }

   public static DrawUtils.LineInfo getLineInfo(Cognition from, Cognition to, float width, float height, float offset, float padding) {
      Cognition.Pos fromPos = from.pos();
      Cognition.Pos toPos = to.pos();
      float fromX = fromPos.x() + 1.0F;
      float fromY = fromPos.y() + 1.0F;
      float toX = toPos.x() + 1.0F;
      float toY = toPos.y() + 1.0F;
      float fromCenterX = fromX + width / 2.0F;
      float fromCenterY = fromY + height / 2.0F;
      float toCenterX = toX + width / 2.0F;
      float toCenterY = toY + height / 2.0F;
      float dx = toCenterX - fromCenterX;
      float dy = toCenterY - fromCenterY;
      boolean isHorizontal = (double)Math.abs(dx) / ((double)width / 2.0) > (double)Math.abs(dy) / ((double)height / 2.0);
      float startX = fromX + (isHorizontal ? (dx >= 0.0F ? width - padding : padding) : width / 2.0F);
      float startY = fromY + (isHorizontal ? height / 2.0F : (dy >= 0.0F ? height - padding : padding));
      float endX = toX + (isHorizontal ? (dx >= 0.0F ? padding : width - padding) : width / 2.0F);
      float endY = toY + (isHorizontal ? height / 2.0F : (dy >= 0.0F ? padding : height - padding));
      float vx = endX - startX;
      float vy = endY - startY;
      float length = (float)Math.sqrt((double)(vx * vx + vy * vy));
      if (length == 0.0F) {
         return new DrawUtils.LineInfo(startX, startY, endX, endY, startX, startY, 0.0F);
      } else {
         float ux = vx / length;
         float uy = vy / length;
         float offsetPointX = startX + ux * offset;
         float offsetPointY = startY + uy * offset;
         float angle = (float)(Math.atan2((double)vy, (double)vx) + Math.PI);
         if ((double)angle > Math.PI * 2) {
            angle -= (float) (Math.PI * 2);
         } else if (angle < 0.0F) {
            angle += (float) (Math.PI * 2);
         }

         return new DrawUtils.LineInfo(startX, startY, endX, endY, offsetPointX, offsetPointY, angle);
      }
   }

   static {
      font = Minecraft.getInstance().font;
   }

   public static record LineInfo(float startX, float startY, float endX, float endY, float offsetX, float offsetY, float angle) {
   }
}

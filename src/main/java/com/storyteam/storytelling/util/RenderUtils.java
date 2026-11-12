package com.storyteam.storytelling.util;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.screen.base.Rectangle;
import com.storyteam.storytelling.util.data.AnimationData;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RenderUtils extends org.zeith.hammerlib.client.utils.RenderUtils {
   public static void renderTextureFromCenter(
      PoseStack matrix,
      float centerX,
      float centerY,
      float texWidth,
      float texHeight,
      float patternWidth,
      float patternHeight,
      float scale,
      AnimationData animation
   ) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         renderTextureFromCenter(matrix, centerX, centerY, texWidth, texHeight, patternWidth, patternHeight, scale, animation, level.getGameTime());
      }
   }

   public static boolean isMouseOver(int mx, int my, int x, int y, int w, int h) {
      return mx >= x && mx <= x + w && my >= y && my <= y + h;
   }

   public static void renderTextureFromCenter(
      PoseStack matrix,
      float centerX,
      float centerY,
      float texWidth,
      float texHeight,
      float patternWidth,
      float patternHeight,
      float scale,
      AnimationData animation,
      long ticks
   ) {
      int length = animation.getLength();
      int frames = animation.getFrames().size();
      long time = ticks % (long)length;
      int index = 0;
      int sum = 0;

      for (int i = 0; i < frames; i++) {
         Pair<Integer, Integer> pair = animation.getFrames().get(i);
         int modifier = (Integer)pair.getRight();
         if (time > (long)(sum + modifier)) {
            sum += modifier;
            index = index >= frames - 1 ? 0 : i + 1;
         }
      }

      Pair<Integer, Integer> pair = animation.getFrames().get(index);
      renderTextureFromCenter(
         matrix, centerX, centerY, 0.0F, patternHeight * (float)((Integer)pair.getKey()).intValue(), texWidth, texHeight, patternWidth, patternHeight, scale
      );
   }

   public static void renderTextureFromCenter(PoseStack matrix, float centerX, float centerY, float width, float height, float scale) {
      renderTextureFromCenter(matrix, centerX, centerY, 0.0F, 0.0F, width, height, width, height, scale);
   }

   public static void renderTextureFromCenter(
      PoseStack matrix, float centerX, float centerY, float texOffX, float texOffY, float texWidth, float texHeight, float width, float height, float scale
   ) {
      BufferBuilder builder = Tesselator.getInstance().getBuilder();
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      matrix.pushPose();
      matrix.translate(centerX, centerY, 0.0F);
      matrix.scale(scale, scale, scale);
      Matrix4f m = matrix.last().pose();
      float u1 = texOffX / texWidth;
      float u2 = (texOffX + width) / texWidth;
      float v1 = texOffY / texHeight;
      float v2 = (texOffY + height) / texHeight;
      float w2 = width / 2.0F;
      float h2 = height / 2.0F;
      builder.vertex(m, -w2, h2, 0.0F).uv(u1, v2).endVertex();
      builder.vertex(m, w2, h2, 0.0F).uv(u2, v2).endVertex();
      builder.vertex(m, w2, -h2, 0.0F).uv(u2, v1).endVertex();
      builder.vertex(m, -w2, -h2, 0.0F).uv(u1, v1).endVertex();
      matrix.popPose();
      BufferUploader.drawWithShader(builder.end());
   }

   public static void fillGradientHorizontal(GuiGraphics gfx, float pX1, float pY1, float pX2, float pY2, int pColorFrom, int pColorTo) {
      VertexConsumer pConsumer = gfx.bufferSource().getBuffer(RenderType.gui());
      float pZ = 0.0F;
      float f = (float)ARGB32.alpha(pColorFrom) / 255.0F;
      float f1 = (float)ARGB32.red(pColorFrom) / 255.0F;
      float f2 = (float)ARGB32.green(pColorFrom) / 255.0F;
      float f3 = (float)ARGB32.blue(pColorFrom) / 255.0F;
      float f4 = (float)ARGB32.alpha(pColorTo) / 255.0F;
      float f5 = (float)ARGB32.red(pColorTo) / 255.0F;
      float f6 = (float)ARGB32.green(pColorTo) / 255.0F;
      float f7 = (float)ARGB32.blue(pColorTo) / 255.0F;
      Matrix4f matrix4f = gfx.pose().last().pose();
      pConsumer.vertex(matrix4f, pX1, pY1, pZ).color(f1, f2, f3, f).endVertex();
      pConsumer.vertex(matrix4f, pX1, pY2, pZ).color(f1, f2, f3, f).endVertex();
      pConsumer.vertex(matrix4f, pX2, pY2, pZ).color(f5, f6, f7, f4).endVertex();
      pConsumer.vertex(matrix4f, pX2, pY1, pZ).color(f5, f6, f7, f4).endVertex();
      gfx.flush();
   }

   public static void renderBeams(PoseStack matrixStack, MultiBufferSource bufferIn, float partialTicks, int amount, float size, Color color) {
      matrixStack.pushPose();
      Random random = new Random(1488L);

      for (int i = 1; i < amount; i++) {
         matrixStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F + partialTicks));
         matrixStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F + partialTicks));
         matrixStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + partialTicks));
         renderBeam(matrixStack, bufferIn, partialTicks, size, color);
      }

      matrixStack.popPose();
   }

   public static void renderBeam(PoseStack matrixStack, MultiBufferSource bufferIn, float partialTicks, float size, Color color) {
      VertexConsumer builder = bufferIn.getBuffer(RenderType.lightning());
      Matrix4f matrix4f = matrixStack.last().pose();
      float length = size * 0.2F;
      int red = Mth.clamp(color.getRed(), 0, 255);
      int green = Mth.clamp(color.getGreen(), 0, 255);
      int blue = Mth.clamp(color.getBlue(), 0, 255);
      int alpha = (int)(255.0F * (1.0F - partialTicks / 200.0F));
      builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
      builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
      builder.vertex(matrix4f, -((float)(Math.sqrt(3.0) / 2.0)) * length, size, -0.5F * length).color(red, green, blue, 0).endVertex();
      builder.vertex(matrix4f, (float)(Math.sqrt(3.0) / 2.0) * length, size, -0.5F * length).color(red, green, blue, 0).endVertex();
      builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
      builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
      builder.vertex(matrix4f, (float)(Math.sqrt(3.0) / 2.0) * length, size, -0.5F * length).color(red, green, blue, 0).endVertex();
      builder.vertex(matrix4f, 0.0F, size, length).color(red, green, blue, 0).endVertex();
      builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
      builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
      builder.vertex(matrix4f, 0.0F, size, length).color(red, green, blue, 0).endVertex();
      builder.vertex(matrix4f, -((float)(Math.sqrt(3.0) / 2.0)) * length, size, -0.5F * length).color(red, green, blue, 0).endVertex();
   }

   public static void renderTexture(PoseStack poseStack, int x, int y, int width, int height, ResourceLocation resourceLocation) {
      renderTexture(poseStack, x, y, width, height, resourceLocation, 1.0F);
   }

   public static void renderTexture(PoseStack poseStack, int x, int y, int width, int height, ResourceLocation resourceLocation, float alpha) {
      poseStack.pushPose();
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      RenderSystem.setShaderTexture(0, resourceLocation);
      BufferBuilder buffer = Tesselator.getInstance().getBuilder();
      buffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      Matrix4f matrix = poseStack.last().pose();
      buffer.vertex(matrix, (float)x, (float)y, 100.0F).uv(0.0F, 0.0F).endVertex();
      buffer.vertex(matrix, (float)x, (float)(y + height), 100.0F).uv(0.0F, 1.0F).endVertex();
      buffer.vertex(matrix, (float)(x + width), (float)(y + height), 100.0F).uv(1.0F, 1.0F).endVertex();
      buffer.vertex(matrix, (float)(x + width), (float)y, 100.0F).uv(1.0F, 0.0F).endVertex();
      BufferUploader.drawWithShader(buffer.end());
      poseStack.popPose();
   }

   public static void drawString(GuiGraphics gfx, Font pFont, Component pText, int pX, int pY, int pColor) {
      gfx.drawString(pFont, pText.getVisualOrderText(), (float)pX, (float)pY, pColor, false);
   }

   public static void drawString(GuiGraphics gfx, Font pFont, Component pText, int pX, int pY, int pColor, float scale) {
      drawString(gfx, pFont, pText, pX, pY, pColor, scale, true);
   }

   public static void drawString(GuiGraphics gfx, Font pFont, Component pText, int pX, int pY, int pColor, float scale, boolean isRightAlign) {
      gfx.pose().pushPose();
      gfx.pose().scale(scale, scale, scale);
      FormattedCharSequence text = pText.getVisualOrderText();
      int textWidth = pFont.width(text);
      float scaledX = isRightAlign ? (float)pX / scale : ((float)pX - (float)textWidth * scale) / scale;
      float scaledY = (float)pY / scale;
      gfx.drawString(pFont, text, scaledX, scaledY, pColor, false);
      gfx.pose().popPose();
   }

   public static void drawString(GuiGraphics gfx, Font pFont, FormattedCharSequence pText, int pX, int pY, int pColor) {
      gfx.drawString(pFont, pText, (float)pX, (float)pY, pColor, false);
   }

   public static void drawCenteredString(GuiGraphics gfx, Font pFont, String pText, int pX, int pY, int pColor, boolean shadow) {
      gfx.drawString(pFont, pText, (float)(pX - pFont.width(pText) / 2), (float)pY, pColor, shadow);
   }

   public static void drawCenteredStringRGBA(GuiGraphics pPoseStack, Font pFont, String pText, int pX, int pY, float r, float g, float b, float a) {
      drawTextRGBA(pFont, pPoseStack, pText, pX - pFont.width(pText) / 2, pY, (int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
   }

   public static void drawCenteredStringRGBA(GuiGraphics pPoseStack, Font pFont, Component pText, int pX, int pY, float r, float g, float b, float a) {
      drawTextRGBA(pFont, pPoseStack, pText, pX - pFont.width(pText) / 2, pY, (int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
   }

   public static void drawCenteredString(GuiGraphics gfx, Font pFont, Component pText, int pX, int pY, int pColor, boolean shadow) {
      FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
      gfx.drawString(pFont, formattedcharsequence, (float)(pX - pFont.width(pText) / 2), (float)pY, pColor, shadow);
   }

   public static void drawCenteredString(GuiGraphics gfx, Font pFont, FormattedCharSequence pText, int pX, int pY, int pColor, boolean shadow) {
      gfx.drawString(pFont, pText, (float)(pX - pFont.width(pText) / 2), (float)pY, pColor, shadow);
   }

   public static void drawEntity(GuiGraphics gfx, LivingEntity entity, int x, int y, float mouseX, float mouseY, int rWidth, int rHeight) {
      if (entity != null) {
         PoseStack matrices = gfx.pose();
         Minecraft MC = Minecraft.getInstance();
         Rectangle mobIconRect = Rectangle.create(x, y, rWidth, rHeight);
         float mobHeight = entity.getBbHeight();
         float mobWidth = entity.getBbWidth();
         float eyeHeight = entity.getEyeHeight(entity.getPose());
         float size = Math.max(mobHeight, mobWidth) * entity.getScale();
         float scale = (float)mobIconRect.getWidth() * 1.5F / size;
         float entityX = (float)mobIconRect.getLeft() + (float)mobIconRect.getWidth() / 2.0F;
         float entityY = (float)mobIconRect.getBottom();
         Window window = MC.getWindow();
         double guiScale = window.getGuiScale();
         int scissorX = (int)((double)mobIconRect.getLeft() * guiScale);
         int scissorY = window.getHeight() - (int)((double)mobIconRect.getBottom() * guiScale);
         int scissorWidth = (int)((double)mobIconRect.getWidth() * guiScale);
         int scissorHeight = (int)((double)mobIconRect.getHeight() * guiScale);
         RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);
         InventoryScreen.renderEntityInInventory(
            gfx, Math.round(entityX), Math.round(entityY), Math.round(scale), matrices.last().pose().getUnnormalizedRotation(new Quaternionf()), null, entity
         );
         RenderSystem.disableScissor();
      }
   }

   public static void drawOutlinedText(GuiGraphics guiGraphics, Font font, String text, float x, float y, int textColor, int outlineColor) {
      drawOutlinedText(guiGraphics, font, text, x, y, textColor, outlineColor, 1.0F);
   }

   public static void drawOutlinedText(GuiGraphics guiGraphics, Font font, String text, float x, float y, int textColor, int outlineColor, float scale) {
      guiGraphics.pose().pushPose();
      guiGraphics.pose().scale(scale, scale, scale);
      float scaledX = x / scale;
      float scaledY = y / scale;
      guiGraphics.drawString(font, text, scaledX - 1.0F, scaledY, outlineColor, false);
      guiGraphics.drawString(font, text, scaledX + 1.0F, scaledY, outlineColor, false);
      guiGraphics.drawString(font, text, scaledX, scaledY - 1.0F, outlineColor, false);
      guiGraphics.drawString(font, text, scaledX, scaledY + 1.0F, outlineColor, false);
      guiGraphics.drawString(font, text, scaledX, scaledY, textColor, false);
      guiGraphics.pose().popPose();
   }
}

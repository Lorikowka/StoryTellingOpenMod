package com.storyteam.storytelling.util;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;
import com.storyteam.storytelling.events.ClientModEvents;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderTools {
   private static final Stack<float[]> shaderColors = new Stack<>();
   public static final RenderTools.VertexConstructor POSITION_COLOR = (vertex, matrix, vx, vy, vz, r, g, b, a, u, v, light) -> vertex.vertex(matrix, vx, vy, vz)
         .color(r, g, b, a)
         .endVertex();
   public static final RenderTools.VertexConstructor POSITION_COLOR_UV = (vertex, matrix, vx, vy, vz, r, g, b, a, u, v, light) -> vertex.vertex(
            matrix, vx, vy, vz
         )
         .color(r, g, b, a)
         .uv(u, v)
         .endVertex();
   public static final RenderTools.VertexConstructor POSITION_COLOR_UV_LIGHTMAP = (vertex, matrix, vx, vy, vz, r, g, b, a, u, v, light) -> vertex.vertex(
            matrix, vx, vy, vz
         )
         .color(r, g, b, a)
         .uv(u, v)
         .uv2(light)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .endVertex();

   public static void renderGradient(
      GuiGraphics graphics,
      float x,
      float y,
      float w,
      float h,
      float r1,
      float g1,
      float b1,
      float a1,
      float r2,
      float g2,
      float b2,
      float a2,
      float r3,
      float g3,
      float b3,
      float a3,
      float r4,
      float g4,
      float b4,
      float a4
   ) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getRendertypeLightningShader);
      BufferBuilder vertex = Tesselator.getInstance().getBuilder();
      Matrix4f m = graphics.pose().last().pose();
      vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      vertex.vertex(m, x, y, 0.0F).color(r1, g1, b1, a1).endVertex();
      vertex.vertex(m, x + w, y, 0.0F).color(r2, g2, b2, a2).endVertex();
      vertex.vertex((double)(x + w), (double)(y + h), 0.0).color(r3, g3, b3, a3).endVertex();
      vertex.vertex(m, x, y + h, 0.0F).color(r4, g4, b4, a4).endVertex();
      vertex.vertex(m, x, y + h, 0.0F).color(r4, g4, b4, a4).endVertex();
      vertex.vertex((double)(x + w), (double)(y + h), 0.0).color(r3, g3, b3, a3).endVertex();
      vertex.vertex(m, x + w, y, 0.0F).color(r2, g2, b2, a2).endVertex();
      vertex.vertex(m, x, y, 0.0F).color(r1, g1, b1, a1).endVertex();
      BufferUploader.drawWithShader(vertex.end());
      RenderSystem.disableBlend();
   }

   public static void blitWithBlend(
      GuiGraphics matrices, float x, float y, float texPosX, float texPosY, float width, float height, float texWidth, float texHeight, float alpha
   ) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
      BufferBuilder vertex = Tesselator.getInstance().getBuilder();
      float u1 = texPosX / texWidth;
      float u2 = (texPosX + width) / texWidth;
      float v1 = texPosY / texHeight;
      float v2 = (texPosY + height) / texHeight;
      Matrix4f m = matrices.pose().last().pose();
      if (vertex.building()) {
         vertex.endOrDiscardIfEmpty();
      }

      vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
      vertex.vertex(m, x, y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(u1, v1).endVertex();
      vertex.vertex(m, x, y + height, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(u1, v2).endVertex();
      vertex.vertex(m, x + width, y + height, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(u2, v2).endVertex();
      vertex.vertex(m, x + width, y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(u2, v1).endVertex();
      BufferUploader.drawWithShader(vertex.end());
      RenderSystem.disableBlend();
   }

   public static void blitWithBlend(
      GuiGraphics matrices,
      float x,
      float y,
      float width,
      float height,
      float texPosX,
      float texPosY,
      float renderAmountX,
      float renderAmountY,
      float texWidth,
      float texHeight,
      float alpha
   ) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(() -> ClientModEvents.POSITION_COLOR_TEX_NO_DISCARD);
      BufferBuilder vertex = Tesselator.getInstance().getBuilder();
      float u1 = texPosX / texWidth;
      float u2 = (texPosX + renderAmountX) / texWidth;
      float v1 = texPosY / texHeight;
      float v2 = (texPosY + renderAmountY) / texHeight;
      Matrix4f m = matrices.pose().last().pose();
      if (vertex.building()) {
         vertex.endOrDiscardIfEmpty();
      }

      vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
      vertex.vertex(m, x, y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(u1, v1).endVertex();
      vertex.vertex(m, x, y + height, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(u1, v2).endVertex();
      vertex.vertex(m, x + width, y + height, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(u2, v2).endVertex();
      vertex.vertex(m, x + width, y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(u2, v1).endVertex();
      BufferUploader.drawWithShader(vertex.end());
      RenderSystem.disableBlend();
   }

   public static void blitWithBlend(
      GuiGraphics matrices,
      float x,
      float y,
      float texPosX,
      float texPosY,
      float width,
      float height,
      float texWidth,
      float texHeight,
      float zOffset,
      float alpha
   ) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
      BufferBuilder vertex = Tesselator.getInstance().getBuilder();
      float u1 = texPosX / texWidth;
      float u2 = (texPosX + width) / texWidth;
      float v1 = texPosY / texHeight;
      float v2 = (texPosY + height) / texHeight;
      Matrix4f m = matrices.pose().last().pose();
      if (vertex.building()) {
         vertex.endOrDiscardIfEmpty();
      }

      vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
      vertex.vertex(m, x, y, zOffset).color(1.0F, 1.0F, 1.0F, alpha).uv(u1, v1).endVertex();
      vertex.vertex(m, x, y + height, zOffset).color(1.0F, 1.0F, 1.0F, alpha).uv(u1, v2).endVertex();
      vertex.vertex(m, x + width, y + height, zOffset).color(1.0F, 1.0F, 1.0F, alpha).uv(u2, v2).endVertex();
      vertex.vertex(m, x + width, y, zOffset).color(1.0F, 1.0F, 1.0F, alpha).uv(u2, v1).endVertex();
      BufferUploader.drawWithShader(vertex.end());
      RenderSystem.disableBlend();
   }

   public static void blitWithBlend(
      GuiGraphics matrices,
      float x,
      float y,
      float texPosX,
      float texPosY,
      float width,
      float height,
      float texWidth,
      float texHeight,
      float zOffset,
      float r,
      float g,
      float b,
      float alpha
   ) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
      BufferBuilder vertex = Tesselator.getInstance().getBuilder();
      float u1 = texPosX / texWidth;
      float u2 = (texPosX + width) / texWidth;
      float v1 = texPosY / texHeight;
      float v2 = (texPosY + height) / texHeight;
      Matrix4f m = matrices.pose().last().pose();
      if (vertex.building()) {
         vertex.endOrDiscardIfEmpty();
      }

      vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
      vertex.vertex(m, x, y, zOffset).color(r, g, b, alpha).uv(u1, v1).endVertex();
      vertex.vertex(m, x, y + height, zOffset).color(r, g, b, alpha).uv(u1, v2).endVertex();
      vertex.vertex(m, x + width, y + height, zOffset).color(r, g, b, alpha).uv(u2, v2).endVertex();
      vertex.vertex(m, x + width, y, zOffset).color(r, g, b, alpha).uv(u2, v1).endVertex();
      BufferUploader.drawWithShader(vertex.end());
      RenderSystem.disableBlend();
   }

   public static void bindText(ResourceLocation loc) {
      RenderSystem.setShaderTexture(0, loc);
   }

   public static boolean isMouseInBorders(int mx, int my, int x1, int y1, int x2, int y2) {
      return mx >= x1 && mx <= x2 && my >= y1 && my <= y2;
   }

   public static boolean isMouseInBorders(double mx, double my, int x1, int y1, int x2, int y2) {
      return mx >= (double)x1 && mx <= (double)x2 && my >= (double)y1 && my <= (double)y2;
   }

   public static void renderScaledText(GuiGraphics gfx, float x, float y, float scale, FormattedCharSequence component, int color) {
      Font font = Minecraft.getInstance().font;
      PoseStack pose = gfx.pose();
      pose.pushPose();
      pose.scale(scale, scale, 0.0F);
      gfx.drawString(font, component, x / scale, y / scale, color, false);
      pose.popPose();
   }

   public static void renderScaledText(GuiGraphics gfx, float x, float y, float scale, Component component, int color) {
      Font font = Minecraft.getInstance().font;
      PoseStack pose = gfx.pose();
      pose.pushPose();
      pose.scale(scale, scale, 0.0F);
      gfx.drawString(font, component.getVisualOrderText(), x / scale, y / scale, color, false);
      pose.popPose();
   }

   public static void renderScaledTextCentered(GuiGraphics gfx, float x, float y, float scale, FormattedCharSequence component, int color) {
      Font font = Minecraft.getInstance().font;
      PoseStack pose = gfx.pose();
      pose.pushPose();
      pose.scale(scale, scale, 0.0F);
      float w = (float)font.width(component) / scale;
      gfx.drawString(font, component, x / scale - w / 2.0F, y / scale, color, false);
      pose.popPose();
   }

   public static void renderScaledTextCentered(GuiGraphics gfx, float x, float y, float scale, Component component, int color) {
      Font font = Minecraft.getInstance().font;
      PoseStack pose = gfx.pose();
      pose.pushPose();
      pose.scale(scale, scale, 0.0F);
      float w = (float)font.width(component) / scale;
      gfx.drawString(font, component.getVisualOrderText(), x / scale - w / 2.0F, y / scale, color, false);
      pose.popPose();
   }

   public static void renderScaledTextCenteredFixed(GuiGraphics gfx, float x, float y, float scale, FormattedCharSequence component, int color) {
      Font font = Minecraft.getInstance().font;
      PoseStack pose = gfx.pose();
      pose.pushPose();
      pose.scale(scale, scale, 0.0F);
      gfx.drawString(font, component, x / scale - (float)font.width(component) / 2.0F, y / scale, color, false);
      pose.popPose();
   }

   public static void renderScaledTextCenteredFixed(GuiGraphics gfx, float x, float y, float scale, Component component, int color) {
      Font font = Minecraft.getInstance().font;
      PoseStack pose = gfx.pose();
      pose.pushPose();
      pose.scale(scale, scale, 0.0F);
      gfx.drawString(font, component.getVisualOrderText(), x / scale - (float)font.width(component) / 2.0F, y / scale, color, false);
      pose.popPose();
   }

   public static int renderScaledLines(GuiGraphics matrices, float x, float y, float scale, Component component, int color, int pixels) {
      int lineHeight = (int)Math.ceil(9.0F * scale);
      List<FormattedCharSequence> components = Minecraft.getInstance().font.split(component, Math.round((float)pixels / scale));
      int yi = 0;

      for (FormattedCharSequence seq : components) {
         renderScaledText(matrices, x, y + (float)yi, scale, seq, color);
         yi += lineHeight;
      }

      return yi;
   }

   public static int renderScaledLinesCentered(GuiGraphics matrices, float x, float y, float scale, Component component, int color, int pixels) {
      int lineHeight = (int)Math.ceil(9.0F * scale);
      List<FormattedCharSequence> components = Minecraft.getInstance().font.split(component, Math.round((float)pixels / scale));
      int yi = 0;

      for (FormattedCharSequence seq : components) {
         renderScaledTextCenteredFixed(matrices, x, y + (float)yi, scale, seq, color);
         yi += lineHeight;
      }

      return yi;
   }

   public static void renderScrollBar(
      GuiGraphics matrices,
      float x,
      float y,
      float scrollBarWidth,
      float scrollBarHeight,
      float scrollValue,
      float maxScrollValue,
      float r,
      float g,
      float b,
      float a,
      float scrollerR,
      float scrollerG,
      float scrollerB,
      float scrollerA
   ) {
      fill(matrices, (double)x, (double)y, (double)(x + scrollBarWidth), (double)(y + scrollBarHeight), r, g, b, a);
      float factor = (maxScrollValue + scrollBarHeight) / scrollBarHeight;
      float scrollBarYSize = scrollBarHeight / factor;
      float scrollBarYPos = scrollValue / factor + y;
      fill(
         matrices,
         (double)x,
         (double)scrollBarYPos,
         (double)(x + scrollBarWidth),
         (double)(scrollBarYPos + scrollBarYSize),
         scrollerR,
         scrollerG,
         scrollerB,
         scrollerA
      );
   }

   public static void fill(GuiGraphics gfx, double x1, double y1, double x2, double y2, float r, float g, float b, float a) {
      if (x1 > x2) {
         double k = x1;
         x1 = x2;
         x2 = k;
      }

      if (y1 > y2) {
         double k = y1;
         y1 = y2;
         y2 = k;
      }

      PoseStack pose = gfx.pose();
      pose.pushPose();
      Matrix4f matrix4f = pose.last().pose();
      BufferBuilder builder = Tesselator.getInstance().getBuilder();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      builder.vertex(matrix4f, (float)x1, (float)y1, 0.0F).color(r, g, b, a).endVertex();
      builder.vertex(matrix4f, (float)x1, (float)y2, 0.0F).color(r, g, b, a).endVertex();
      builder.vertex(matrix4f, (float)x2, (float)y2, 0.0F).color(r, g, b, a).endVertex();
      builder.vertex(matrix4f, (float)x2, (float)y1, 0.0F).color(r, g, b, a).endVertex();
      BufferUploader.drawWithShader(builder.end());
      RenderSystem.disableBlend();
      pose.popPose();
   }

   public static void renderBorder(GuiGraphics matrices, int x, int y, int xw, int yw, int borderWidth, int color) {
      matrices.fill(x, y + borderWidth, x + borderWidth, y + yw - borderWidth, color);
      matrices.fill(x, y, x + xw, y + borderWidth, color);
      matrices.fill(x, y + yw - borderWidth, x + xw, y + yw, color);
      matrices.fill(x + xw - borderWidth, y + borderWidth, x + xw, y + yw - borderWidth, color);
   }

   public static float easeInOut(float p) {
      return (double)p <= 0.5 ? 2.0F * p * p : -2.0F * (p - 1.0F) * (p - 1.0F) + 1.0F;
   }

   public static void pushShaderColor(float r, float g, float b, float a) {
      float[] arr = Arrays.copyOf(RenderSystem.getShaderColor(), 4);
      shaderColors.add(arr);
      RenderSystem.setShaderColor(r, g, b, a);
   }

   public static void popShaderColor() {
      try {
         float[] arr = shaderColors.pop();
         RenderSystem.setShaderColor(arr[0], arr[1], arr[2], arr[3]);
      } catch (EmptyStackException var1) {
         throw new RuntimeException("Color stack was empty!");
      }
   }

   public static void applyMovementMatrixRotations(PoseStack matrices, Vec3 vec) {
      double angleY = Math.toDegrees(Math.atan2(vec.x, vec.z));
      double angleX = Math.toDegrees(Math.atan2(Math.sqrt(vec.x * vec.x + vec.z * vec.z), vec.y));
      matrices.mulPose(Axis.YP.rotationDegrees((float)angleY));
      matrices.mulPose(Axis.XP.rotationDegrees((float)angleX));
   }

   public static void drawItem(PoseStack poseStack, ItemStack stack, int pX, int pY, float r, float g, float b, float a) {
      Minecraft MC = Minecraft.getInstance();
      MC.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.setShaderColor(r, g, b, a);
      PoseStack posestack = RenderSystem.getModelViewStack();
      posestack.pushPose();
      posestack.translate((float)pX, (float)pY, 0.0F);
      posestack.translate(8.0, 8.0, 0.0);
      posestack.scale(1.0F, -1.0F, 1.0F);
      posestack.scale(16.0F, 16.0F, 16.0F);
      RenderSystem.applyModelViewMatrix();
      BufferSource multibuffersource$buffersource = MC.renderBuffers().bufferSource();
      BakedModel model = MC.getItemRenderer().getModel(stack, MC.level, MC.player, 0);
      boolean flag = !model.usesBlockLight();
      if (flag) {
         Lighting.setupForFlatItems();
      }

      MC.getItemRenderer().render(stack, ItemDisplayContext.GUI, false, poseStack, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, model);
      multibuffersource$buffersource.endBatch();
      RenderSystem.enableDepthTest();
      if (flag) {
         Lighting.setupFor3DItems();
      }

      posestack.popPose();
      RenderSystem.applyModelViewMatrix();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawItem(PoseStack poseStack, ItemStack stack, int pX, int pY) {
      drawItem(poseStack, stack, pX, pY, 1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void renderSphere(
      RenderTools.VertexConstructor v, VertexConsumer vertex, PoseStack matrices, int detalization, float radius, float r, float g, float b, float a, int light
   ) {
      matrices.pushPose();
      Matrix4f matrix4f = matrices.last().pose();

      for (int longs = 0; longs < detalization; longs++) {
         float pl1 = (float)longs / (float)detalization;
         float pl2 = (float)(longs + 1) / (float)detalization;
         float theta = pl1 * (float) java.lang.Math.PI;
         float thetaNext = pl2 * (float) java.lang.Math.PI;
         float tsin = Math.sin(theta);
         float tnsin = Math.sin(thetaNext);
         float tcos = Math.cos(theta);
         float tncos = Math.cos(thetaNext);

         for (int lats = 0; lats < detalization; lats++) {
            float pa1 = (float)lats / (float)detalization;
            float pa2 = (float)(lats + 1) / (float)detalization;
            float phi = pa1 * (float) java.lang.Math.PI * 2.0F;
            float phiNext = pa2 * (float) java.lang.Math.PI * 2.0F;
            float psin = Math.sin(phi);
            float pnsin = Math.sin(phiNext);
            float pcos = Math.cos(phi);
            float pncos = Math.cos(phiNext);
            Vector3f p1 = new Vector3f(radius * tsin * pcos, radius * tcos, radius * tsin * psin);
            Vector3f p2 = new Vector3f(radius * tnsin * pcos, radius * tncos, radius * tnsin * psin);
            Vector3f p3 = new Vector3f(radius * tsin * pncos, radius * tcos, radius * tsin * pnsin);
            Vector3f p4 = new Vector3f(radius * tnsin * pncos, radius * tncos, radius * tnsin * pnsin);
            v.process(vertex, matrix4f, p3.x, p3.y, p3.z, r, g, b, a, pa2, pl1, light);
            v.process(vertex, matrix4f, p4.x, p4.y, p4.z, r, g, b, a, pa2, pl2, light);
            v.process(vertex, matrix4f, p2.x, p2.y, p2.z, r, g, b, a, pa1, pl2, light);
            v.process(vertex, matrix4f, p1.x, p1.y, p1.z, r, g, b, a, pa1, pl1, light);
         }
      }

      matrices.popPose();
   }

   @FunctionalInterface
   public interface VertexConstructor {
      void process(
         VertexConsumer var1,
         Matrix4f var2,
         float var3,
         float var4,
         float var5,
         float var6,
         float var7,
         float var8,
         float var9,
         float var10,
         float var11,
         int var12
      );
   }
}

package com.storyteam.storytelling.system.simulator.me_tab.fragment.utils;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.system.simulator.me_tab.client.button.StretchButton;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RenderUtils {
   public static void drawStringWithOutline(GuiGraphics guiGraphics, Font font, String text, int x, int y, int mainColor, int outlineColor) {
      guiGraphics.drawString(font, text, x - 1, y, outlineColor, false);
      guiGraphics.drawString(font, text, x + 1, y, outlineColor, false);
      guiGraphics.drawString(font, text, x, y - 1, outlineColor, false);
      guiGraphics.drawString(font, text, x, y + 1, outlineColor, false);
      guiGraphics.drawString(font, text, x, y, mainColor, false);
   }

   public static void drawStringWithOutline(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int mainColor, int outlineColor) {
      guiGraphics.drawString(font, text, x - 1, y, outlineColor, false);
      guiGraphics.drawString(font, text, x + 1, y, outlineColor, false);
      guiGraphics.drawString(font, text, x, y - 1, outlineColor, false);
      guiGraphics.drawString(font, text, x, y + 1, outlineColor, false);
      guiGraphics.drawString(font, text, x, y, mainColor, false);
   }

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

   public static void renderEntityInInventoryFollowsMouse(
      GuiGraphics pGuiGraphics, int pX, int pY, int pScale, float pMouseX, float pMouseY, AnimatedEntity pEntity
   ) {
      float f = (float)Math.atan((double)(pMouseX / 40.0F));
      float f1 = (float)Math.atan((double)(pMouseY / 40.0F));
      renderEntityInInventoryFollowsAngle(pGuiGraphics, pX, pY, pScale, f, f1, pEntity);
   }

   public static void renderEntityInInventoryFollowsAngle(
      GuiGraphics pGuiGraphics, int pX, int pY, int pScale, float angleXComponent, float angleYComponent, AnimatedEntity pEntity
   ) {
      Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
      Quaternionf quaternionf1 = new Quaternionf().rotateX(angleYComponent * 20.0F * (float) (Math.PI / 180.0));
      pEntity.yHeadRot = angleXComponent * 40.0F;
      pEntity.yHeadRotO = angleXComponent * 40.0F;
      pEntity.setXRot(angleYComponent * 10.0F);
      pEntity.xRotO = angleYComponent * 10.0F;
      renderEntityInInventory(pGuiGraphics, pX, pY, pScale, quaternionf, quaternionf1, pEntity);
   }

   public static void renderEntityInInventory(
      GuiGraphics pGuiGraphics, int pX, int pY, int pScale, Quaternionf pPose, @Nullable Quaternionf pCameraOrientation, LivingEntity pEntity
   ) {
      pGuiGraphics.pose().pushPose();
      pGuiGraphics.pose().translate((double)pX, (double)pY, 50.0);
      pGuiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling((float)pScale, (float)pScale, (float)(-pScale)));
      pGuiGraphics.pose().mulPose(pPose);
      Lighting.setupForEntityInInventory();
      EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
      if (pCameraOrientation != null) {
         pCameraOrientation.conjugate();
         entityrenderdispatcher.overrideCameraOrientation(pCameraOrientation);
      }

      entityrenderdispatcher.setRenderShadow(false);
      RenderSystem.runAsFancy(
         () -> entityrenderdispatcher.render(pEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, pGuiGraphics.pose(), pGuiGraphics.bufferSource(), 15728880)
      );
      pGuiGraphics.flush();
      entityrenderdispatcher.setRenderShadow(true);
      pGuiGraphics.pose().popPose();
      Lighting.setupFor3DItems();
   }

   public static void renderButtonDebug(GuiGraphics pGuiGraphics, StretchButton b, int centerX, int centerY, int radius) {
      float cx = (float)b.getX() + (float)b.getWidth() / 2.0F;
      float cy = (float)b.getY() + (float)b.getHeight() / 2.0F;
      drawLine(pGuiGraphics, centerX, centerY, (int)cx, (int)cy, -2013200640);
      drawButtonBorder(pGuiGraphics, b, -1996488960);
      drawDebugCircle(pGuiGraphics, (int)cx, (int)cy, b.getRadius(), -65536);
   }

   public static void drawButtonBorder(GuiGraphics pGuiGraphics, StretchButton b, int color) {
      int x = b.getX();
      int y = b.getY();
      int w = b.getWidth();
      int h = b.getHeight();
      pGuiGraphics.fill(x, y, x + w, y + 1, color);
      pGuiGraphics.fill(x, y + h, x + w, y + h + 1, color);
      pGuiGraphics.fill(x, y, x + 1, y + h, color);
      pGuiGraphics.fill(x + w, y, x + w + 1, y + h, color);
   }

   public static void drawLine(GuiGraphics gfx, int x1, int y1, int x2, int y2, int color) {
      int dx = Math.abs(x2 - x1);
      int dy = Math.abs(y2 - y1);
      int sx = x1 < x2 ? 1 : -1;
      int sy = y1 < y2 ? 1 : -1;
      int err = dx - dy;

      while (true) {
         gfx.fill(x1, y1, x1 + 1, y1 + 1, color);
         if (x1 == x2 && y1 == y2) {
            return;
         }

         int e2 = 2 * err;
         if (e2 > -dy) {
            err -= dy;
            x1 += sx;
         }

         if (e2 < dx) {
            err += dx;
            y1 += sy;
         }
      }
   }

   public static void drawDebugCircle(GuiGraphics gfx, int centerX, int centerY, int radius, int color) {
      int segments = 200;

      for (int i = 0; i < segments; i++) {
         double angle = (Math.PI * 2) * (double)i / (double)segments;
         int x = (int)((double)centerX + Math.cos(angle) * (double)radius);
         int y = (int)((double)centerY + Math.sin(angle) * (double)radius);
         gfx.fill(x, y, x + 1, y + 1, color);
      }
   }
}

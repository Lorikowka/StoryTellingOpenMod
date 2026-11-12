package com.storyteam.storytelling.system.octospizdel.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.awt.geom.Point2D;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class RenderUtils {
   public static Vec2 toScreenCoords(Matrix4f matrix, double x, double y) {
      Matrix4f inverse = new Matrix4f(matrix);
      inverse.invert();
      return toViewportCoords(inverse, x, y);
   }

   public static Vec2 toViewportCoords(Matrix4f matrix, double x, double y) {
      Vector4f vec = new Vector4f((float)x, (float)y, 0.0F, 1.0F);
      vec = matrix.transform(vec);
      return new Vec2(vec.x(), vec.y());
   }

   private boolean isPointInQuad(double x, double y, Point2D[] quad) {
      return this.pointInTriangle(x, y, quad[0], quad[1], quad[2]) || this.pointInTriangle(x, y, quad[0], quad[2], quad[3]);
   }

   private boolean pointInTriangle(double px, double py, Point2D a, Point2D b, Point2D c) {
      double area = 0.5 * (-b.getY() * c.getX() + a.getY() * (-b.getX() + c.getX()) + a.getX() * (b.getY() - c.getY()) + b.getX() * c.getY());
      double sign = area < 0.0 ? -1.0 : 1.0;
      double s = (a.getY() * c.getX() - a.getX() * c.getY() + (c.getY() - a.getY()) * px + (a.getX() - c.getX()) * py) * sign;
      double t = (a.getX() * b.getY() - a.getY() * b.getX() + (a.getY() - b.getY()) * px + (b.getX() - a.getX()) * py) * sign;
      return s >= 0.0 && t >= 0.0 && s + t <= 2.0 * area * sign;
   }

   public static void renderTextureFromCenter(PoseStack matrix, float centerX, float centerY, float width, float height, float scale, float zOffset) {
      renderTextureFromCenter(matrix, centerX, centerY, 0.0F, 0.0F, width, height, width, height, scale, zOffset);
   }

   public static void renderTextureFromCenter(
      PoseStack matrix,
      float centerX,
      float centerY,
      float texOffX,
      float texOffY,
      float texWidth,
      float texHeight,
      float width,
      float height,
      float scale,
      float zOffset
   ) {
      BufferBuilder builder = Tesselator.getInstance().getBuilder();
      builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
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
      builder.vertex(m, -w2, h2, zOffset).uv(u1, v2).endVertex();
      builder.vertex(m, w2, h2, zOffset).uv(u2, v2).endVertex();
      builder.vertex(m, w2, -h2, zOffset).uv(u2, v1).endVertex();
      builder.vertex(m, -w2, -h2, zOffset).uv(u1, v1).endVertex();
      matrix.popPose();
      BufferUploader.drawWithShader(builder.end());
   }

   public static void renderTilingTexture(
      PoseStack matrix,
      float x,
      float y,
      float texOffX,
      float texOffY,
      float texWidth,
      float texHeight,
      float width,
      float height,
      float zOffset,
      boolean tileHorizontally,
      boolean tileVertically
   ) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      int wrapS = tileHorizontally ? 10497 : 10496;
      int wrapT = tileVertically ? 10497 : 10496;
      RenderSystem.texParameter(3553, 10242, wrapS);
      RenderSystem.texParameter(3553, 10243, wrapT);
      float uStart = texOffX / texWidth;
      float vStart = texOffY / texHeight;
      float uRange = tileHorizontally ? width / texWidth : 1.0F;
      float vRange = tileVertically ? height / texHeight : 1.0F;
      float uEnd = uStart + uRange;
      float vEnd = vStart + vRange;
      BufferBuilder builder = Tesselator.getInstance().getBuilder();
      builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      matrix.pushPose();
      matrix.translate(x, y, 0.0F);
      Matrix4f m = matrix.last().pose();
      builder.vertex(m, 0.0F, height, zOffset).uv(uStart, vEnd).endVertex();
      builder.vertex(m, width, height, zOffset).uv(uEnd, vEnd).endVertex();
      builder.vertex(m, width, 0.0F, zOffset).uv(uEnd, vStart).endVertex();
      builder.vertex(m, 0.0F, 0.0F, zOffset).uv(uStart, vStart).endVertex();
      matrix.popPose();
      BufferUploader.drawWithShader(builder.end());
   }
}

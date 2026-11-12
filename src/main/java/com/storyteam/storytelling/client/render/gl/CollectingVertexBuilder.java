package com.storyteam.storytelling.client.render.gl;

import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class CollectingVertexBuilder extends DefaultedVertexConsumer {
   protected final List<CollectingVertexBuilder.Vertex> vertices = new ArrayList<>();
   private final List<CollectingVertexBuilder.Vertex> pool = new ArrayList<>();
   private CollectingVertexBuilder.Vertex currentVertex = this.makeVertex();

   @Nonnull
   public VertexConsumer vertex(double x, double y, double z) {
      this.currentVertex.order.add(CollectingVertexBuilder.Element.POSITION);
      this.currentVertex.position[0] = x;
      this.currentVertex.position[1] = y;
      this.currentVertex.position[2] = z;
      return this;
   }

   @Nonnull
   public VertexConsumer color(int red, int green, int blue, int alpha) {
      this.currentVertex.order.add(CollectingVertexBuilder.Element.COLOR);
      this.currentVertex.color[0] = red;
      this.currentVertex.color[1] = green;
      this.currentVertex.color[2] = blue;
      this.currentVertex.color[3] = alpha;
      return this;
   }

   @Nonnull
   public VertexConsumer uv(float u, float v) {
      this.currentVertex.order.add(CollectingVertexBuilder.Element.UV);
      this.currentVertex.uv[0] = u;
      this.currentVertex.uv[1] = v;
      return this;
   }

   @Nonnull
   public VertexConsumer overlayCoords(int u, int v) {
      this.currentVertex.order.add(CollectingVertexBuilder.Element.OVERLAY);
      this.currentVertex.overlay[0] = u;
      this.currentVertex.overlay[1] = v;
      return this;
   }

   @Nonnull
   public VertexConsumer uv2(int u, int v) {
      this.currentVertex.order.add(CollectingVertexBuilder.Element.UV2);
      this.currentVertex.uv2[0] = u;
      this.currentVertex.uv2[1] = v;
      return this;
   }

   @Nonnull
   public VertexConsumer normal(float x, float y, float z) {
      this.currentVertex.order.add(CollectingVertexBuilder.Element.NORMAL);
      this.currentVertex.normal[0] = x;
      this.currentVertex.normal[1] = y;
      this.currentVertex.normal[2] = z;
      return this;
   }

   public void endVertex() {
      this.vertices.add(this.currentVertex);
      this.currentVertex = this.makeVertex();
   }

   public void pipeAndClear(VertexConsumer out) {
      for (CollectingVertexBuilder.Vertex v : this.vertices) {
         v.pipe(out);
      }

      this.clear();
   }

   protected void clear() {
      this.pool.addAll(this.vertices);
      this.vertices.clear();
   }

   private CollectingVertexBuilder.Vertex makeVertex() {
      if (!this.pool.isEmpty()) {
         CollectingVertexBuilder.Vertex result = this.pool.remove(this.pool.size() - 1);
         result.order.clear();
         return result;
      } else {
         return new CollectingVertexBuilder.Vertex();
      }
   }

   protected static enum Element {
      POSITION,
      COLOR,
      UV,
      OVERLAY,
      UV2,
      NORMAL;
   }

   protected static class Vertex {
      private final double[] position = new double[3];
      private final int[] color = new int[4];
      private final float[] uv = new float[2];
      private final int[] overlay = new int[2];
      private final int[] uv2 = new int[2];
      private final float[] normal = new float[3];
      private final List<CollectingVertexBuilder.Element> order = new ArrayList<>();

      public void pipe(VertexConsumer out) {
         for (CollectingVertexBuilder.Element e : this.order) {
            switch (e) {
               case POSITION:
                  out.vertex(this.position[0], this.position[1], this.position[2]);
                  break;
               case COLOR:
                  out.color(this.color[0], this.color[1], this.color[2], this.color[3]);
                  break;
               case UV:
                  out.uv(this.uv[0], this.uv[1]);
                  break;
               case OVERLAY:
                  out.overlayCoords(this.overlay[0], this.overlay[1]);
                  break;
               case UV2:
                  out.uv2(this.uv2[0], this.uv2[1]);
                  break;
               case NORMAL:
                  out.normal(this.normal[0], this.normal[1], this.normal[2]);
            }
         }

         out.endVertex();
      }
   }
}

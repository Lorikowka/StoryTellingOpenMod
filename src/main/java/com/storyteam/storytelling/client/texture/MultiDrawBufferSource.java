package com.storyteam.storytelling.client.texture;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.zeith.hammeranims.core.client.render.vertex.AccumulatingVertexConsumer.IntoSource;

public class MultiDrawBufferSource implements MultiBufferSource {
   private final Map<RenderType, IntoSource> storage = new HashMap<>();

   public IntoSource getBuffer(RenderType pRenderType) {
      return this.storage.computeIfAbsent(pRenderType, this::createBuffer);
   }

   private IntoSource createBuffer(RenderType renderType) {
      return new IntoSource(renderType) {
         public void unsetDefaultColor() {
            this.record(VertexConsumer::unsetDefaultColor);
         }
      };
   }

   public void pipe(MultiBufferSource target) {
      for (IntoSource value : this.storage.values()) {
         value.applyAndReset(target);
      }
   }
}

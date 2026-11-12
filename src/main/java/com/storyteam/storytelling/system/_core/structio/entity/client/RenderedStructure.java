package com.storyteam.storytelling.system._core.structio.entity.client;

import com.mojang.blaze3d.vertex.VertexBuffer;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.renderer.RenderType;

public class RenderedStructure implements AutoCloseable {
   private final Map<RenderType, VertexBuffer> buffers;

   @Override
   public void close() {
      for (VertexBuffer value : this.buffers.values()) {
         value.close();
      }
   }

   @Generated
   public RenderedStructure(Map<RenderType, VertexBuffer> buffers) {
      this.buffers = buffers;
   }

   @Generated
   public Map<RenderType, VertexBuffer> getBuffers() {
      return this.buffers;
   }
}

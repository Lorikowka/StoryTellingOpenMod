package com.storyteam.storytelling.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import javax.annotation.Nullable;

public class BufferBuilderUploader {
   @Nullable
   public static VertexBuffer upload(RenderedBuffer pBuffer) {
      RenderSystem.assertOnRenderThread();
      if (pBuffer.isEmpty()) {
         pBuffer.release();
         return null;
      } else {
         VertexBuffer vertexbuffer = bindImmediateBuffer(pBuffer.drawState().format());
         vertexbuffer.upload(pBuffer);
         return vertexbuffer;
      }
   }

   private static VertexBuffer bindImmediateBuffer(VertexFormat pFormat) {
      VertexBuffer vertexbuffer = pFormat.getImmediateDrawVertexBuffer();
      bindImmediateBuffer(vertexbuffer);
      return vertexbuffer;
   }

   private static void bindImmediateBuffer(VertexBuffer pBuffer) {
      pBuffer.bind();
   }
}

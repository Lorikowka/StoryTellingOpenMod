package com.storyteam.storytelling.system.shaders;

import java.util.ArrayList;
import java.util.List;
import net.irisshaders.iris.gl.buffer.ShaderStorageBuffer;
import net.irisshaders.iris.gl.buffer.ShaderStorageInfo;
import org.lwjgl.opengl.GL46;

public class GLBuffersST {
   public static List<ShaderStorageBuffer> buffers = List.of();
   public static ShaderStorageBuffer lightSourcesBuffer;
   public static ShaderStorageBuffer lightMapBuffer;

   public static List<ShaderStorageBuffer> createBuffers() {
      buffers = new ArrayList<>();
      createBuffers(buffers);
      return buffers;
   }

   public static ShaderStorageBuffer createBuffer(int binding, int target, long size, int usage) {
      ShaderStorageBuffer buffer = new ShaderStorageBuffer(binding, new ShaderStorageInfo(1, false, 0.0F, 0.0F));
      GL46.glBindBuffer(37074, buffer.getId());
      GL46.glBufferData(target, size, usage);
      GL46.glBindBuffer(37074, 0);
      return buffer;
   }

   public static void createBuffers(List<ShaderStorageBuffer> buffers) {
      buffers.add(lightSourcesBuffer = createBuffer(2, 37074, 1000000L, 35040));
      buffers.add(lightMapBuffer = createBuffer(3, 37074, 10000000L, 35040));
   }
}

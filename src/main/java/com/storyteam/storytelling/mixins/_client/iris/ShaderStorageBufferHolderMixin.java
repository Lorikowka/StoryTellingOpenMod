package com.storyteam.storytelling.mixins._client.iris;

import com.storyteam.storytelling.system.shaders.GLBuffersST;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.List;
import net.irisshaders.iris.gl.buffer.ShaderStorageBuffer;
import net.irisshaders.iris.gl.buffer.ShaderStorageBufferHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ShaderStorageBufferHolder.class})
public class ShaderStorageBufferHolderMixin {
   @Shadow
   private ShaderStorageBuffer[] buffers;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void init(Int2ObjectArrayMap overrides, int width, int height, CallbackInfo ci) {
      ShaderStorageBuffer[] oldBuffers = this.buffers;
      List<ShaderStorageBuffer> stBuffers = GLBuffersST.createBuffers();
      this.buffers = new ShaderStorageBuffer[oldBuffers.length + stBuffers.size()];
      System.arraycopy(oldBuffers, 0, this.buffers, 0, oldBuffers.length);

      for (int i = 0; i < stBuffers.size(); i++) {
         this.buffers[oldBuffers.length + i] = stBuffers.get(i);
      }
   }
}

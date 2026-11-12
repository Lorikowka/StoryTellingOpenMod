package com.storyteam.storytelling.mixins._client.iris;

import com.storyteam.storytelling.system.shaders.ShaderTime;
import net.irisshaders.iris.uniforms.HardcodedCustomUniforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({HardcodedCustomUniforms.class})
public class HardcodedCustomUniformsMixin {
   @Inject(
      method = {"getWorldDayTime"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void getWorldDayTime(CallbackInfoReturnable<Integer> cir) {
      if (ShaderTime.overrideTime) {
         cir.setReturnValue(ShaderTime.shaderTime);
      }
   }
}

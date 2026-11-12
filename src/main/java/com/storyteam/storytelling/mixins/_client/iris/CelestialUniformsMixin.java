package com.storyteam.storytelling.mixins._client.iris;

import com.storyteam.storytelling.system.shaders.ShaderTime;
import net.irisshaders.iris.uniforms.CelestialUniforms;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({CelestialUniforms.class})
public class CelestialUniformsMixin {
   @Inject(
      method = {"getSkyAngle"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void getSkyAngle(CallbackInfoReturnable<Float> cir) {
      if (ShaderTime.overrideTime) {
         double d0 = Mth.frac((double)ShaderTime.shaderTime / 24000.0 - 0.25);
         double d1 = 0.5 - Math.cos(d0 * Math.PI) / 2.0;
         cir.setReturnValue((float)((d0 * 2.0 + d1) / 3.0));
      }
   }
}

package com.storyteam.storytelling.mixins._client.iris;

import com.storyteam.storytelling.system.shaders.ShaderTime;
import java.util.Objects;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.shaderpack.DimensionId;
import net.irisshaders.iris.uniforms.WorldTimeUniforms;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({WorldTimeUniforms.class})
public class WorldTimeUniformsMixin {
   @Inject(
      method = {"getWorldDayTime"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void getWorldDayTime(CallbackInfoReturnable<Integer> cir) {
      if (ShaderTime.overrideTime) {
         long timeOfDay = (long)ShaderTime.shaderTime;
         if (Iris.getCurrentDimension() != DimensionId.END && Iris.getCurrentDimension() != DimensionId.NETHER) {
            long dayTime = Objects.requireNonNull(Minecraft.getInstance().level).dimensionType().fixedTime().orElse(timeOfDay % 24000L);
            cir.setReturnValue((int)dayTime);
         } else {
            cir.setReturnValue((int)(timeOfDay % 24000L));
         }
      }
   }
}

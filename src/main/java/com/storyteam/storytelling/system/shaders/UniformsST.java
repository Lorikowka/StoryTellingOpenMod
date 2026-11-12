package com.storyteam.storytelling.system.shaders;

import com.storyteam.storytelling.init.FluidsST;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.material.FluidState;

public class UniformsST {
   public static int lightSourcesCount = 0;
   public static float stLightModifier = 1.0F;

   public static void addSTUniforms(UniformHolder uniforms) {
      uniforms.uniform1i(UniformUpdateFrequency.PER_FRAME, "lightSourcesCount", () -> lightSourcesCount);
      uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "stLightModifier", () -> stLightModifier);
      uniforms.uniform1i(UniformUpdateFrequency.PER_FRAME, "stWaterType", () -> {
         Camera mainCamera = Minecraft.getInstance().gameRenderer.getMainCamera();
         ClientLevel level = Minecraft.getInstance().level;
         if (level == null) {
            return 0;
         } else {
            FluidState state = level.getFluidState(mainCamera.getBlockPosition());
            return FluidsST.PINK_WATER.is(state.getType()) ? 1 : 0;
         }
      });
   }
}

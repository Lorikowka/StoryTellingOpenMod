package com.storyteam.storytelling.client.particle.controllers.custom_controller;

import com.storyteam.storytelling.client.particle.ParticleInfo;
import lombok.Generated;
import net.minecraft.world.phys.Vec3;

public class MovementFunctionWithParams<T> implements MovementFunction<T> {
   private final MovementFunction<T> function;
   private T params;

   public MovementFunctionWithParams(MovementFunction<T> function, T params) {
      this.function = function;
      this.params = params;
   }

   @Override
   public Vec3 apply(ParticleInfo info, T ignored) {
      return this.function.apply(info, this.params);
   }

   @Generated
   public T getParams() {
      return this.params;
   }

   @Generated
   public void setParams(T params) {
      this.params = params;
   }
}

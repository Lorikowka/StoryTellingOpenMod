package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import com.storyteam.storytelling.client.particle.controllers.custom_controller.MovementFunction;
import com.storyteam.storytelling.client.particle.controllers.custom_controller.MovementFunctionWithParams;
import com.storyteam.storytelling.client.particle.controllers.custom_controller.MovementFunctions;
import com.storyteam.storytelling.init.ParticleTypesST;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class CustomController<T> implements ControllerData, ControllerType<CustomController<T>> {
   private final String id;
   private final MovementFunctionWithParams<T> functionWithParams;

   public CustomController() {
      this.id = "default";
      this.functionWithParams = MovementFunctions.get(this.id);
   }

   public CustomController(String id) {
      this.id = id;
      this.functionWithParams = MovementFunctions.get(id);
   }

   public CustomController(String id, MovementFunction<T> function, T params) {
      this.id = id;
      this.functionWithParams = new MovementFunctionWithParams<>(function, params);
      if (id != null && !id.isEmpty()) {
         MovementFunctions.register(id, function, params);
      }
   }

   @Nonnull
   @Override
   public <X extends ControllerData> ControllerType<X> getType() {
      return (ControllerType<X>)ParticleTypesST.ParticleBehaviorRegistry.CUSTOM_CONTROLLER;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf pBuffer) {
      pBuffer.writeUtf(this.id);
   }

   @Override
   public String writeToString() {
      return "";
   }

   public CustomController<T> fromCommand(StringReader pReader) throws CommandSyntaxException {
      return new CustomController<>(this.id);
   }

   public CustomController<T> fromNetwork(FriendlyByteBuf pBuffer) {
      return new CustomController<>(pBuffer.readUtf());
   }

   @Nonnull
   public Vec3 handleMovement(ParticleInfo info, CustomController<T> data) {
      return data.functionWithParams.apply(info, null);
   }
}

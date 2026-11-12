package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import com.storyteam.storytelling.init.ParticleTypesST;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ZeroMovementController implements ControllerData, ControllerType<ZeroMovementController> {
   @NotNull
   @Override
   public ControllerType<ZeroMovementController> getType() {
      return ParticleTypesST.ParticleBehaviorRegistry.ZERO_MOVEMENT;
   }

   public ZeroMovementController fromCommand(StringReader pReader) {
      return this;
   }

   public ZeroMovementController fromNetwork(FriendlyByteBuf pBuffer) {
      return this;
   }

   @Nonnull
   public Vec3 handleMovement(ParticleInfo info, ZeroMovementController data) {
      return new Vec3(0.0, 0.0, 0.0);
   }

   @Override
   public String writeToString() {
      return "";
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf pBuffer) {
   }
}

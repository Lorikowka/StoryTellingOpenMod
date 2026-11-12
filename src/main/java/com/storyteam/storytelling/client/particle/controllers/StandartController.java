package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import com.storyteam.storytelling.init.ParticleTypesST;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class StandartController implements ControllerData, ControllerType<StandartController> {
   @NotNull
   @Override
   public ControllerType<StandartController> getType() {
      return ParticleTypesST.ParticleBehaviorRegistry.STANDARD_CONTROLLER;
   }

   public StandartController fromCommand(StringReader pReader) {
      return this;
   }

   public StandartController fromNetwork(FriendlyByteBuf pBuffer) {
      return this;
   }

   @Nonnull
   public Vec3 handleMovement(ParticleInfo info, StandartController data) {
      double mx = info.movement().x;
      double my = info.movement().y;
      double mz = info.movement().z;
      my -= 0.04 * (double)info.gravity();
      return new Vec3(mx, my, mz);
   }

   @Override
   public String writeToString() {
      return "";
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf pBuffer) {
   }
}

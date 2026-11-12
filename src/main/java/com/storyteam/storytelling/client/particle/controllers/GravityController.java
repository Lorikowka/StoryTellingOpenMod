package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import com.storyteam.storytelling.init.ParticleTypesST;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GravityController implements ControllerData, ControllerType<GravityController> {
   public static double gravity = 0.0;

   @NotNull
   @Override
   public ControllerType<GravityController> getType() {
      return ParticleTypesST.ParticleBehaviorRegistry.GRAVITY_CONTROLLER;
   }

   public GravityController fromCommand(StringReader pReader) {
      return this;
   }

   public GravityController fromNetwork(FriendlyByteBuf pBuffer) {
      return this;
   }

   @Nonnull
   public Vec3 handleMovement(ParticleInfo info, GravityController data) {
      double mx = info.movement().x;
      double my = info.movement().y;
      double mz = info.movement().z;
      my -= gravity;
      mx *= (double)info.friction();
      my *= (double)info.friction();
      mz *= (double)info.friction();
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

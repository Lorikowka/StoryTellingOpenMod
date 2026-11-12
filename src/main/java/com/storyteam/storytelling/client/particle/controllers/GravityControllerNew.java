package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import com.storyteam.storytelling.init.ParticleTypesST;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class GravityControllerNew implements ControllerData, ControllerType<GravityControllerNew> {
   public double gravity;

   public GravityControllerNew() {
      this.gravity = 0.0;
   }

   public GravityControllerNew(double gravity) {
      this.gravity = gravity;
   }

   @Nonnull
   @Override
   public <T extends ControllerData> ControllerType<T> getType() {
      return (ControllerType<T>)ParticleTypesST.ParticleBehaviorRegistry.GRAVITY_CONTROLLER_NEW;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf pBuffer) {
      pBuffer.writeDouble(this.gravity);
   }

   @Override
   public String writeToString() {
      return String.format("%f", this.gravity);
   }

   public GravityControllerNew fromCommand(StringReader pReader) throws CommandSyntaxException {
      this.gravity = pReader.readDouble();
      return this;
   }

   public GravityControllerNew fromNetwork(FriendlyByteBuf pBuffer) {
      this.gravity = pBuffer.readDouble();
      return this;
   }

   @Nonnull
   public Vec3 handleMovement(ParticleInfo info, GravityControllerNew data) {
      double mx = info.movement().x;
      double my = info.movement().y;
      double mz = info.movement().z;
      my -= this.gravity;
      mx *= (double)info.friction();
      my *= (double)info.friction();
      mz *= (double)info.friction();
      return new Vec3(mx, my, mz);
   }
}

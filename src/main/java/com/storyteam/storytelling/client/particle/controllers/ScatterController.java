package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import com.storyteam.storytelling.init.ParticleTypesST;
import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ScatterController implements ControllerData, ControllerType<ScatterController> {
   Vec3 a = Vec3.ZERO;
   static Random random = new Random();

   @Nonnull
   @Override
   public <T extends ControllerData> ControllerType<T> getType() {
      return (ControllerType<T>)ParticleTypesST.ParticleBehaviorRegistry.SCATTER_CONTROLLER;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf pBuffer) {
   }

   @Override
   public String writeToString() {
      return "";
   }

   @Nonnull
   public Vec3 handleMovement(ParticleInfo info, ScatterController data) {
      double length = info.movement().length();
      if ((info.age() + 1.0F + (float)random.nextInt(14)) % 10.0F == 0.0F) {
         this.a = new Vec3(random.nextDouble(-0.5, 0.5), random.nextDouble(-0.5, 0.5), random.nextDouble(-0.5, 0.5))
            .scale(length * random.nextDouble(0.2, 0.3));
      }

      return info.movement().add(this.a);
   }

   public ScatterController fromCommand(StringReader pReader) throws CommandSyntaxException {
      return new ScatterController();
   }

   public ScatterController fromNetwork(FriendlyByteBuf pBuffer) {
      return new ScatterController();
   }
}

package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AccelerationType implements ControllerType<AccelerationData> {
   @NotNull
   public Vec3 handleMovement(ParticleInfo info, AccelerationData data) {
      return info.movement().add(data.getAcs());
   }

   public AccelerationData fromCommand(StringReader pReader) throws CommandSyntaxException {
      double axisX = pReader.readDouble();
      pReader.expect(' ');
      double axisY = pReader.readDouble();
      pReader.expect(' ');
      double axisZ = pReader.readDouble();
      pReader.expect(' ');
      return new AccelerationData(axisX, axisY, axisZ);
   }

   public AccelerationData fromNetwork(FriendlyByteBuf pBuffer) {
      Vec3 axis = new Vec3(pBuffer.readDouble(), pBuffer.readDouble(), pBuffer.readDouble());
      return new AccelerationData(axis);
   }
}

package com.storyteam.storytelling.client.particle.controllers;

import com.storyteam.storytelling.init.ParticleTypesST;
import lombok.Generated;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AccelerationData implements ControllerData {
   private Vec3 acs;

   public AccelerationData(Vec3 acs) {
      this.acs = acs;
   }

   public AccelerationData(double xa, double ya, double za) {
      this.acs = new Vec3(xa, ya, za);
   }

   @NotNull
   @Override
   public <T extends ControllerData> ControllerType<T> getType() {
      return (ControllerType<T>)ParticleTypesST.ParticleBehaviorRegistry.ACCELERATION_CONTROLLER;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf pBuffer) {
      pBuffer.writeDouble(this.acs.x);
      pBuffer.writeDouble(this.acs.y);
      pBuffer.writeDouble(this.acs.z);
   }

   @Override
   public String writeToString() {
      return String.format("%f %f %f", this.acs.x, this.acs.y, this.acs.z);
   }

   @Generated
   public void setAcs(Vec3 acs) {
      this.acs = acs;
   }

   @Generated
   public Vec3 getAcs() {
      return this.acs;
   }
}

package com.storyteam.storytelling.client.particle.controllers;

import com.storyteam.storytelling.init.ParticleTypesST;
import javax.annotation.Nonnull;
import lombok.Generated;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RotatingData implements ControllerData {
   @Nullable
   private Vec3 staticMovement;
   private Vec3 axis = new Vec3(0.0, 1.0, 0.0);
   private Vec3 center = new Vec3(0.0, 0.0, 0.0);
   private double rotatingSpeed = 0.3;
   private double resizeSpeed = 1.0;

   public RotatingData(double centerX, double centerY, double centerZ, double axisX, double axisY, double axisZ, double rotatingSpeed, double resizeSpeed) {
      this.axis = new Vec3(axisX, axisY, axisZ);
      this.center = new Vec3(centerX, centerY, centerZ);
      this.rotatingSpeed = rotatingSpeed;
      this.resizeSpeed = resizeSpeed;
   }

   public RotatingData(Vec3 axis, Vec3 center, double rotatingSpeed, double resizeSpeed) {
      this.axis = axis;
      this.center = center;
      this.rotatingSpeed = rotatingSpeed;
      this.resizeSpeed = resizeSpeed;
   }

   @Nonnull
   @Override
   public ControllerType<RotatingData> getType() {
      return ParticleTypesST.ParticleBehaviorRegistry.ROTATING_CONTROLLER;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf pBuffer) {
      pBuffer.writeDouble(this.axis.x);
      pBuffer.writeDouble(this.axis.y);
      pBuffer.writeDouble(this.axis.z);
      pBuffer.writeDouble(this.center.x);
      pBuffer.writeDouble(this.center.y);
      pBuffer.writeDouble(this.center.z);
      pBuffer.writeDouble(this.rotatingSpeed);
      pBuffer.writeDouble(this.resizeSpeed);
   }

   @Override
   public String writeToString() {
      return String.format(
         "%f %f %f %f %f %f %f %f", this.axis.x, this.axis.y, this.axis.z, this.center.x, this.center.y, this.center.z, this.rotatingSpeed, this.resizeSpeed
      );
   }

   @Generated
   public static RotatingData.RotatingDataBuilder builder() {
      return new RotatingData.RotatingDataBuilder();
   }

   @Nullable
   @Generated
   public Vec3 getStaticMovement() {
      return this.staticMovement;
   }

   @Generated
   public Vec3 getAxis() {
      return this.axis;
   }

   @Generated
   public Vec3 getCenter() {
      return this.center;
   }

   @Generated
   public double getRotatingSpeed() {
      return this.rotatingSpeed;
   }

   @Generated
   public double getResizeSpeed() {
      return this.resizeSpeed;
   }

   @Generated
   public void setStaticMovement(@Nullable Vec3 staticMovement) {
      this.staticMovement = staticMovement;
   }

   @Generated
   public static class RotatingDataBuilder {
      @Generated
      private Vec3 axis;
      @Generated
      private Vec3 center;
      @Generated
      private double rotatingSpeed;
      @Generated
      private double resizeSpeed;

      @Generated
      RotatingDataBuilder() {
      }

      @Generated
      public RotatingData.RotatingDataBuilder axis(Vec3 axis) {
         this.axis = axis;
         return this;
      }

      @Generated
      public RotatingData.RotatingDataBuilder center(Vec3 center) {
         this.center = center;
         return this;
      }

      @Generated
      public RotatingData.RotatingDataBuilder rotatingSpeed(double rotatingSpeed) {
         this.rotatingSpeed = rotatingSpeed;
         return this;
      }

      @Generated
      public RotatingData.RotatingDataBuilder resizeSpeed(double resizeSpeed) {
         this.resizeSpeed = resizeSpeed;
         return this;
      }

      @Generated
      public RotatingData build() {
         return new RotatingData(this.axis, this.center, this.rotatingSpeed, this.resizeSpeed);
      }

      @Generated
      @Override
      public String toString() {
         return "RotatingData.RotatingDataBuilder(axis="
            + this.axis
            + ", center="
            + this.center
            + ", rotatingSpeed="
            + this.rotatingSpeed
            + ", resizeSpeed="
            + this.resizeSpeed
            + ")";
      }
   }
}

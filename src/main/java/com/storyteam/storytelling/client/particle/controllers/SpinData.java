package com.storyteam.storytelling.client.particle.controllers;

import com.storyteam.storytelling.init.ParticleTypesST;
import javax.annotation.Nonnull;
import lombok.Generated;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SpinData implements ControllerData {
   private Vec3 staticMovement = Vec3.ZERO;
   private Vec3 axis = new Vec3(0.0, 1.0, 0.0);
   private Vec3 center = new Vec3(0.0, 0.0, 0.0);
   private double rotatingSpeed = 0.3;
   private double resizeSpeed = 1.0;
   private double initialRadius = 1.0;
   private double midRadius = 1.0;
   private double finalRadius = 1.0;
   private double radiusMidpoint = 0.5;
   private boolean expandAndContract = false;
   private boolean enableDeviation = false;
   private double deviationAmplitude = 0.0;
   private Vec3 phaseOffset = Vec3.ZERO;
   private Vec3 lastTargetPos = null;

   private SpinData(double centerX, double centerY, double centerZ, double axisX, double axisY, double axisZ, double rotatingSpeed, double resizeSpeed) {
      this.axis = new Vec3(axisX, axisY, axisZ);
      this.center = new Vec3(centerX, centerY, centerZ);
      this.rotatingSpeed = rotatingSpeed;
      this.resizeSpeed = resizeSpeed;
   }

   private SpinData(
      Vec3 axis,
      Vec3 center,
      double rotatingSpeed,
      double resizeSpeed,
      double initialRadius,
      double midRadius,
      double finalRadius,
      double radiusMidpoint,
      boolean expandAndContract,
      Vec3 phaseOffset,
      boolean enableDeviation,
      double deviationAmplitude
   ) {
      this.axis = axis;
      this.center = center;
      this.rotatingSpeed = rotatingSpeed;
      this.resizeSpeed = resizeSpeed;
      this.initialRadius = initialRadius;
      this.midRadius = midRadius;
      this.finalRadius = finalRadius;
      this.radiusMidpoint = Mth.clamp(radiusMidpoint, 0.0, 1.0);
      this.expandAndContract = expandAndContract;
      this.phaseOffset = phaseOffset;
      this.enableDeviation = enableDeviation;
      this.deviationAmplitude = deviationAmplitude;
   }

   @Nonnull
   @Override
   public ControllerType<SpinData> getType() {
      return ParticleTypesST.ParticleBehaviorRegistry.SPIN_CONTROLLER;
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
      pBuffer.writeDouble(this.initialRadius);
      pBuffer.writeDouble(this.midRadius);
      pBuffer.writeDouble(this.finalRadius);
      pBuffer.writeDouble(this.radiusMidpoint);
      pBuffer.writeBoolean(this.expandAndContract);
      pBuffer.writeDouble(this.phaseOffset.x);
      pBuffer.writeDouble(this.phaseOffset.y);
      pBuffer.writeDouble(this.phaseOffset.z);
      pBuffer.writeBoolean(this.enableDeviation);
      pBuffer.writeDouble(this.deviationAmplitude);
   }

   @Override
   public String writeToString() {
      return String.format(
         "%f %f %f %f %f %f %f %f %f %f %f %f %b %f %f %f %b %f",
         this.axis.x,
         this.axis.y,
         this.axis.z,
         this.center.x,
         this.center.y,
         this.center.z,
         this.rotatingSpeed,
         this.resizeSpeed,
         this.initialRadius,
         this.midRadius,
         this.finalRadius,
         this.radiusMidpoint,
         this.expandAndContract,
         this.phaseOffset.x,
         this.phaseOffset.y,
         this.phaseOffset.z,
         this.enableDeviation,
         this.deviationAmplitude
      );
   }

   @Generated
   public static SpinData.SpinDataBuilder builder() {
      return new SpinData.SpinDataBuilder();
   }

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
   public double getInitialRadius() {
      return this.initialRadius;
   }

   @Generated
   public double getMidRadius() {
      return this.midRadius;
   }

   @Generated
   public double getFinalRadius() {
      return this.finalRadius;
   }

   @Generated
   public double getRadiusMidpoint() {
      return this.radiusMidpoint;
   }

   @Generated
   public boolean isExpandAndContract() {
      return this.expandAndContract;
   }

   @Generated
   public boolean isEnableDeviation() {
      return this.enableDeviation;
   }

   @Generated
   public double getDeviationAmplitude() {
      return this.deviationAmplitude;
   }

   @Generated
   public Vec3 getPhaseOffset() {
      return this.phaseOffset;
   }

   @Generated
   public void setStaticMovement(Vec3 staticMovement) {
      this.staticMovement = staticMovement;
   }

   @Generated
   public void setLastTargetPos(Vec3 lastTargetPos) {
      this.lastTargetPos = lastTargetPos;
   }

   @Generated
   public Vec3 getLastTargetPos() {
      return this.lastTargetPos;
   }

   @Generated
   public static class SpinDataBuilder {
      @Generated
      private Vec3 axis;
      @Generated
      private Vec3 center;
      @Generated
      private double rotatingSpeed;
      @Generated
      private double resizeSpeed;
      @Generated
      private double initialRadius;
      @Generated
      private double midRadius;
      @Generated
      private double finalRadius;
      @Generated
      private double radiusMidpoint;
      @Generated
      private boolean expandAndContract;
      @Generated
      private Vec3 phaseOffset;
      @Generated
      private boolean enableDeviation;
      @Generated
      private double deviationAmplitude;

      @Generated
      SpinDataBuilder() {
      }

      @Generated
      public SpinData.SpinDataBuilder axis(Vec3 axis) {
         this.axis = axis;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder center(Vec3 center) {
         this.center = center;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder rotatingSpeed(double rotatingSpeed) {
         this.rotatingSpeed = rotatingSpeed;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder resizeSpeed(double resizeSpeed) {
         this.resizeSpeed = resizeSpeed;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder initialRadius(double initialRadius) {
         this.initialRadius = initialRadius;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder midRadius(double midRadius) {
         this.midRadius = midRadius;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder finalRadius(double finalRadius) {
         this.finalRadius = finalRadius;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder radiusMidpoint(double radiusMidpoint) {
         this.radiusMidpoint = radiusMidpoint;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder expandAndContract(boolean expandAndContract) {
         this.expandAndContract = expandAndContract;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder phaseOffset(Vec3 phaseOffset) {
         this.phaseOffset = phaseOffset;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder enableDeviation(boolean enableDeviation) {
         this.enableDeviation = enableDeviation;
         return this;
      }

      @Generated
      public SpinData.SpinDataBuilder deviationAmplitude(double deviationAmplitude) {
         this.deviationAmplitude = deviationAmplitude;
         return this;
      }

      @Generated
      public SpinData build() {
         return new SpinData(
            this.axis,
            this.center,
            this.rotatingSpeed,
            this.resizeSpeed,
            this.initialRadius,
            this.midRadius,
            this.finalRadius,
            this.radiusMidpoint,
            this.expandAndContract,
            this.phaseOffset,
            this.enableDeviation,
            this.deviationAmplitude
         );
      }

      @Generated
      @Override
      public String toString() {
         return "SpinData.SpinDataBuilder(axis="
            + this.axis
            + ", center="
            + this.center
            + ", rotatingSpeed="
            + this.rotatingSpeed
            + ", resizeSpeed="
            + this.resizeSpeed
            + ", initialRadius="
            + this.initialRadius
            + ", midRadius="
            + this.midRadius
            + ", finalRadius="
            + this.finalRadius
            + ", radiusMidpoint="
            + this.radiusMidpoint
            + ", expandAndContract="
            + this.expandAndContract
            + ", phaseOffset="
            + this.phaseOffset
            + ", enableDeviation="
            + this.enableDeviation
            + ", deviationAmplitude="
            + this.deviationAmplitude
            + ")";
      }
   }
}

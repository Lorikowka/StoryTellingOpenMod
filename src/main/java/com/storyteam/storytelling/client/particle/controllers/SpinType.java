package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class SpinType implements ControllerType<SpinData> {
   @Nonnull
   public Vec3 handleMovement(ParticleInfo info, SpinData data) {
      if (data.getStaticMovement() == Vec3.ZERO) {
         data.setStaticMovement(new Vec3(info.movement().x, info.movement().y, info.movement().z));
      }

      double lifeRatio = (double)info.age() / (double)info.lifetime();
      double baseAngle = lifeRatio * data.getRotatingSpeed() * 2.0 * Math.PI;
      Vec3 offset = data.getPhaseOffset();
      double phaseAngle = Math.atan2(offset.y, offset.x);
      double angle = baseAngle + phaseAngle;
      double radius;
      if (data.isExpandAndContract()) {
         if (lifeRatio < data.getRadiusMidpoint()) {
            radius = this.lerp(data.getInitialRadius(), data.getMidRadius(), lifeRatio / data.getRadiusMidpoint());
         } else {
            radius = this.lerp(data.getMidRadius(), data.getFinalRadius(), (lifeRatio - data.getRadiusMidpoint()) / (1.0 - data.getRadiusMidpoint()));
         }
      } else {
         radius = this.lerp(data.getInitialRadius(), data.getFinalRadius(), lifeRatio);
      }

      double x = radius * Math.cos(angle);
      double y = radius * Math.sin(angle);
      double z = data.getResizeSpeed() * lifeRatio;
      if (data.isEnableDeviation()) {
         double amp = data.getDeviationAmplitude();
         double noiseX = amp * Math.sin(lifeRatio * 20.0 + phaseAngle * 10.0);
         double noiseY = amp * Math.cos(lifeRatio * 15.0 + phaseAngle * 12.0);
         double noiseZ = amp * Math.sin(lifeRatio * 10.0 + phaseAngle * 8.0);
         x += noiseX;
         y += noiseY;
         z += noiseZ;
      }

      Vec3 local = new Vec3(x, y, z);
      Vec3 axis = data.getAxis().normalize();
      Vec3 rotated = this.rotateVectorToAxis(local, axis);
      Vec3 center = data.getCenter();
      Vec3 targetPos = center.add(rotated);
      Vec3 last = data.getLastTargetPos();
      Vec3 delta;
      if (last == null) {
         data.setLastTargetPos(targetPos);
         delta = targetPos.subtract(info.pos());
      } else {
         delta = targetPos.subtract(last);
         data.setLastTargetPos(targetPos);
      }

      return delta.add(data.getStaticMovement());
   }

   private double lerp(double start, double end, double t) {
      return start + (end - start) * t;
   }

   private Vec3 rotateVectorToAxis(Vec3 local, Vec3 axis) {
      if (axis.equals(new Vec3(0.0, 0.0, 1.0))) {
         return local;
      } else {
         Vec3 zAxis = new Vec3(0.0, 0.0, 1.0);
         Vec3 rotationAxis = zAxis.cross(axis).normalize();
         double angle = Math.acos(zAxis.dot(axis));
         return this.rotateAroundAxis(local, rotationAxis, angle);
      }
   }

   private Vec3 rotateAroundAxis(Vec3 v, Vec3 axis, double angle) {
      double cos = Math.cos(angle);
      double sin = Math.sin(angle);
      return v.scale(cos).add(axis.cross(v).scale(sin)).add(axis.scale(axis.dot(v) * (1.0 - cos)));
   }

   public SpinData fromCommand(StringReader reader) throws CommandSyntaxException {
      double axisX = reader.readDouble();
      reader.expect(' ');
      double axisY = reader.readDouble();
      reader.expect(' ');
      double axisZ = reader.readDouble();
      reader.expect(' ');
      double centerX = reader.readDouble();
      reader.expect(' ');
      double centerY = reader.readDouble();
      reader.expect(' ');
      double centerZ = reader.readDouble();
      reader.expect(' ');
      double rotatingSpeed = reader.readDouble();
      reader.expect(' ');
      double resizeSpeed = reader.readDouble();
      reader.expect(' ');
      double initialRadius = reader.readDouble();
      reader.expect(' ');
      double midRadius = reader.readDouble();
      reader.expect(' ');
      double finalRadius = reader.readDouble();
      reader.expect(' ');
      double radiusMidpoint = reader.readDouble();
      reader.expect(' ');
      boolean expandAndContract = reader.readBoolean();
      double phaseOffsetX = reader.readDouble();
      reader.expect(' ');
      double phaseOffsetY = reader.readDouble();
      reader.expect(' ');
      double phaseOffsetZ = reader.readDouble();
      reader.expect(' ');
      boolean enableDeviation = reader.readBoolean();
      double deviationAmplitude = reader.readDouble();
      reader.expect(' ');
      return SpinData.builder()
         .axis(new Vec3(axisX, axisY, axisZ))
         .center(new Vec3(centerX, centerY, centerZ))
         .rotatingSpeed(rotatingSpeed)
         .resizeSpeed(resizeSpeed)
         .initialRadius(initialRadius)
         .midRadius(midRadius)
         .finalRadius(finalRadius)
         .radiusMidpoint(radiusMidpoint)
         .expandAndContract(expandAndContract)
         .phaseOffset(new Vec3(phaseOffsetX, phaseOffsetY, phaseOffsetZ))
         .enableDeviation(enableDeviation)
         .deviationAmplitude(deviationAmplitude)
         .build();
   }

   public SpinData fromNetwork(FriendlyByteBuf buf) {
      Vec3 axis = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
      Vec3 center = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
      double rotatingSpeed = buf.readDouble();
      double resizeSpeed = buf.readDouble();
      double initialRadius = buf.readDouble();
      double midRadius = buf.readDouble();
      double finalRadius = buf.readDouble();
      double radiusMidpoint = buf.readDouble();
      boolean expandAndContract = buf.readBoolean();
      Vec3 phaseOffset = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
      boolean enableDeviation = buf.readBoolean();
      double deviationAmplitude = buf.readDouble();
      return SpinData.builder()
         .axis(axis)
         .center(center)
         .rotatingSpeed(rotatingSpeed)
         .resizeSpeed(resizeSpeed)
         .initialRadius(initialRadius)
         .midRadius(midRadius)
         .finalRadius(finalRadius)
         .radiusMidpoint(radiusMidpoint)
         .expandAndContract(expandAndContract)
         .phaseOffset(phaseOffset)
         .enableDeviation(enableDeviation)
         .deviationAmplitude(deviationAmplitude)
         .build();
   }
}

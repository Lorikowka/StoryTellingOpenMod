package com.storyteam.storytelling.client.particle.controllers;

import com.storyteam.storytelling.init.ParticleTypesST;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LiquidBehaviorData implements ControllerData {
   public final double gravity;
   public Vec3 spreadSpeed;
   long seed;
   public double maxSpreadSpeed;
   public Vec3 preferredDirection;
   public double preferredDirectionAngleSpread;
   public boolean changeDirectionDuePhysics;

   public LiquidBehaviorData(
      double gravity,
      double maxSpreadSpeed,
      LiquidBehaviorData.HorizontalDirection direction,
      double preferredDirectionAngleSpread,
      boolean changeDirectionDuePhysics
   ) {
      this.gravity = gravity;
      this.spreadSpeed = Vec3.ZERO;
      this.seed = UUID.randomUUID().getMostSignificantBits();
      this.maxSpreadSpeed = maxSpreadSpeed;
      this.preferredDirection = direction.toVec3();
      this.preferredDirectionAngleSpread = preferredDirectionAngleSpread;
      this.changeDirectionDuePhysics = changeDirectionDuePhysics;
   }

   public LiquidBehaviorData(double gravity, double maxSpreadSpeed, Vec3 direction, double preferredDirectionAngleSpread, boolean changeDirectionDuePhysics) {
      this.gravity = gravity;
      this.spreadSpeed = Vec3.ZERO;
      this.seed = UUID.randomUUID().getMostSignificantBits();
      this.maxSpreadSpeed = maxSpreadSpeed;
      this.preferredDirection = direction.normalize();
      this.preferredDirectionAngleSpread = preferredDirectionAngleSpread;
      this.changeDirectionDuePhysics = changeDirectionDuePhysics;
   }

   @NotNull
   @Override
   public <T extends ControllerData> ControllerType<T> getType() {
      return (ControllerType<T>)ParticleTypesST.ParticleBehaviorRegistry.LIQUID_BEHAVIOR_CONTROLLER;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf buffer) {
      buffer.writeDouble(this.gravity);
      buffer.writeDouble(this.maxSpreadSpeed);
      buffer.writeDouble(this.preferredDirection.x());
      buffer.writeDouble(this.preferredDirection.y());
      buffer.writeDouble(this.preferredDirection.z());
      buffer.writeDouble(this.preferredDirectionAngleSpread);
      buffer.writeBoolean(this.changeDirectionDuePhysics);
   }

   @Override
   public String writeToString() {
      return String.format(
         "%f %f %f %f %f %f %b",
         this.gravity,
         this.maxSpreadSpeed,
         this.preferredDirection.x(),
         this.preferredDirection.y(),
         this.preferredDirection.z(),
         this.preferredDirectionAngleSpread,
         this.changeDirectionDuePhysics
      );
   }

   public static enum HorizontalDirection {
      NORTH(0, 0, -1),
      EAST(1, 0, 0),
      SOUTH(0, 0, 1),
      WEST(-1, 0, 0);

      public final int x;
      public final int y;
      public final int z;

      private HorizontalDirection(int x, int y, int z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }

      public Vec3 toVec3() {
         return new Vec3((double)this.x, (double)this.y, (double)this.z);
      }

      public static LiquidBehaviorData.HorizontalDirection fromVec3(Vec3 dir) {
         Vec3 norm = dir.normalize();
         LiquidBehaviorData.HorizontalDirection best = null;
         double bestDot = -Double.MAX_VALUE;

         for (LiquidBehaviorData.HorizontalDirection d : values()) {
            Vec3 enumVec = d.toVec3().normalize();
            double dot = norm.dot(enumVec);
            if (dot > bestDot) {
               bestDot = dot;
               best = d;
            }
         }

         return best;
      }
   }
}

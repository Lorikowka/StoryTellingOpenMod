package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LiquidBehaviorController implements ControllerType<LiquidBehaviorData> {
   private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0);

   @NotNull
   public Vec3 handleMovement(ParticleInfo info, LiquidBehaviorData data) {
      Vec3 motion = info.movement();
      boolean onGround = false;
      if ((motion.x() != 0.0 || motion.y() != 0.0 || motion.z() != 0.0)
         && motion.x() * motion.x() + motion.y() * motion.y() + motion.z() * motion.z() < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
         Vec3 collided = Entity.collideBoundingBox(null, motion, info.bb(), info.level(), List.of());
         boolean onGroundByCollision = this.isOnGround(motion.y, collided.y);
         boolean onGroundByBlock = this.isOnGroundByBlock(info.pos(), info, data);
         onGround = onGroundByCollision || onGroundByBlock;
      }

      if (!onGround) {
         Vec3 move = info.movement();
         move = move.add(0.0, data.gravity, 0.0);
         Vec3 collidedMove = Entity.collideBoundingBox(null, move, info.bb(), info.level(), List.of());
         double epsilon = 1.0E-4;
         boolean blockedX = Math.abs(move.x() - collidedMove.x()) > epsilon;
         boolean blockedZ = Math.abs(move.z() - collidedMove.z()) > epsilon;
         if (blockedX) {
            data.spreadSpeed = Vec3.ZERO;
            collidedMove = new Vec3(0.0, move.y(), move.z());
         }

         if (blockedZ) {
            data.spreadSpeed = Vec3.ZERO;
            collidedMove = new Vec3(move.x(), move.y(), 0.0);
         }

         return collidedMove;
      } else {
         if (data.spreadSpeed == Vec3.ZERO) {
            data.spreadSpeed = this.getRandomHorizontalDirection(info, data);
         }

         Vec3 tryMove = data.spreadSpeed;
         if (!data.changeDirectionDuePhysics) {
            return tryMove;
         } else {
            Vec3 collidedMovex = Entity.collideBoundingBox(null, tryMove, info.bb(), info.level(), List.of());
            double epsilonx = 1.0E-4;
            boolean blocked = Math.abs(tryMove.x() - collidedMovex.x()) > epsilonx || Math.abs(tryMove.z() - collidedMovex.z()) > epsilonx;
            if (blocked) {
               data.spreadSpeed = this.getRandomHorizontalDirection(info, data);
               collidedMovex = data.spreadSpeed;
            }

            return collidedMovex;
         }
      }
   }

   boolean isOnGround(double originalY, double movedY) {
      return originalY != movedY;
   }

   private boolean isOnGroundByBlock(Vec3 pos, ParticleInfo info, LiquidBehaviorData data) {
      BlockPos blockBelow = new BlockPos((int)pos.x, (int)(pos.y + data.gravity + (double)(info.width() / 2.0F)), (int)pos.z);
      return info.level().getBlockState(blockBelow).isSolid();
   }

   private Vec3 getRandomHorizontalDirection(ParticleInfo info, LiquidBehaviorData data) {
      Random rnd = new Random(data.seed ^ System.nanoTime());
      double epsilon = 1.0E-4;
      Vec3 preferredNorm = data.preferredDirection.normalize();
      if (data.spreadSpeed.equals(Vec3.ZERO)) {
         double baseYaw = Math.atan2(preferredNorm.z(), preferredNorm.x());
         double spread = Math.toRadians(data.preferredDirectionAngleSpread);
         int angleSteps = 12;
         double step = spread / (double)angleSteps;
         List<Vec3> candidates = new ArrayList<>();

         for (int i = 0; i <= angleSteps; i++) {
            double offset = (double)rnd.nextFloat(-0.5F, 0.5F) * spread;
            double angle = baseYaw + offset;
            Vec3 candidate = new Vec3(Math.cos(angle), 0.0, Math.sin(angle)).scale(data.maxSpreadSpeed);
            Vec3 collision = Entity.collideBoundingBox(null, candidate, info.bb(), info.level(), List.of());
            boolean notBlocked = Math.abs(candidate.x() - collision.x()) < epsilon && Math.abs(candidate.z() - collision.z()) < epsilon;
            if (notBlocked) {
               candidates.add(collision);
            }
         }

         if (!candidates.isEmpty()) {
            return candidates.get(rnd.nextInt(candidates.size()));
         }
      }

      Vec3 currentDir = info.movement();
      Vec3 bestDirection = Vec3.ZERO;
      double bestAlignment = -1.0;
      int offset = rnd.nextInt(12);

      for (int ix = 0; ix < 12; ix++) {
         int index = (ix + offset) % 12;
         double angle = Math.toRadians((double)index * 30.0 + (double)(rnd.nextFloat() * 30.0F - 15.0F));
         Vec3 candidateDir = new Vec3(Math.cos(angle), 0.0, Math.sin(angle)).scale(data.maxSpreadSpeed);
         Vec3 collision = Entity.collideBoundingBox(null, candidateDir, info.bb(), info.level(), List.of());
         boolean notBlocked = Math.abs(candidateDir.x() - collision.x()) < epsilon && Math.abs(candidateDir.z() - collision.z()) < epsilon;
         if (notBlocked) {
            double dot = currentDir.normalize().dot(collision.normalize());
            if (dot > bestAlignment) {
               bestAlignment = dot;
               bestDirection = collision;
            }
         }
      }

      return bestDirection;
   }

   public LiquidBehaviorData fromCommand(StringReader reader) throws CommandSyntaxException {
      double gravity = reader.readDouble();
      reader.expect(' ');
      double maxSpreadSpeed = reader.readDouble();
      double xP = reader.readDouble();
      reader.expect(' ');
      double yP = reader.readDouble();
      reader.expect(' ');
      double zP = reader.readDouble();
      reader.expect(' ');
      Vec3 preferredDirection = new Vec3(xP, yP, zP);
      double preferredDirectionAngleSpread = reader.readDouble();
      reader.expect(' ');
      boolean changeDirectionDuePhysics = reader.readBoolean();
      reader.expect(' ');
      reader.expect(' ');
      return new LiquidBehaviorData(gravity, maxSpreadSpeed, preferredDirection, preferredDirectionAngleSpread, changeDirectionDuePhysics);
   }

   public LiquidBehaviorData fromNetwork(FriendlyByteBuf buffer) {
      double gravity = buffer.readDouble();
      double maxSpreadSpeed = buffer.readDouble();
      Vec3 preferredDirection = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
      double preferredDirectionAngleSpread = buffer.readDouble();
      boolean changeDirectionDuePhysics = buffer.readBoolean();
      return new LiquidBehaviorData(gravity, maxSpreadSpeed, preferredDirection, preferredDirectionAngleSpread, changeDirectionDuePhysics);
   }
}

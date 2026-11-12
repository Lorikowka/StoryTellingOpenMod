package com.storyteam.storytelling.util;

import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.mixins._client.minecraft.ParticleAccessor;
import com.storyteam.storytelling.net.PacketEnginedParticle;
import com.storyteam.storytelling.net.PacketSpawnParticle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;
import org.zeith.hammerlib.net.Network;

public class ParticleHelper {
   static Random rng = new Random(64453560L);

   @Deprecated(
      forRemoval = true
   )
   public static void spawnParticlesClient(Level level, Vec3 start, Vec3 end, int particleCount, ParticleOptions particle) {
      Vec3 add = end.subtract(start).scale(1.0 / (double)particleCount);

      for (int i = 0; i < particleCount; i++) {
         Vec3 pos = start.add(add.scale((double)i));
         level.addParticle(particle, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
      }
   }

   public static void spawnParticleSphere(Level world, ParticleOptions particleOptions, Vec3 center, double radius, boolean fill, int count, double maxSpeed) {
      for (int i = 0; i < count; i++) {
         double moveX = maxSpeed * world.random.nextGaussian();
         double moveY = maxSpeed * world.random.nextGaussian();
         double moveZ = maxSpeed * world.random.nextGaussian();
         spawnParticleSphere(world, particleOptions, center, radius, fill, 1, moveX, moveY, moveZ);
      }
   }

   public static void spawnParticleSphere(
      Level world, ParticleOptions particleOptions, Vec3 center, double radius, boolean fill, int count, double moveX, double moveY, double moveZ
   ) {
      for (int i = 0; i < count; i++) {
         double dx = world.random.nextGaussian() * ((double)world.random.nextInt(2) - 0.5) * 2.0;
         double dy = Math.sqrt(radius * radius - dx * dx) * ((double)world.random.nextInt(2) - 0.5) * 2.0;
         double dz = Math.sqrt(radius * radius - dx * dx - dy * dy)
            * ((double)world.random.nextInt(2) - 0.5)
            * 2.0
            * (fill ? world.random.nextGaussian() : 1.0);
         spawnDirectedParticle(world, particleOptions, center.x + dx, center.y + dy, center.z + dz, moveX, moveY, moveZ);
      }
   }

   public static void spawnCylinderWithoutBases(
      Level world, ParticleOptions particleOptions, Vec3 center, double radius, double height, int heightCount, int countPerCircle, double maxSpeed
   ) {
      int i = 0;

      while ((double)i <= height) {
         spawnParticleCircle(world, particleOptions, center.add(0.0, (double)i, 0.0), radius, false, countPerCircle, maxSpeed);
         i += heightCount;
      }
   }

   public static void spawnCylinderWithoutBases(
      Level world,
      ParticleOptions particleOptions,
      Vec3 center,
      double radius,
      double height,
      int heightCount,
      int countPerCircle,
      double moveX,
      double moveY,
      double moveZ
   ) {
      int i = 0;

      while ((double)i <= height) {
         spawnParticleCircle(world, particleOptions, center.add(0.0, (double)i, 0.0), radius, false, countPerCircle, moveX, moveY, moveZ);
         i += heightCount;
      }
   }

   public static void spawnParticleCircle(
      Level world, ParticleOptions particleOptions, Vec3 center, double radius, boolean fill, int count, double moveX, double moveY, double moveZ
   ) {
      for (int i = 0; i < count; i++) {
         double dx = world.random.nextGaussian() * ((double)world.random.nextInt(2) - 0.5) * 2.0;
         double dz = Math.sqrt(radius * radius - dx * dx) * ((double)world.random.nextInt(2) - 0.5) * 2.0 * (fill ? world.random.nextDouble() : 1.0);
         spawnDirectedParticle(world, particleOptions, center.x + dx, center.y, center.z + dz, moveX, moveY, moveZ);
      }
   }

   public static void spawnParticleCircle(Level world, ParticleOptions particleOptions, Vec3 center, double radius, boolean fill, int count, double maxSpeed) {
      for (int i = 0; i < count; i++) {
         double moveX = maxSpeed * world.random.nextGaussian();
         double moveY = maxSpeed * world.random.nextGaussian();
         double moveZ = maxSpeed * world.random.nextGaussian();
         spawnParticleCircle(world, particleOptions, center, radius, fill, 1, moveX, moveY, moveZ);
      }
   }

   public static void spawnParticleEntity(Level world, ParticleOptions particleOptions, Entity entity, int count, double maxSpeed) {
      spawnParticleAABB(world, particleOptions, entity.getBoundingBox(), count, maxSpeed);
   }

   public static void spawnParticleEntity(Level world, ParticleOptions particleOptions, Entity entity, int count, double moveX, double moveY, double moveZ) {
      spawnParticleAABB(world, particleOptions, entity.getBoundingBox(), count, moveX, moveY, moveZ);
   }

   public static void spawnParticleAABB(Level world, ParticleOptions particleOptions, AABB box, int count, double maxSpeed) {
      Vec3 center = box.getCenter();
      double deltaX = box.getXsize() / 2.0;
      double deltaY = box.getYsize() / 2.0;
      double deltaZ = box.getZsize() / 2.0;
      spawnParticles(world, particleOptions, center.x, center.y, center.z, count, deltaX, deltaY, deltaZ, maxSpeed);
   }

   public static void spawnParticleAABB(Level world, ParticleOptions particleOptions, AABB box, int count, double moveX, double moveY, double moveZ) {
      Vec3 center = box.getCenter();
      double deltaX = box.getXsize() / 2.0;
      double deltaY = box.getYsize() / 2.0;
      double deltaZ = box.getZsize() / 2.0;

      for (int i = 0; i < count; i++) {
         double x = center.x + deltaX * world.random.nextGaussian();
         double y = center.y + deltaY * world.random.nextGaussian();
         double z = center.z + deltaZ * world.random.nextGaussian();
         spawnDirectedParticle(world, particleOptions, x, y, z, moveX, moveY, moveZ);
      }
   }

   public static void spawnParticleLine(Level world, ParticleOptions particle, Vec3 start, Vec3 end, int particleCount, double speed) {
      if (!ScriptUtils.getLevel().isClientSide()) {
         Vec3 delta = end.subtract(start);
         Vec3 dir = delta.normalize();
         double len = delta.length();

         for (int i = 0; i < particleCount; i++) {
            double progress = (double)i * len / (double)particleCount;
            spawnParticles(world, particle, start.x + dir.x * progress, start.y + dir.y * progress, start.z + dir.z * progress, 1, 0.0, 0.0, 0.0, speed);
         }
      }
   }

   public static void spawnParticleLineWithBlockCollision(Level world, ParticleOptions particle, Vec3 start, Vec3 end, int particleCount, double speed) {
      if (!ScriptUtils.getLevel().isClientSide()) {
         boolean touch = false;
         Vec3 delta = end.subtract(start);
         Vec3 dir = delta.normalize();
         double len = delta.length();

         for (int i = 0; i < particleCount; i++) {
            double progress = (double)i * len / (double)particleCount;
            Block block = ScriptUtils.getLevel()
               .getBlockState(BlockPos.containing(start.x + dir.x * progress, start.y + dir.y * progress, start.z + dir.z * progress))
               .getBlock();
            if (new Vec3(start.x + dir.x * progress, start.y + dir.y * progress, start.z + dir.z * progress).distanceTo(ScriptUtils.getPlayer().position())
               <= 1.0) {
               touch = true;
            }

            if (!block.equals(Blocks.AIR) && progress >= 1.0) {
               return;
            }

            spawnParticles(world, particle, start.x + dir.x * progress, start.y + dir.y * progress, start.z + dir.z * progress, 1, 0.0, 0.0, 0.0, speed);
         }
      }
   }

   public static void spawnParticleAtLocation(Level world, ParticleOptions particle, Vec3 location, double speed) {
      spawnParticles(world, particle, location.x, location.y, location.z, 1, 0.0, 0.0, 0.0, speed);
   }

   public static void spawnParticleBoxOutline(Level world, ParticleOptions particle, int particleCount, Vec3 pos) {
      float[] d1 = new float[]{0.0F, 1.0F, 0.0F, 1.0F};
      float[] d2 = new float[]{0.0F, 0.0F, 1.0F, 1.0F};

      for (int i = 0; i < 4; i++) {
         spawnParticleLine(world, particle, pos.add((double)d1[i], (double)d2[i], 0.0), pos.add((double)d1[i], (double)d2[i], 1.0), particleCount, 0.0);
         spawnParticleLine(world, particle, pos.add((double)d1[i], 0.0, (double)d2[i]), pos.add((double)d1[i], 1.0, (double)d2[i]), particleCount, 0.0);
         spawnParticleLine(world, particle, pos.add(0.0, (double)d1[i], (double)d2[i]), pos.add(1.0, (double)d1[i], (double)d2[i]), particleCount, 0.0);
      }
   }

   public static void spawnSimpleParticleOutline(Level world, Color color, Vec3 pos) {
      spawnParticleBoxOutline(world, new CircleTintData(color, 0.05F, 20, 1.0F, false, true), 20, pos);
   }

   public static void spawnDirectedParticle(Level level, ParticleOptions options, double x, double y, double z, double moveX, double moveY, double moveZ) {
      if (level.isClientSide) {
         level.addParticle(options, true, x, y, z, moveX, moveY, moveZ);
      } else {
         Network.sendToAll(new PacketSpawnParticle(options, x, y, z, moveX, moveY, moveZ));
      }
   }

   public static void spawnDirectedParticle(Level level, ParticleOptions options, Vec3 pos, Vec3 move) {
      spawnDirectedParticle(level, options, pos.x, pos.y, pos.z, move.x, move.y, move.z);
   }

   public static void spawnParticles(
      Level level, ParticleOptions options, double x, double y, double z, int count, double dx, double dy, double dz, double maxSpeed
   ) {
      for (int i = 0; i < count; i++) {
         double d1 = level.random.nextGaussian() * dx;
         double d3 = level.random.nextGaussian() * dy;
         double d5 = level.random.nextGaussian() * dz;
         double d6 = level.random.nextGaussian() * maxSpeed;
         double d7 = level.random.nextGaussian() * maxSpeed;
         double d8 = level.random.nextGaussian() * maxSpeed;
         spawnDirectedParticle(level, options, x + d1, y + d3, z + d5, d6, d7, d8);
      }
   }

   public static void spawnParticleAABBTheBest(Level level, ParticleOptions particle, AABB box, int count, double maxSpeed) {
      List<BlockPos> positions = BlockPos.betweenClosedStream(
            new BlockPos((int)box.minX, (int)box.minY, (int)box.minZ), new BlockPos((int)box.maxX, (int)box.maxY, (int)box.maxZ)
         )
         .<BlockPos>map(BlockPos::immutable)
         .toList();

      for (int i = 0; i < count && !positions.isEmpty(); i++) {
         BlockPos pos = positions.get(level.random.nextInt(positions.size()));
         double x = (double)pos.getX() + level.random.nextDouble();
         double y = (double)pos.getY() + level.random.nextDouble();
         double z = (double)pos.getZ() + level.random.nextDouble();
         double dx = (level.random.nextDouble() - 0.5) * 2.0 * maxSpeed;
         double dy = (level.random.nextDouble() - 0.5) * 2.0 * maxSpeed;
         double dz = (level.random.nextDouble() - 0.5) * 2.0 * maxSpeed;
         if (level.isClientSide) {
            level.addParticle(particle, true, x, y, z, dx, dy, dz);
         } else {
            Network.sendToAll(new PacketSpawnParticle(particle, x, y, z, dx, dy, dz));
         }
      }
   }

   public static void spawnParticleAABBTheBest(Level level, ParticleOptions particle, AABB box, int count, double dx, double dy, double dz) {
      List<BlockPos> positions = BlockPos.betweenClosedStream(
            new BlockPos((int)box.minX, (int)box.minY, (int)box.minZ), new BlockPos((int)box.maxX, (int)box.maxY, (int)box.maxZ)
         )
         .<BlockPos>map(BlockPos::immutable)
         .toList();

      for (int i = 0; i < count && !positions.isEmpty(); i++) {
         BlockPos pos = positions.get(level.random.nextInt(positions.size()));
         double x = (double)pos.getX() + level.random.nextDouble();
         double y = (double)pos.getY() + level.random.nextDouble();
         double z = (double)pos.getZ() + level.random.nextDouble();
         double dx1 = (level.random.nextDouble() - 0.5) * 2.0 * dx;
         double dy1 = (level.random.nextDouble() - 0.5) * 2.0 * dy;
         double dz1 = (level.random.nextDouble() - 0.5) * 2.0 * dz;
         if (level.isClientSide) {
            level.addParticle(particle, true, x, y, z, dx1, dy1, dz1);
         } else {
            Network.sendToAll(new PacketSpawnParticle(particle, x, y, z, dx1, dy1, dz1));
         }
      }
   }

   public static void spawnParticles(Level level, ParticleOptions options, Vec3 pos, int count, double dx, double dy, double dz, double maxSpeed) {
      spawnParticles(level, options, pos.x, pos.y, pos.z, count, dx, dy, dz, maxSpeed);
   }

   public static void spawnEnginedParticle(
      Level level, ParticleOptions options, Vec3 pos, double moveX, double moveY, double moveZ, float scale, int lifetime, Color color, float alpha
   ) {
      if (level.isClientSide()) {
         useParticleEngine(level, options, pos, moveX, moveY, moveZ, scale, lifetime, color, alpha);
      } else {
         Network.sendToAll(
            new PacketEnginedParticle(
               options,
               pos.x,
               pos.y,
               pos.z,
               moveX,
               moveY,
               moveZ,
               scale,
               lifetime,
               (float)color.getRed() / 255.0F,
               (float)color.getGreen() / 255.0F,
               (float)color.getBlue() / 255.0F,
               alpha
            )
         );
      }
   }

   public static void spawnEnginedParticle(
      Level level,
      ParticleOptions options,
      double x,
      double y,
      double z,
      double moveX,
      double moveY,
      double moveZ,
      float scale,
      int lifetime,
      Color color,
      float alpha
   ) {
      spawnEnginedParticle(level, options, new Vec3(x, y, z), moveX, moveY, moveZ, scale, lifetime, color, alpha);
   }

   public static void spawnEnginedParticle(Level level, ParticleOptions options, Vec3 pos, Vec3 move, float scale, int lifetime, Color color, float alpha) {
      spawnEnginedParticle(level, options, pos, move.x, move.y, move.z, scale, lifetime, color, alpha);
   }

   @OnlyIn(Dist.CLIENT)
   private static void useParticleEngine(
      Level level, ParticleOptions options, Vec3 pos, double moveX, double moveY, double moveZ, float scale, int lifetime, Color color, float alpha
   ) {
      Particle particle = Minecraft.getInstance().particleEngine.createParticle(options, pos.x, pos.y, pos.z, moveX, moveY, moveZ);
      if (particle != null) {
         particle.scale(scale);
         particle.setLifetime(lifetime);
         particle.setColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
         ((ParticleAccessor)particle).setAlpha(alpha);
      }
   }

   public static void spawnEnginedParticles(
      Level level,
      ParticleOptions options,
      double x,
      double y,
      double z,
      int count,
      double dx,
      double dy,
      double dz,
      double maxSpeed,
      float scale,
      int lifetime,
      Color color,
      float alpha
   ) {
      for (int i = 0; i < count; i++) {
         double d1 = level.random.nextGaussian() * dx;
         double d3 = level.random.nextGaussian() * dy;
         double d5 = level.random.nextGaussian() * dz;
         double d6 = level.random.nextGaussian() * maxSpeed;
         double d7 = level.random.nextGaussian() * maxSpeed;
         double d8 = level.random.nextGaussian() * maxSpeed;
         spawnEnginedParticle(level, options, x + d1, y + d3, z + d5, d6, d7, d8, scale, lifetime, color, alpha);
      }
   }

   public static void spawnEnginedParticles(
      Level level,
      ParticleOptions options,
      Vec3 centerPos,
      int count,
      double dx,
      double dy,
      double dz,
      double maxSpeed,
      float scale,
      int lifetime,
      Color color,
      float alpha
   ) {
      spawnEnginedParticles(level, options, centerPos.x, centerPos.y, centerPos.z, count, dx, dy, dz, maxSpeed, scale, lifetime, color, alpha);
   }

   public static void spawnRandomJaggedParticleLine(
      Level level, Vec3 begin, Vec3 end, double maxJagMultiplier, ParticleOptions options, int particlesPerBlock, int sliceIterations
   ) {
      List<Vec3> locs = new ArrayList<>(List.of(begin));
      locs.addAll(getJagged(begin, end, sliceIterations, maxJagMultiplier));
      locs.add(end);

      for (int i = 1; i < locs.size(); i++) {
         double distance = locs.get(i).subtract(locs.get(i - 1)).length();
         spawnParticleLine(level, options, locs.get(i - 1), locs.get(i), (int)Math.round(distance * (double)particlesPerBlock), 0.0);
      }
   }

   public static void drawJaggedLightning(
      Level level, Vec3 start, Vec3 end, int sliceIterations, double maxJagMultiplier, float d, Color color, boolean doStartBurst
   ) {
      if (doStartBurst) {
         spawnParticleAABB(level, new CircleTintData(color, d, 15, -1.0F, false, false), new AABB(start, start), 10, 0.2);
         spawnParticleAABB(level, new CircleTintData(color, d * 0.5F, 30, -1.0F, false, false), new AABB(start, start), 10, 0.15);
      }

      spawnRandomJaggedParticleLine(level, start, end, maxJagMultiplier, new CircleTintData(color, d, 35, 0.9F, false, false), 16, sliceIterations);
      spawnParticleAABB(level, new CircleTintData(color, d * 0.5F, 50, -1.0F, false, false), new AABB(end, end), 10, 0.06);
      spawnParticleAABB(level, new CircleTintData(color, d, 30, -1.0F, false, false), new AABB(end, end), 10, 0.08);
   }

   public static ArrayList<Vec3> getJagged(Vec3 begin, Vec3 end, int slicesLeft, double maxJagMultiplier) {
      float a = 0.4F + rng.nextFloat(0.2F);
      Vec3 mid = begin.add(end.subtract(begin).scale((double)a))
         .add(getRandomRotNormal(end.subtract(begin)).normalize().scale(rng.nextDouble(end.subtract(begin).length() * maxJagMultiplier)));
      if (slicesLeft > 0) {
         ArrayList<Vec3> left = getJagged(begin, mid, slicesLeft - 1, maxJagMultiplier);
         ArrayList<Vec3> right = getJagged(mid, end, slicesLeft - 1, maxJagMultiplier);
         left.add(mid);
         left.addAll(right);
         return left;
      } else {
         return new ArrayList<>(List.of(mid));
      }
   }

   public static Vec3 getRandomRotNormal(Vec3 vec) {
      Vec3 normal = vec.normalize();
      Vec3 x = normal.x < 0.001 && normal.z < 0.001 ? normal.cross(new Vec3(1.0, 0.0, 0.0)).normalize() : normal.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
      Vec3 z = normal.cross(x).normalize();
      float deg = rng.nextFloat(360.0F);
      return x.scale(Math.cos(Math.toRadians((double)deg))).add(z.scale(Math.sin(Math.toRadians((double)deg))));
   }

   public static void spawnParticlePolygon(
      Level level,
      ParticleOptions particleOptions,
      Vec3 center,
      Vec3 axis,
      double radius,
      int vertices,
      double alphaOffset,
      double moveX,
      double moveY,
      double moveZ
   ) {
      Vector3d axisNormalized = new Vector3d(axis.x, axis.y, axis.z).normalize();
      Vector3d perp = new Vector3d(1.0, 0.0, 0.0);
      if (Math.abs(axisNormalized.x) > 0.99) {
         perp.set(0.0, 1.0, 0.0);
      }

      perp.cross(axisNormalized).normalize();
      Vector3d perp2 = new Vector3d(axisNormalized).cross(perp).normalize();
      double pi = Math.PI;

      for (int i = 0; i < vertices; i++) {
         double alpha = 2.0 * pi * ((double)i / (double)vertices) + alphaOffset;
         Vector3d offset = new Vector3d(perp).mul(Math.cos(alpha) * radius).add(new Vector3d(perp2).mul(Math.sin(alpha) * radius));
         Vec3 pos = center.add(offset.x, offset.y, offset.z);
         spawnDirectedParticle(level, particleOptions, pos.x, pos.y, pos.z, moveX, moveY, moveZ);
      }
   }

   public static void spawnParticlePolygon(
      Level level, ParticleOptions particleOptions, Vec3 center, Vec3 axis, double radius, int vertices, double moveX, double moveY, double moveZ
   ) {
      spawnParticlePolygon(level, particleOptions, center, axis, radius, vertices, 0.0, moveX, moveY, moveZ);
   }
}

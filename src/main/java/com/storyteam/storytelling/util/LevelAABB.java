package com.storyteam.storytelling.util;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LevelAABB extends AABB {
   protected final ResourceKey<Level> dimension;

   public LevelAABB(ResourceKey<Level> dimension, double pX1, double pY1, double pZ1, double pX2, double pY2, double pZ2) {
      super(pX1, pY1, pZ1, pX2, pY2, pZ2);
      this.dimension = dimension;
   }

   public LevelAABB(ResourceKey<Level> dimension, BlockPos pPos) {
      super(pPos);
      this.dimension = dimension;
   }

   public LevelAABB(ResourceKey<Level> dimension, BlockPos pStart, BlockPos pEnd) {
      super(pStart, pEnd);
      this.dimension = dimension;
   }

   public LevelAABB(ResourceKey<Level> dimension, Vec3 pStart, Vec3 pEnd) {
      super(pStart, pEnd);
      this.dimension = dimension;
   }

   public LevelAABB(ResourceKey<Level> dimension, AABB aabb) {
      super(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
      this.dimension = dimension;
   }

   public static LevelAABB of(ResourceKey<Level> level, AABB aabb) {
      return new LevelAABB(level, aabb);
   }

   public static LevelAABB of(ResourceKey<Level> level, double x1, double y1, double z1, double x2, double y2, double z2) {
      return of(level, new AABB(x1, y1, z1, x2, y2, z2));
   }

   public LevelAABB inflate(double pValue) {
      return new LevelAABB(this.dimension, super.inflate(pValue));
   }

   public ResourceKey<Level> getDimension() {
      return this.dimension;
   }

   public boolean sameDimension(Level level) {
      return Objects.equals(level.dimension(), this.dimension);
   }

   public boolean contains(Entity entity) {
      return this.sameDimension(entity.level()) && this.intersects(entity.getBoundingBox());
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.putString("dimension", this.dimension.location().toString());
      tag.putDouble("minX", this.minX);
      tag.putDouble("minY", this.minY);
      tag.putDouble("minZ", this.minZ);
      tag.putDouble("maxX", this.maxX);
      tag.putDouble("maxY", this.maxY);
      tag.putDouble("maxZ", this.maxZ);
      return tag;
   }

   public static LevelAABB deserializeNBT(CompoundTag tag) {
      ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("dimension")));
      double minX = tag.getDouble("minX");
      double minY = tag.getDouble("minY");
      double minZ = tag.getDouble("minZ");
      double maxX = tag.getDouble("maxX");
      double maxY = tag.getDouble("maxY");
      double maxZ = tag.getDouble("maxZ");
      return new LevelAABB(dimension, minX, minY, minZ, maxX, maxY, maxZ);
   }
}

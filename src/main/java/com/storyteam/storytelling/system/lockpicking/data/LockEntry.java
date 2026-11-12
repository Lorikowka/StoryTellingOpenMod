package com.storyteam.storytelling.system.lockpicking.data;

import com.storyteam.storytelling.system.lockpicking.extras.ExtraLockPosProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class LockEntry implements IAutoNBTSerializable {
   @NBTSerializable
   private GlobalPos pos;
   @NBTSerializable
   private Vec3 offset;
   @NBTSerializable
   private BlockPos[] total = new BlockPos[0];
   @NBTSerializable
   private float speed;
   @NBTSerializable
   private int notches;
   @NBTSerializable
   private LockComplexity complexity;
   @NBTSerializable
   private long seed = new Random().nextLong();
   private AABB lockBoundary;

   public LockEntry(GlobalPos pos, float speed, int notches, LockComplexity complexity) {
      this.pos = pos;
      this.speed = speed;
      this.notches = notches;
      this.complexity = complexity;
      this.addPos(pos.pos());
   }

   public LockEntry addPos(BlockPos pos) {
      for (BlockPos p : this.total) {
         if (pos.equals(p)) {
            return this;
         }
      }

      this.lockBoundary = null;
      this.total = (BlockPos[])ArrayUtils.add(this.total, pos);
      return this;
   }

   public LockEntry addAll(HashSet<BlockPos> pos) {
      Set<BlockPos> existing = Set.of(this.total);
      pos.removeAll(existing);
      this.total = (BlockPos[])ArrayUtils.addAll(this.total, pos.toArray(BlockPos[]::new));
      return this;
   }

   public LockEntry offset(Vec3 offset) {
      this.offset = offset;
      return this;
   }

   public AABB getLockBoundary() {
      if (this.lockBoundary == null) {
         int minX = Integer.MAX_VALUE;
         int minY = Integer.MAX_VALUE;
         int minZ = Integer.MAX_VALUE;
         int maxX = Integer.MIN_VALUE;
         int maxY = Integer.MIN_VALUE;
         int maxZ = Integer.MIN_VALUE;

         for (BlockPos pos : this.total) {
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxX = Math.max(maxX, pos.getX());
            maxY = Math.max(maxY, pos.getY());
            maxZ = Math.max(maxZ, pos.getZ());
         }

         this.setLockBoundary(new AABB((double)minX, (double)minY, (double)minZ, (double)(maxX + 1), (double)(maxY + 1), (double)(maxZ + 1)));
      }

      return this.lockBoundary;
   }

   private void setLockBoundary(AABB lockBoundary) {
      this.lockBoundary = lockBoundary;
   }

   public LockEntry setupExtras(ServerLevel level) {
      BlockPos pos = this.pos.pos();
      BlockState state = level.getBlockState(pos);
      HashSet<BlockPos> extras = new HashSet<>();

      for (ExtraLockPosProvider provider : ExtraLockPosProvider.all()) {
         if (provider.test().test(level, pos, state)) {
            provider.extras().accept(level, pos, state, extras::add);
         }
      }

      this.addAll(extras);
      return this;
   }

   @Generated
   public GlobalPos getPos() {
      return this.pos;
   }

   @Generated
   public Vec3 getOffset() {
      return this.offset;
   }

   @Generated
   public BlockPos[] getTotal() {
      return this.total;
   }

   @Generated
   public float getSpeed() {
      return this.speed;
   }

   @Generated
   public int getNotches() {
      return this.notches;
   }

   @Generated
   public LockComplexity getComplexity() {
      return this.complexity;
   }

   @Generated
   public long getSeed() {
      return this.seed;
   }

   @Generated
   public void setPos(GlobalPos pos) {
      this.pos = pos;
   }

   @Generated
   public void setOffset(Vec3 offset) {
      this.offset = offset;
   }

   @Generated
   public void setTotal(BlockPos[] total) {
      this.total = total;
   }

   @Generated
   public void setSpeed(float speed) {
      this.speed = speed;
   }

   @Generated
   public void setNotches(int notches) {
      this.notches = notches;
   }

   @Generated
   public void setComplexity(LockComplexity complexity) {
      this.complexity = complexity;
   }

   @Generated
   public void setSeed(long seed) {
      this.seed = seed;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LockEntry other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (Float.compare(this.getSpeed(), other.getSpeed()) != 0) {
         return false;
      } else if (this.getNotches() != other.getNotches()) {
         return false;
      } else if (this.getSeed() != other.getSeed()) {
         return false;
      } else {
         Object this$pos = this.getPos();
         Object other$pos = other.getPos();
         if (this$pos == null ? other$pos == null : this$pos.equals(other$pos)) {
            Object this$offset = this.getOffset();
            Object other$offset = other.getOffset();
            if (this$offset == null ? other$offset == null : this$offset.equals(other$offset)) {
               if (!Arrays.deepEquals(this.getTotal(), other.getTotal())) {
                  return false;
               } else {
                  Object this$complexity = this.getComplexity();
                  Object other$complexity = other.getComplexity();
                  if (this$complexity == null ? other$complexity == null : this$complexity.equals(other$complexity)) {
                     Object this$lockBoundary = this.getLockBoundary();
                     Object other$lockBoundary = other.getLockBoundary();
                     return this$lockBoundary == null ? other$lockBoundary == null : this$lockBoundary.equals(other$lockBoundary);
                  } else {
                     return false;
                  }
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof LockEntry;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + Float.floatToIntBits(this.getSpeed());
      result = result * 59 + this.getNotches();
      long $seed = this.getSeed();
      result = result * 59 + (int)($seed >>> 32 ^ $seed);
      Object $pos = this.getPos();
      result = result * 59 + ($pos == null ? 43 : $pos.hashCode());
      Object $offset = this.getOffset();
      result = result * 59 + ($offset == null ? 43 : $offset.hashCode());
      result = result * 59 + Arrays.deepHashCode(this.getTotal());
      Object $complexity = this.getComplexity();
      result = result * 59 + ($complexity == null ? 43 : $complexity.hashCode());
      Object $lockBoundary = this.getLockBoundary();
      return result * 59 + ($lockBoundary == null ? 43 : $lockBoundary.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "LockEntry(pos="
         + this.getPos()
         + ", offset="
         + this.getOffset()
         + ", total="
         + Arrays.deepToString(this.getTotal())
         + ", speed="
         + this.getSpeed()
         + ", notches="
         + this.getNotches()
         + ", complexity="
         + this.getComplexity()
         + ", seed="
         + this.getSeed()
         + ", lockBoundary="
         + this.getLockBoundary()
         + ")";
   }

   @Generated
   public LockEntry() {
   }
}

package com.storyteam.storytelling.system.navmesh;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class Navmesh implements IAutoNBTSerializable {
   private final int height;
   private final int slice;
   private final int minY;
   @NBTSerializable
   public byte[] navigatable;

   public Navmesh(int minY, int height) {
      this.height = height;
      this.minY = minY;
      this.slice = this.height * 16;
      this.navigatable = new byte[256 * height / 8];
   }

   public boolean canNavigate(BlockPos pos) {
      int idx = this.getNavigationIndex(pos);
      int wordIndex = idx / 8;
      boolean blocked = wordIndex < this.navigatable.length && ((long)this.navigatable[wordIndex] & 1L << idx % 8) != 0L;
      return !blocked;
   }

   public void setCanNavigate(BlockPos pos, boolean canNavigate) {
      int idx = this.getNavigationIndex(pos);
      if (!canNavigate) {
         this.navigatable[idx / 8] = (byte)(this.navigatable[idx / 8] | 1 << idx % 8);
      } else {
         this.navigatable[idx / 8] = (byte)(this.navigatable[idx / 8] & ~(1 << idx % 8));
      }
   }

   public int getNavigationIndex(BlockPos pos) {
      int x = pos.getX() & 15;
      int z = pos.getZ() & 15;
      int y = pos.getY() - this.minY;
      return y + x * this.height + z * this.slice;
   }

   public int getMaxY() {
      return this.minY + this.height;
   }

   public Navmesh(LevelChunk chunk) {
      this(chunk.getMinBuildHeight(), chunk.getHeight());
   }

   public static Navmesh get(Level level, BlockPos pos) {
      return (Navmesh)level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))
         .getCapability(NavmeshProvider.MESH)
         .orElseThrow(NoSuchElementException::new);
   }

   public static boolean canNavigate(Level level, BlockPos pos) {
      return get(level, pos).canNavigate(pos);
   }

   public static boolean isPositionBlockedForNavigation(Level level, BlockPos pos) {
      Navmesh mesh = get(level, pos);
      return !mesh.canNavigate(pos) || !mesh.canNavigate(pos.below());
   }

   @Generated
   @Override
   public String toString() {
      return "Navmesh(height="
         + this.getHeight()
         + ", slice="
         + this.getSlice()
         + ", minY="
         + this.getMinY()
         + ", navigatable="
         + Arrays.toString(this.navigatable)
         + ")";
   }

   @Generated
   public int getHeight() {
      return this.height;
   }

   @Generated
   public int getSlice() {
      return this.slice;
   }

   @Generated
   public int getMinY() {
      return this.minY;
   }
}

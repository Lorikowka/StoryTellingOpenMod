package com.storyteam.storytelling.system.shaders.lighting;

import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class LightEngine {
   final BlockPos pos;
   final byte light;
   final boolean[] blockData;
   final int size;
   int[] lightData;
   boolean needsUpdate = true;

   public LightEngine(BlockPos pos, byte light) {
      this.pos = pos;
      this.light = light;
      this.size = 2 * light - 1;
      this.blockData = new boolean[this.size * this.size * this.size];
      this.lightData = new int[this.size * this.size * this.size];
   }

   public void setBlockData(BlockPos pos, boolean isOpaque) {
      BlockPos p = pos.subtract(this.pos);

      try {
         boolean lastValue = this.blockData[(p.getX() + this.light - 1) * this.size * this.size
            + (p.getY() + this.light - 1) * this.size
            + p.getZ()
            + this.light
            - 1];
         if (lastValue != isOpaque) {
            this.blockData[(p.getX() + this.light - 1) * this.size * this.size + (p.getY() + this.light - 1) * this.size + p.getZ() + this.light - 1] = isOpaque;
            this.needsUpdate = true;
         }
      } catch (IndexOutOfBoundsException var5) {
      }
   }

   public void updateIfNeeded() {
      if (this.needsUpdate) {
         Arrays.fill(this.lightData, 0);
         this.lightData[(this.light - 1) * this.size * this.size + (this.light - 1) * this.size + this.light - 1] = this.light;
         this.dfs(0, 0, 0);
         this.needsUpdate = false;
      }
   }

   private void dfs(int x, int y, int z) {
      int l = this.getLightInLocalSpace(x, y, z);

      for (Direction dir : Direction.values()) {
         int x1 = x + dir.getStepX();
         int y1 = y + dir.getStepY();
         int z1 = z + dir.getStepZ();
         int index = (x1 + this.light - 1) * this.size * this.size + (y1 + this.light - 1) * this.size + z1 + this.light - 1;
         if (Math.abs(x1) < this.light && Math.abs(y1) < this.light && Math.abs(z1) < this.light && !this.blockData[index]) {
            int l1 = this.getLightInLocalSpace(x1, y1, z1);
            if (l1 < l - 1) {
               this.lightData[index] = (byte)(l - 1);
               this.dfs(x1, y1, z1);
            }
         }
      }
   }

   public byte getLightInLocalSpace(int x, int y, int z) {
      return Math.abs(x) < this.light && Math.abs(y) < this.light && Math.abs(z) < this.light
         ? (byte)this.lightData[(x + this.light - 1) * this.size * this.size + (y + this.light - 1) * this.size + z + this.light - 1]
         : 0;
   }

   public int getLight(BlockPos pos) {
      BlockPos p = pos.subtract(this.pos);
      return this.getLightInLocalSpace(p.getX(), p.getY(), p.getZ());
   }
}

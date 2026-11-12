package com.storyteam.storytelling.system.shaders.lighting;

import java.nio.ByteBuffer;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class LightSource {
   public final int maxDistance;
   public final LightEngine lightEngine;
   private final Level level;
   public BlockPos pos;
   public Vector3f color;
   public Predicate<Level> condition = w -> true;
   private boolean initialized = false;

   public LightSource(Level level, BlockPos pos, int maxDistance) {
      this.level = level;
      this.pos = pos;
      this.maxDistance = maxDistance;
      this.lightEngine = new LightEngine(pos, (byte)(maxDistance + 1));
   }

   public void initLightEngine() {
      this.initialized = false;

      for (int i = -this.maxDistance; i <= this.maxDistance; i++) {
         for (int j = -this.maxDistance; j <= this.maxDistance; j++) {
            for (int k = -this.maxDistance; k <= this.maxDistance; k++) {
               BlockPos p = this.pos.offset(i, j, k);
               if (!this.level.isOutsideBuildHeight(p) && !this.level.isLoaded(p)) {
                  return;
               }

               BlockState state = this.level.getBlockState(p);
               this.lightEngine.setBlockData(p, this.isOpaque(state));
            }
         }
      }

      this.lightEngine.updateIfNeeded();
      this.initialized = true;
   }

   public boolean isOpaque(BlockState state) {
      return state.getLightBlock(this.level, this.pos) != 0;
   }

   public void updateBlock(BlockPos pos, BlockState state) {
      this.lightEngine.setBlockData(pos, this.isOpaque(state));
      this.lightEngine.updateIfNeeded();
   }

   public void writeToBuffer(ByteBuffer buffer) {
      buffer.putInt(this.pos.getX());
      buffer.putInt(this.pos.getY());
      buffer.putInt(this.pos.getZ());
      buffer.putInt(0);
      buffer.putFloat(this.color.x);
      buffer.putFloat(this.color.y);
      buffer.putFloat(this.color.z);
      buffer.putInt(this.maxDistance);
   }

   public void writeLightMap(ByteBuffer buffer) {
      for (int i : this.lightEngine.lightData) {
         buffer.putInt(i);
      }
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }
}

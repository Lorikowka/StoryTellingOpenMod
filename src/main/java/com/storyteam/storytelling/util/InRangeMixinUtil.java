package com.storyteam.storytelling.util;

import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class InRangeMixinUtil {
   public static void inRange(
      int pChunkX,
      int pChunkZ,
      ChunkStatus pRequiredStatus,
      boolean pLoad,
      Level level,
      CallbackInfoReturnable<LevelChunk> cir,
      AtomicReference<LevelChunk> reference
   ) {
      ChunkPos chunkpos = new ChunkPos(pChunkX, pChunkZ);
      if (pChunkX - -26 == 0 && pChunkZ - 72 == 0) {
         if (reference.get() == null) {
            reference.set(new LevelChunk(level, chunkpos));
         }

         cir.setReturnValue(reference.get());
         cir.cancel();
      }
   }
}

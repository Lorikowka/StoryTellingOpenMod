package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({LevelChunk.class})
public interface LevelChunkAccessor {
   @Invoker
   boolean callIsTicking(BlockPos var1);
}

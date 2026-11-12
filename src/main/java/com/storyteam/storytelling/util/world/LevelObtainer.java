package com.storyteam.storytelling.util.world;

import com.storyteam.storytelling.mixins.minecraft.PathNavigationRegionAccessor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class LevelObtainer {
   public static Level fromBlockGetter(BlockGetter getter) {
      if (getter instanceof Level) {
         return (Level)getter;
      } else if (getter instanceof PathNavigationRegionAccessor region) {
         return region.getLevel();
      } else {
         return getter instanceof LevelChunk lc ? lc.getLevel() : null;
      }
   }
}

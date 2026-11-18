package com.storyteam.storytelling.script.performer.action;

/** Inclusive block-coordinate bounds defined by any two opposite corners. */
public record BlockArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
   public static BlockArea between(int x1, int y1, int z1, int x2, int y2, int z2) {
      return new BlockArea(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2),
         Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
   }

   public boolean contains(int x, int y, int z) {
      return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
   }
}

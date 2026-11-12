package com.storyteam.storytelling.api.data;

import lombok.Generated;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AABBWithShape extends AABB {
   private final VoxelShape shape;

   public AABBWithShape(AABB box, VoxelShape shape) {
      super(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
      this.shape = shape;
   }

   public static AABBWithShape of(VoxelShape shape) {
      return new AABBWithShape(shape.bounds(), shape);
   }

   @Generated
   public VoxelShape getShape() {
      return this.shape;
   }
}

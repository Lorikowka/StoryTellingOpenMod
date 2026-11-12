package com.storyteam.storytelling.content.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HumanoidEntityRotate extends HumanoidEntity {
   public double prevRot = 0.0;

   public HumanoidEntityRotate(EntityType<? extends HumanoidEntity> type, Level world) {
      super(type, world);
   }
}

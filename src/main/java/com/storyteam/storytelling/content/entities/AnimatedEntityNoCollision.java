package com.storyteam.storytelling.content.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AnimatedEntityNoCollision extends AnimatedEntity {
   public AnimatedEntityNoCollision(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
   }

   public boolean canCollideWith(Entity pEntity) {
      return false;
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public boolean isPushable() {
      return false;
   }

   public void push(Entity pEntity) {
   }
}

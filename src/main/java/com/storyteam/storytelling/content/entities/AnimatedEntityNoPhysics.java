package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.api.entity.INoNameplate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AnimatedEntityNoPhysics extends AnimatedEntity implements INoNameplate {
   public AnimatedEntityNoPhysics(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   @Override
   public void tick() {
      this.setDeltaMovement(Vec3.ZERO);
      super.tick();
      this.setDeltaMovement(Vec3.ZERO);
   }

   public boolean canCollideWith(Entity pEntity) {
      return false;
   }

   public boolean canBeCollidedWith() {
      return false;
   }
}

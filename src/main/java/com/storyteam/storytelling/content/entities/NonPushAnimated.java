package com.storyteam.storytelling.content.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class NonPushAnimated extends AnimatedEntity {
   public NonPushAnimated(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
      this.setMaxUpStep(0.0F);
   }

   public boolean canCollideWith(@NotNull Entity entity) {
      return false;
   }

   public boolean isColliding(BlockPos pPos, BlockState pState) {
      return false;
   }

   public void push(Entity pEntity) {
      super.push(pEntity);
   }

   public void push(double pX, double pY, double pZ) {
      super.push(pX, pY, pZ);
   }

   public boolean isPushable() {
      return true;
   }

   protected void pushEntities() {
      super.pushEntities();
   }

   protected void doPush(Entity p_20971_) {
   }
}

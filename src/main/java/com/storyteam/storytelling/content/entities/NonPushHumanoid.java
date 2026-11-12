package com.storyteam.storytelling.content.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class NonPushHumanoid extends HumanoidEntity {
   public NonPushHumanoid(EntityType<? extends HumanoidEntity> type, Level world) {
      super(type, world);
   }

   @Override
   public boolean canCollideWith(@NotNull Entity entity) {
      return false;
   }

   public boolean isColliding(BlockPos pPos, BlockState pState) {
      return false;
   }

   public void push(Entity pEntity) {
   }

   public void push(double pX, double pY, double pZ) {
   }

   public boolean isPushable() {
      return true;
   }

   protected void pushEntities() {
   }
}

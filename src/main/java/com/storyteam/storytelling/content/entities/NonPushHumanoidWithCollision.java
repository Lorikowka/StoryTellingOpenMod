package com.storyteam.storytelling.content.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NonPushHumanoidWithCollision extends HumanoidEntity {
   public NonPushHumanoidWithCollision(EntityType<? extends HumanoidEntity> type, Level world) {
      super(type, world);
   }

   @Override
   public boolean canCollideWith(Entity pEntity) {
      return canVehicleCollide(this, pEntity);
   }

   public static boolean canVehicleCollide(Entity pVehicle, Entity pEntity) {
      return (pEntity.canBeCollidedWith() || pEntity.isPushable()) && !pVehicle.isPassengerOfSameVehicle(pEntity);
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public boolean isPushable() {
      return false;
   }

   public boolean isColliding(BlockPos pPos, BlockState pState) {
      return super.isColliding(pPos, pState);
   }

   public void push(Entity pEntity) {
   }

   public void push(double pX, double pY, double pZ) {
   }

   protected void pushEntities() {
   }
}

package com.storyteam.storytelling.content.entities.api;

import lombok.Generated;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class CustomFlyingMoveControl extends MoveControl {
   private final int maxTurn;
   private boolean hoversInPlace;
   float speed = 0.3F;

   public CustomFlyingMoveControl(Mob pMob, int pMaxTurn, boolean pHoversInPlace) {
      super(pMob);
      this.maxTurn = pMaxTurn;
      this.hoversInPlace = pHoversInPlace;
   }

   public void tick() {
      if (this.operation == Operation.MOVE_TO) {
         this.operation = Operation.WAIT;
         this.mob.setNoGravity(true);
         if (this.mob.horizontalCollision || this.mob.verticalCollision) {
            this.speed = 0.1F;
         }

         double deltaX = this.wantedX - this.mob.getX();
         double deltaY = this.wantedY - this.mob.getY() + 0.05;
         double deltaZ = this.wantedZ - this.mob.getZ();
         double distanceSquared = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
         if (Math.abs(distanceSquared) > 1.0E-5) {
            double horizontalAdjustment = 1.0 - Math.abs(deltaY * 0.7) / distanceSquared;
            deltaX *= horizontalAdjustment;
            deltaZ *= horizontalAdjustment;
            double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            double totalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ + deltaY * deltaY);
            float currentYaw = this.mob.getYRot();
            float targetYaw = (float)Mth.atan2(deltaZ, deltaX);
            float adjustedCurrentYaw = Mth.wrapDegrees(this.mob.getYRot() + 90.0F);
            float adjustedTargetYaw = Mth.wrapDegrees(targetYaw * (180.0F / (float)Math.PI));
            this.mob.setYRot(Mth.approachDegrees(adjustedCurrentYaw, adjustedTargetYaw, 4.0F) - 90.0F);
            this.mob.yBodyRot = this.mob.getYRot();
            if (Mth.degreesDifferenceAbs(currentYaw, this.mob.getYRot()) < 4.0F) {
               this.speed = Mth.approach(this.speed, 0.5F, 0.009F * (0.5F / this.speed));
            } else {
               this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
            }

            float targetPitch = (float)(-(Mth.atan2(-deltaY, horizontalDistance) * 180.0F / (float)Math.PI));
            this.mob.setXRot(targetPitch);
            float yawOffset = this.mob.getYRot() + 90.0F;
            double velocityX = (double)(this.speed * Mth.cos(yawOffset * (float) (Math.PI / 180.0))) * Math.abs(deltaX / totalDistance);
            double velocityZ = (double)(this.speed * Mth.sin(yawOffset * (float) (Math.PI / 180.0))) * Math.abs(deltaZ / totalDistance);
            double velocityY = (double)(this.speed * Mth.sin(targetPitch * (float) (Math.PI / 180.0))) * Math.abs(deltaY / totalDistance);
            Vec3 currentVelocity = this.mob.getDeltaMovement();
            Vec3 newVelocity = currentVelocity.add(new Vec3(velocityX, velocityY, velocityZ).subtract(currentVelocity).scale(0.2));
            this.mob.setDeltaMovement(newVelocity);
         }
      } else {
         if (!this.hoversInPlace) {
            this.mob.setNoGravity(false);
         }

         this.mob.setYya(0.0F);
         this.mob.setZza(0.0F);
      }
   }

   @Generated
   public boolean isHoversInPlace() {
      return this.hoversInPlace;
   }

   @Generated
   public void setHoversInPlace(boolean hoversInPlace) {
      this.hoversInPlace = hoversInPlace;
   }
}

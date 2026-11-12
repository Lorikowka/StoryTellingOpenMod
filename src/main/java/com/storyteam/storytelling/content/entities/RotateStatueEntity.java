package com.storyteam.storytelling.content.entities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RotateStatueEntity extends AnimatedEntity {
   private float targetYaw;
   private boolean rotating = false;
   private static final float ROTATION_SPEED = 2.0F;

   public RotateStatueEntity(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
      this.setNoAi(true);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.rotating) {
         float current = this.getYRot();
         float diff = this.normalizeAngle(this.targetYaw - current);
         if (Math.abs(diff) < 2.0F) {
            this.setYRot(this.targetYaw);
            this.setYHeadRot(this.targetYaw);
            this.setYBodyRot(this.targetYaw);
            this.rotating = false;
         } else {
            float next = current + Math.signum(diff) * 2.0F;
            this.setYRot(next);
            this.setYHeadRot(next);
            this.setYBodyRot(next);
         }
      }
   }

   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (!this.level().isClientSide() && hand == InteractionHand.MAIN_HAND && !this.rotating) {
         this.targetYaw = this.normalizeAngle(this.getYRot() + 45.0F);
         this.rotating = true;
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.sidedSuccess(this.level().isClientSide());
      }
   }

   private float normalizeAngle(float angle) {
      angle %= 360.0F;
      if (angle < 0.0F) {
         angle += 360.0F;
      }

      return angle;
   }
}

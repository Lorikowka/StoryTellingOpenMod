package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.client.core.SidedAnimationSet;
import com.storyteam.storytelling.content.caps.StoryData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammerlib.net.properties.PropertyBool;

public class RestrictiveBodyController extends BodyRotationControl {
   private final Mob mob;
   private int headStableTime;
   private float lastStableYHeadRot;

   public RestrictiveBodyController(Mob p_i50334_1_) {
      super(p_i50334_1_);
      this.mob = p_i50334_1_;
   }

   public void clientTick() {
      if (this.isMoving()) {
         this.mob.yBodyRot = this.mob.getYRot();
         this.rotateHeadIfNecessary();
         this.lastStableYHeadRot = this.mob.yHeadRot;
         this.headStableTime = 0;
         if (this.mob instanceof AnimatedEntity animatedEntity) {
            AnimationSystem animationSystem = animatedEntity.getAnimationSystem();
            SidedAnimationSet animDef = animatedEntity.getRotateWalkingAnimation();
            if (animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.right()) || animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.left())) {
               animatedEntity.getAnimationSystem().stopAnimation("ROTATE_WALKING", 0.6F);
            }
         }
      } else if (this.notCarryingMobPassengers()) {
         this.rotateBodyIfNecessary();
      }
   }

   private void rotateBodyIfNecessary() {
      boolean blockAnimRotateWalk = StoryData.optionalOf(this.mob)
         .map(StoryData::getBlockAnimRotateWalking)
         .<Boolean>map(PropertyBool::getBoolean)
         .orElse(false);
      if (!StoryData.optionalOf(this.mob).map(StoryData::getBlockBody).<Boolean>map(PropertyBool::getBoolean).orElse(false)) {
         float angleDifference = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
         float baseSpeed = 0.1F;
         float maxSpeed = 0.8F;
         float rotationSpeed = Math.min(0.8F, baseSpeed + Math.abs(angleDifference) / 180.0F * (maxSpeed - baseSpeed));
         if (this.isMoving()) {
            this.mob.yBodyRot = Mth.lerp(maxSpeed, this.mob.yBodyRot, this.mob.getYRot());
            if (this.mob instanceof AnimatedEntity animatedEntity) {
               AnimationSystem animationSystem = animatedEntity.getAnimationSystem();
               SidedAnimationSet animDef = animatedEntity.getRotateWalkingAnimation();
               if (animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.right()) || animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.left())) {
                  animatedEntity.getAnimationSystem().stopAnimation("ROTATE_WALKING", 0.6F);
               }
            }
         } else {
            if (!this.mob.level().isClientSide()) {
               if (Math.abs(angleDifference) < 10.0F) {
                  if (this.mob instanceof AnimatedEntity animatedEntityx) {
                     AnimationSystem animationSystem = animatedEntityx.getAnimationSystem();
                     SidedAnimationSet animDef = animatedEntityx.getRotateWalkingAnimation();
                     if (animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.right())
                        || animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.left())) {
                        animatedEntityx.getAnimationSystem().stopAnimation("ROTATE_WALKING", 0.6F);
                     }
                  }
               } else if (this.mob instanceof AnimatedEntity animatedEntityxx && !blockAnimRotateWalk) {
                  AnimationSystem animationSystem = animatedEntityxx.getAnimationSystem();
                  SidedAnimationSet animDef = animatedEntityxx.getRotateWalkingAnimation();
                  float weight = 1.0F + rotationSpeed / 0.8F;
                  if (!animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.right()) && angleDifference > 0.0F) {
                     animatedEntityxx.getAnimationSystem().startAnimationAt("ROTATE_WALKING", animDef.right().configure().transitionTime(0.0F).weight(weight));
                  } else if (!animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.left()) && angleDifference < 0.0F) {
                     animatedEntityxx.getAnimationSystem().startAnimationAt("ROTATE_WALKING", animDef.left().configure().transitionTime(0.0F).weight(weight));
                  }
               }
            }

            this.mob.yBodyRot = Mth.lerp(rotationSpeed, this.mob.yBodyRot, this.mob.yHeadRot);
         }
      } else if (this.mob instanceof AnimatedEntity animatedEntityxxx) {
         AnimationSystem animationSystem = animatedEntityxxx.getAnimationSystem();
         SidedAnimationSet animDef = animatedEntityxxx.getRotateWalkingAnimation();
         if (animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.right()) || animationSystem.isActiveAnimation("ROTATE_WALKING", animDef.left())) {
            animatedEntityxxx.getAnimationSystem().stopAnimation("ROTATE_WALKING", 0.6F);
         }
      }
   }

   private void rotateHeadIfNecessary() {
      this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float)this.mob.getMaxHeadYRot());
   }

   private void rotateHeadTowardsFront() {
      StoryData.optionalOf(this.mob).ifPresent(data -> {
         int i = this.headStableTime - 10;
         float f = Mth.clamp((float)i / 10.0F, 0.0F, 1.0F);
         float f1 = (float)this.mob.getMaxHeadYRot() * (1.0F - f);
         if (!data.blockBody.getBoolean()) {
            this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, f1);
         }
      });
   }

   private boolean notCarryingMobPassengers() {
      return this.mob.getPassengers().isEmpty() || !(this.mob.getPassengers().get(0) instanceof Mob) || this.mob instanceof AnimatedEntity;
   }

   private boolean isMoving() {
      double d0 = this.mob.getX() - this.mob.xo;
      double d1 = this.mob.getZ() - this.mob.zo;
      return d0 * d0 + d1 * d1 > 2.5000003E-7F;
   }
}

package com.storyteam.storytelling.system.animation;

import com.storyteam.storytelling.client.AnimationsRegistry;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.util.Chirality;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammeranims.api.animsys.AnimationSystem;

public class EyeGazeSystemSkyEye extends EyeGazeSystem {
   public EyeGazeSystemSkyEye(Entity owner, String layerNamePrefix) {
      super(owner, AnimationsST8.SKY_EYE_GAZE, layerNamePrefix);
   }

   @Override
   public Vec3 getEyePosition(Chirality side) {
      Vec3 lookDirHoriz = this.data.lookDir.multiply(1.0, 0.0, 1.0);
      return this.data.eyeCenter.add(lookDirHoriz.scale(0.2));
   }

   @Override
   public void preApply(AnimationSystem m, float partialTick) {
      AnimationsRegistry.EyeGaze gaze = this.gazeAnims;
      if (this.rightHorizontalLayer != null) {
         float rightHorizWeight = Mth.lerp(partialTick, this.data.oRightHorizWeight, this.data.cRightHorizWeight);
         if (rightHorizWeight > 0.0F) {
            m.startAnimationAt(this.rightEyeHoriz, gaze.rightEyeLooksToRight.configure().startTime(1.0F).transitionTime(1.0E-10F));
         } else {
            m.startAnimationAt(this.rightEyeHoriz, gaze.rightEyeLooksToLeft.configure().startTime(1.0F).transitionTime(1.0E-10F));
         }

         this.rightHorizontalLayer.setWeight(Math.abs(rightHorizWeight));
      }
   }

   @Override
   public void updateClient(Vec3 eyeCenter, float xRot, float yRot, AnimationSystem m) {
      Vec3 gazePosition = this.data.rawGazePosition;
      if (gazePosition == null) {
         this.updateWeight(0.0F, 0.0F);
         this.updateAnimations(m);
      } else {
         this.data.eyeCenter = eyeCenter;
         this.data.lookDir = this.calculateViewVector(0.0F, yRot);
         Vec3 gazePositionHoriz = gazePosition.multiply(1.0, 0.0, 1.0).add(0.0, eyeCenter.y, 0.0);
         Vec3 lookDirHoriz = this.data.lookDir.multiply(1.0, 0.0, 1.0);
         eyeCenter = eyeCenter.add(lookDirHoriz.scale(0.2));
         Vec3 leftGazeDirection = gazePositionHoriz.subtract(eyeCenter).normalize();
         Vec3 rightGazeDirection = gazePositionHoriz.subtract(eyeCenter).normalize();
         double leftAngle = 180.0F / (float)Math.PI * Math.acos(lookDirHoriz.yRot((float) (Math.PI / 2)).dot(leftGazeDirection)) - 90.0;
         double rightAngle = 180.0F / (float)Math.PI * Math.acos(lookDirHoriz.yRot((float) (Math.PI / 2)).dot(rightGazeDirection)) - 90.0;
         double leftHorizontalWeight = leftAngle / 90.0;
         double rightHorizontalWeight = rightAngle / 90.0;
         this.updateWeight((float)leftHorizontalWeight, (float)rightHorizontalWeight);
         this.updateAnimations(m);
      }
   }
}

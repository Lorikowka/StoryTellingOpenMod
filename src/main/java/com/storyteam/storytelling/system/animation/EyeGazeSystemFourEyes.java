package com.storyteam.storytelling.system.animation;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.AnimationsRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.core.init.DefaultsHA;

public class EyeGazeSystemFourEyes extends EyeGazeSystem {
   public EyeGazeSystemFourEyes(Entity owner, String layerNamePrefix) {
      super(owner, layerNamePrefix);
   }

   @Override
   public void preApply(AnimationSystem m, float partialTick) {
      AnimationsRegistry.EyeGaze gaze = Animations.STELLA_EYE_GAZE;
      if (this.leftHorizontalLayer != null) {
         float leftHorizWeight = Mth.lerp(partialTick, this.data.oLeftHorizWeight, this.data.cLeftHorizWeight);
         if (leftHorizWeight < 0.0F) {
            m.startAnimationAt(this.leftEyeHoriz, gaze.leftEyeLooksToLeft.configure().startTime(1.0F).transitionTime(1.0E-10F));
         } else {
            m.startAnimationAt(this.leftEyeHoriz, gaze.leftEyeLooksToRight.configure().startTime(1.0F).transitionTime(1.0E-10F));
         }

         this.leftHorizontalLayer.setWeight(Math.abs(leftHorizWeight));
      }

      if (this.rightHorizontalLayer != null) {
         float rightHorizWeight = Mth.lerp(partialTick, this.data.oRightHorizWeight, this.data.cRightHorizWeight);
         if (rightHorizWeight < 0.0F) {
            m.startAnimationAt(this.rightEyeHoriz, DefaultsHA.NULL_ANIMATION_SYNTETIC.configure().startTime(1.0F).transitionTime(1.0E-10F));
         } else {
            m.startAnimationAt(this.rightEyeHoriz, gaze.rightEyeLooksToRight.configure().startTime(1.0F).transitionTime(1.0E-10F));
         }

         this.rightHorizontalLayer.setWeight(Math.abs(rightHorizWeight));
      }
   }
}

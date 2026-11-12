package com.storyteam.storytelling.system.animation;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.AnimationsRegistry;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.net.gaze.PacketSetGaze;
import com.storyteam.storytelling.util.Chirality;
import com.storyteam.storytelling.util.LayersList;
import lombok.Generated;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammeranims.api.animation.interp.BlendMode;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;
import org.zeith.hammeranims.core.init.DefaultsHA;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.Network;

public class EyeGazeMeTabSystem extends EyeGazeSystem {
   protected final String eyesVert;
   protected float eyeDistance = 0.25F;
   public final EyeGazeMeTabSystem.GazeData data = new EyeGazeMeTabSystem.GazeData();
   protected boolean gazePoseChanged;
   protected AnimationLayer leftHorizontalLayer;
   protected AnimationLayer rightHorizontalLayer;
   protected AnimationLayer bothVertical;

   public EyeGazeMeTabSystem(Entity owner, String layerNamePrefix) {
      super(owner, layerNamePrefix);
      this.eyesVert = layerNamePrefix + "Vert";
   }

   @Override
   public void setGazePosition(Vec3 gazePosition) {
      if (gazePosition != null && gazePosition.length() < 1.0E-5) {
         gazePosition = null;
      }

      if (gazePosition == null) {
         if (this.owner.level().isClientSide) {
            this.updateWeight(0.0F, 0.0F, 0.0F);
         } else {
            Network.sendToAll(new PacketSetGaze(this.owner.getId(), null));
         }

         this.data.rawGazePosition = null;
      } else {
         if (this.data.rawGazePosition == null || !this.data.rawGazePosition.closerThan(gazePosition, 1.0E-5)) {
            if (!this.owner.level().isClientSide) {
               Network.sendToAll(new PacketSetGaze(this.owner.getId(), gazePosition));
            }

            this.data.rawGazePosition = gazePosition;
         }
      }
   }

   @Override
   public void setupLayers(LayersList layers) {
      layers.addLast(new LayersList.LayerDefinition(this.eyesVert, BlendMode.ADD, 0.0F).preventAutoSync().nonPersistent());
      layers.addLast(new LayersList.LayerDefinition(this.leftEyeHoriz, BlendMode.ADD, 0.0F).preventAutoSync().nonPersistent());
      layers.addLast(new LayersList.LayerDefinition(this.rightEyeHoriz, BlendMode.ADD, 0.0F).preventAutoSync().nonPersistent());
   }

   @Override
   public void updateAnimations(AnimationSystem m) {
      if (this.gazePoseChanged) {
         this.gazePoseChanged = false;
      }

      this.leftHorizontalLayer = m.getLayer(this.leftEyeHoriz);
      this.rightHorizontalLayer = m.getLayer(this.rightEyeHoriz);
      this.bothVertical = m.getLayer(this.eyesVert);
      this.data.tick();
   }

   public void updateWeight(float leftHoriz, float rightHoriz, float eyesVert) {
      if (this.data.leftHorizWeight != leftHoriz || this.data.rightHorizWeight != rightHoriz || this.data.eyesVertWeight != eyesVert) {
         this.data.leftHorizWeight = leftHoriz;
         this.data.rightHorizWeight = rightHoriz;
         this.data.eyesVertWeight = eyesVert;
      }
   }

   @Override
   public Vec3 getEyePosition(Chirality side) {
      Vec3 lookDirHoriz = this.data.lookDir.multiply(1.0, 0.0, 1.0);
      Vec3 eyeCenter = this.data.eyeCenter.add(lookDirHoriz.scale(0.2));

      return switch (side) {
         case LEFT -> eyeCenter.add(lookDirHoriz.yRot(45.0F).scale(0.15));
         case RIGHT -> eyeCenter.add(lookDirHoriz.yRot(-45.0F).scale(0.15));
      };
   }

   @Override
   public void updateClient(Vec3 eyeCenter, float xRot, float yRot, AnimationSystem m) {
      Vec3 gazePosition = this.data.rawGazePosition;
      if (gazePosition == null) {
         this.updateWeight(0.0F, 0.0F, 0.0F);
         this.updateAnimations(m);
      } else {
         this.data.eyeCenter = eyeCenter;
         this.data.lookDir = this.calculateViewVector(0.0F, yRot);
         Vec3 gazePositionHoriz = gazePosition.multiply(1.0, 0.0, 1.0).add(0.0, eyeCenter.y, 0.0);
         Vec3 lookDirHoriz = this.data.lookDir.multiply(1.0, 0.0, 1.0);
         eyeCenter = eyeCenter.add(lookDirHoriz.scale(0.2));
         Vec3 leftEyeCenter = eyeCenter.add(lookDirHoriz.yRot(45.0F).scale(0.15));
         Vec3 rightEyeCenter = eyeCenter.add(lookDirHoriz.yRot(-45.0F).scale(0.15));
         Vec3 leftGazeDirection = gazePositionHoriz.subtract(leftEyeCenter).normalize();
         Vec3 rightGazeDirection = gazePositionHoriz.subtract(rightEyeCenter).normalize();
         float leftAngle = (float)Mth.clamp(
               180.0F / (float)Math.PI * Math.acos(lookDirHoriz.yRot((float) (Math.PI / 2)).dot(leftGazeDirection)) - 15.0, 0.0, 180.0
            )
            - 90.0F;
         float rightAngle = (float)Mth.clamp(
               180.0F / (float)Math.PI * Math.acos(lookDirHoriz.yRot((float) (Math.PI / 2)).dot(rightGazeDirection)) + 15.0, 0.0, 180.0
            )
            - 90.0F;
         float leftHorizontalWeight = leftAngle / 90.0F;
         float rightHorizontalWeight = rightAngle / 90.0F;
         Vec3 toTarget = gazePosition.subtract(eyeCenter).normalize();
         float lookPitch = (float)Math.asin(Mth.clamp(this.data.lookDir.y, -1.0, 1.0));
         float targetPitch = (float)Math.asin(Mth.clamp(toTarget.y, -1.0, 1.0));
         float deltaPitch = targetPitch - lookPitch;
         float verticalWeight = Mth.clamp(deltaPitch / (float) (Math.PI / 4), -1.0F, 1.0F);
         this.updateWeight(leftHorizontalWeight, rightHorizontalWeight, verticalWeight);
         this.updateAnimations(m);
      }
   }

   @Override
   public void preApply(AnimationSystem m, float partialTick) {
      AnimationsRegistry.EyeGaze gaze = Animations.EYE_GAZE;
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

      if (this.bothVertical != null) {
         float vertWeight = Mth.lerp(partialTick, this.data.oEyesVertWeight, this.data.cEyesVertWeight);
         if (vertWeight > 0.0F) {
            m.startAnimationAt(this.eyesVert, AnimationsST8.EYES_UP_NARROW_EYED.configure().startTime(1.0F).transitionTime(1.0E-10F));
         } else {
            m.startAnimationAt(this.eyesVert, AnimationsST8.EYES_DOWN_NARROW_EYED.configure().startTime(1.0F).transitionTime(1.0E-10F));
         }

         this.bothVertical.setWeight(Math.abs(vertWeight));
      }
   }

   @Generated
   @Override
   public void setEyeDistance(float eyeDistance) {
      this.eyeDistance = eyeDistance;
   }

   public static class GazeData implements IAutoNBTSerializable {
      @NBTSerializable
      public float leftHorizWeight;
      @NBTSerializable
      public float rightHorizWeight;
      @NBTSerializable
      public float eyesVertWeight;
      public float cLeftHorizWeight;
      public float cRightHorizWeight;
      public float cEyesVertWeight;
      public float oLeftHorizWeight;
      public float oRightHorizWeight;
      public float oEyesVertWeight;
      @NBTSerializable
      public Vec3 rawGazePosition;
      @NBTSerializable
      public Vec3 eyeCenter = Vec3.ZERO;
      @NBTSerializable
      public Vec3 lookDir = Vec3.ZERO;

      public void copyFrom(EyeGazeMeTabSystem.GazeData data) {
         this.leftHorizWeight = data.leftHorizWeight;
         this.rightHorizWeight = data.rightHorizWeight;
         this.eyesVertWeight = data.eyesVertWeight;
         this.rawGazePosition = data.rawGazePosition;
         this.eyeCenter = data.eyeCenter;
         this.lookDir = data.lookDir;
      }

      public void tick() {
         this.oLeftHorizWeight = this.cLeftHorizWeight;
         this.oRightHorizWeight = this.cRightHorizWeight;
         this.oEyesVertWeight = this.cEyesVertWeight;
         this.cLeftHorizWeight = Mth.approachDegrees(this.cLeftHorizWeight, this.leftHorizWeight, 0.15F);
         this.cRightHorizWeight = Mth.approachDegrees(this.cRightHorizWeight, this.rightHorizWeight, 0.15F);
         this.cEyesVertWeight = Mth.approachDegrees(this.cEyesVertWeight, this.eyesVertWeight, 0.15F);
      }
   }
}

package com.storyteam.storytelling.system.fight;

import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.core.client.model.ModelBoneF;
import org.zeith.hammerlib.util.java.tuples.Tuple3;

public class AttackAnimAction {
   List<Tuple3<Float, Float, Float>> time_inflate = new ArrayList<>();
   float time_anim_max = 0.0F;
   float time_anim = 0.0F;
   List<String> bones = new ArrayList<>();
   boolean is_active = false;
   float base_inflate = 1.0F;
   Vec3 inflate_v3 = new Vec3(1.0, 1.0, 1.0);
   Vec3 offsetV3 = new Vec3(0.0, 0.0, 0.0);
   List<ModelBoneF> modelBoneFList = new ArrayList<>();

   public AttackAnimAction() {
   }

   public AttackAnimAction(
      float time_anim_max,
      float time_anim,
      List<String> bones,
      boolean is_active,
      float base_inflate,
      List<Tuple3<Float, Float, Float>> time_inflate,
      Vec3 inflate_v3,
      Vec3 offsetV3
   ) {
      this.time_anim_max = time_anim_max;
      this.time_anim = time_anim;
      this.bones = bones;
      this.time_inflate = time_inflate;
      this.is_active = is_active;
      this.base_inflate = base_inflate;
      this.inflate_v3 = inflate_v3;
      this.offsetV3 = offsetV3;
   }

   public AttackAnimAction(
      ConfiguredAnimation configuredAnimation,
      float time_anim_max,
      List<String> bones,
      AnimatedEntityLikeHumanoid entity,
      float base_inflate,
      List<Tuple3<Float, Float, Float>> time_inflate,
      Vec3 inflate_v3,
      Vec3 offsetV3
   ) {
      if (time_anim_max == 0.0F) {
         this.time_anim_max = (float)configuredAnimation.animation
               .getData()
               .getLength()
               .minus(Duration.ofMillis((long)Mth.floor(configuredAnimation.startTime * 1000.0F)))
               .toMillis()
            / 50.0F
            / configuredAnimation.speed;
      } else {
         this.time_anim_max = time_anim_max;
      }

      this.time_anim = 0.0F;
      this.bones = bones;
      this.time_inflate = time_inflate;
      this.is_active = true;
      this.base_inflate = base_inflate;
      this.inflate_v3 = inflate_v3;

      for (String bone : bones) {
         ModelBoneF modelBoneF = Collider.getModelBone(entity, bone);
         if (modelBoneF != null) {
            this.modelBoneFList.add(modelBoneF);
         }
      }

      this.offsetV3 = offsetV3;
   }

   public float inflateBoxFromTime() {
      if (this.time_inflate.isEmpty()) {
         return 1.0F;
      } else {
         for (Tuple3<Float, Float, Float> triple : this.time_inflate) {
            if (this.time_anim >= (Float)triple.a() && this.time_anim <= (Float)triple.b()) {
               return (Float)triple.c();
            }
         }

         return 1.0F;
      }
   }

   @Generated
   public List<Tuple3<Float, Float, Float>> getTime_inflate() {
      return this.time_inflate;
   }

   @Generated
   public float getTime_anim_max() {
      return this.time_anim_max;
   }

   @Generated
   public float getTime_anim() {
      return this.time_anim;
   }

   @Generated
   public List<String> getBones() {
      return this.bones;
   }

   @Generated
   public boolean is_active() {
      return this.is_active;
   }

   @Generated
   public float getBase_inflate() {
      return this.base_inflate;
   }

   @Generated
   public Vec3 getInflate_v3() {
      return this.inflate_v3;
   }

   @Generated
   public Vec3 getOffsetV3() {
      return this.offsetV3;
   }

   @Generated
   public List<ModelBoneF> getModelBoneFList() {
      return this.modelBoneFList;
   }

   @Generated
   public void setTime_inflate(List<Tuple3<Float, Float, Float>> time_inflate) {
      this.time_inflate = time_inflate;
   }

   @Generated
   public void setTime_anim_max(float time_anim_max) {
      this.time_anim_max = time_anim_max;
   }

   @Generated
   public void setTime_anim(float time_anim) {
      this.time_anim = time_anim;
   }

   @Generated
   public void setBones(List<String> bones) {
      this.bones = bones;
   }

   @Generated
   public void set_active(boolean is_active) {
      this.is_active = is_active;
   }

   @Generated
   public void setBase_inflate(float base_inflate) {
      this.base_inflate = base_inflate;
   }

   @Generated
   public void setInflate_v3(Vec3 inflate_v3) {
      this.inflate_v3 = inflate_v3;
   }

   @Generated
   public void setOffsetV3(Vec3 offsetV3) {
      this.offsetV3 = offsetV3;
   }

   @Generated
   public void setModelBoneFList(List<ModelBoneF> modelBoneFList) {
      this.modelBoneFList = modelBoneFList;
   }
}

package com.storyteam.storytelling.system.fight;

import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammerlib.util.java.tuples.Tuple3;

public class AttackAnimActionBuilder {
   List<Tuple3<Float, Float, Float>> time_inflate = new ArrayList<>();
   List<String> bones = new ArrayList<>();
   float base_inflate = 1.0F;
   Vec3 inflate_v3 = new Vec3(1.0, 1.0, 1.0);
   Vec3 offsetV3 = new Vec3(0.0, 0.0, 0.0);
   ConfiguredAnimation configuredAnimation;
   AnimatedEntityLikeHumanoid entity;
   float time_anim_max = 0.0F;

   public AttackAnimActionBuilder(ConfiguredAnimation configuredAnimation, String bone, AnimatedEntityLikeHumanoid entity) {
      this.configuredAnimation = configuredAnimation;
      this.time_anim_max = (float)configuredAnimation.animation
            .getData()
            .getLength()
            .minus(Duration.ofMillis((long)Mth.floor(configuredAnimation.startTime * 1000.0F)))
            .toMillis()
         / 50.0F
         / configuredAnimation.speed;
      this.bones = List.of(bone);
      this.entity = entity;
   }

   public AttackAnimActionBuilder(ConfiguredAnimation configuredAnimation, List<String> bones, AnimatedEntityLikeHumanoid entity) {
      this.configuredAnimation = configuredAnimation;
      this.time_anim_max = (float)configuredAnimation.animation
            .getData()
            .getLength()
            .minus(Duration.ofMillis((long)Mth.floor(configuredAnimation.startTime * 1000.0F)))
            .toMillis()
         / 50.0F
         / configuredAnimation.speed;
      this.bones = bones;
      this.entity = entity;
   }

   @SafeVarargs
   public final AttackAnimActionBuilder withTimeInflate(Tuple3<Float, Float, Float>... time_inflate) {
      this.time_inflate = List.of(time_inflate);
      return this;
   }

   public AttackAnimActionBuilder withBaseInflate(float base_inflate) {
      this.base_inflate = base_inflate;
      return this;
   }

   public AttackAnimActionBuilder withInflateV3(Vec3 inflateV3) {
      this.inflate_v3 = inflateV3;
      return this;
   }

   public AttackAnimActionBuilder withOffset(Vec3 offsetV3) {
      this.offsetV3 = offsetV3;
      return this;
   }

   public AttackAnimActionBuilder setTimeAnimMax(float time_anim_max) {
      this.time_anim_max = time_anim_max;
      return this;
   }

   public AttackAnimAction build() {
      return new AttackAnimAction(
         this.configuredAnimation, this.time_anim_max, this.bones, this.entity, this.base_inflate, this.time_inflate, this.inflate_v3, this.offsetV3
      );
   }
}

package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class HeadLololoshkaAntEntity extends HumanoidEntity {
   public HeadLololoshkaAntEntity(EntityType<? extends HumanoidEntity> type, Level world) {
      super(type, world);
   }

   @Override
   public SidedAnimationSet getRotateWalkingAnimation() {
      return AnimationsST8.HEAD_LOLOLOSHKA_ANT_ROTATE_WALKING;
   }

   @Override
   public ConfiguredAnimation getBlinkingAnimation() {
      return AnimationsST8.ANTS_BLINKING.configure().transitionTime(0.0F).important();
   }
}

package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.client.animations.AnimationsST8;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class AntGroupEntity extends HumanoidEntity {
   public AntGroupEntity(EntityType<? extends HumanoidEntity> type, Level world) {
      super(type, world);
   }

   @Override
   public ConfiguredAnimation getBlinkingAnimation() {
      return AnimationsST8.ANTS_BLINKING.configure().transitionTime(0.0F).important();
   }
}

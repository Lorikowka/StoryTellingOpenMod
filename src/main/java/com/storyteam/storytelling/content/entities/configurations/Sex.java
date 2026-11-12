package com.storyteam.storytelling.content.entities.configurations;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.animations.AnimationsST5;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import java.util.List;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public enum Sex implements IDefaultAnimConfiguration {
   STEVE(
      EntityModelsST.CITY_STEVE,
      IDefaultAnimConfiguration.empty()
         .with(MovementActionType.IDLE, AnimationsST5.AnimationsIdle.CITY_IDLE_STEVE_NEW)
         .with(MovementActionType.WALKING, AnimationsST5.AnimationsIdle.CITY_WALK_STEVE_NEW)
         .with(MovementActionType.RUNNING, Animations.CITY_BIG_RUN)
         .with(MovementActionType.FAST_WALKING, AnimationsST5.AnimationsIdle.CITY_FAST_WALK_STEVE_NEW)
   ),
   ALEX(
      EntityModelsST.CITY_ALEX,
      IDefaultAnimConfiguration.empty()
         .with(MovementActionType.IDLE, AnimationsST5.AnimationsIdle.CITY_IDLE_ALEX_NEW)
         .with(MovementActionType.WALKING, AnimationsST5.AnimationsIdle.CITY_WALK_ALEX_NEW)
         .with(MovementActionType.RUNNING, Animations.CITY_BIG_RUN)
         .with(MovementActionType.FAST_WALKING, AnimationsST5.AnimationsIdle.CITY_FAST_WALK_ALEX_NEW)
   );

   final IGeometryContainer model;
   final IDefaultAnimConfiguration container;

   @NotNull
   @Override
   public List<ConfiguredAnimation> getAnimations(MovementActionType type) {
      return this.container.getAnimations(type);
   }

   @Generated
   public IGeometryContainer getModel() {
      return this.model;
   }

   @Generated
   public IDefaultAnimConfiguration getContainer() {
      return this.container;
   }

   @Generated
   private Sex(IGeometryContainer model, IDefaultAnimConfiguration container) {
      this.model = model;
      this.container = container;
   }
}

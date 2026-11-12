package com.storyteam.storytelling.content.entities.configurations;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class AnimConfigurationImpl implements IDefaultAnimConfiguration {
   Map<MovementActionType, Supplier<List<ConfiguredAnimation>>> map;

   public AnimConfigurationImpl(Map<MovementActionType, Supplier<List<ConfiguredAnimation>>> map) {
      this.map = map;
   }

   @NotNull
   @Override
   public List<ConfiguredAnimation> getAnimations(MovementActionType type) {
      return this.map.get(type).get();
   }
}

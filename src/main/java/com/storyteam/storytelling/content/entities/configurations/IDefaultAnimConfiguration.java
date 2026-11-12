package com.storyteam.storytelling.content.entities.configurations;

import com.storyteam.storytelling.client.Animations;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public interface IDefaultAnimConfiguration {
   @NotNull
   List<ConfiguredAnimation> getAnimations(MovementActionType var1);

   @NotNull
   default IDefaultAnimConfiguration with(MovementActionType type, Supplier<List<ConfiguredAnimation>> animations) {
      Map<MovementActionType, Supplier<List<ConfiguredAnimation>>> map = new HashMap<>();
      Arrays.stream(MovementActionType.values()).forEach(t -> map.put(t, () -> this.getAnimations(t)));
      map.put(type, animations);
      return new AnimConfigurationImpl(map);
   }

   @NotNull
   default IDefaultAnimConfiguration with(MovementActionType type, IAnimationSource... animation) {
      return this.with(type, () -> Arrays.stream(animation).<ConfiguredAnimation>map(IAnimationSource::configure).toList());
   }

   static IDefaultAnimConfiguration empty() {
      return new AnimConfigurationImpl(new HashMap<>());
   }

   static IDefaultAnimConfiguration noAnims() {
      Map<MovementActionType, Supplier<List<ConfiguredAnimation>>> map = new HashMap<>();
      Arrays.stream(MovementActionType.values()).forEach(t -> map.put(t, () -> List.of(Animations.NULL.configure())));
      return new AnimConfigurationImpl(map);
   }
}

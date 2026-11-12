package com.storyteam.storytelling.client.core;

import com.storyteam.storytelling.api.animations.IAnimationGroup;
import com.storyteam.storytelling.util.Chirality;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.zeith.hammeranims.api.animation.IAnimationSource;

public class SidedAnimationSet implements IAnimationGroup {
   private final Map<Chirality, String> sidedVariants = new HashMap<>();
   private final Map<String, IAnimationSource> animations;

   public SidedAnimationSet(Map<String, IAnimationSource> animations) {
      this.animations = new HashMap<>(animations);
   }

   public SidedAnimationSet forSide(Chirality side, String variant) {
      this.sidedVariants.put(side, variant);
      return this;
   }

   public IAnimationSource side(Chirality side) {
      return this.animations.get(this.sidedVariants.get(side));
   }

   public IAnimationSource left() {
      return this.side(Chirality.LEFT);
   }

   public IAnimationSource right() {
      return this.side(Chirality.RIGHT);
   }

   @Override
   public Collection<String> getKeys() {
      return Chirality.MAPPINGS.keySet();
   }

   @Override
   public Optional<IAnimationSource> getAnimation(String key) {
      return Optional.ofNullable(Chirality.MAPPINGS.getOrDefault(key, null)).map(this::side);
   }
}

package com.storyteam.storytelling.api.animations;

import java.util.Collection;
import java.util.Optional;
import org.zeith.hammeranims.api.animation.IAnimationSource;

public interface IAnimationGroup {
   Collection<String> getKeys();

   Optional<IAnimationSource> getAnimation(String var1);
}

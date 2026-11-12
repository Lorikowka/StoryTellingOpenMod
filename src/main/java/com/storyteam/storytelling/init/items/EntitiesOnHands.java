package com.storyteam.storytelling.init.items;

import com.storyteam.storytelling.client.Animations;
import java.util.function.Consumer;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "entities_on_hands/"
)
public interface EntitiesOnHands {
   static Consumer<AnimationSystem> breathing() {
      return s -> s.startAnimationAt("ambient", Animations.HUMANOID_IDLE.configure().transitionTime(0.0F));
   }
}

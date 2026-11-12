package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.configurations.ILivingAnimationEntity;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class OverridePredefinedAnimationAction<T extends Entity & ILivingAnimationEntity> extends EntityPerformerAction<T> {
   final Supplier<ConfiguredAnimation> animation;
   @Nullable
   final MovementActionType type;

   public OverridePredefinedAnimationAction(ScriptPerformer performer, Character<T> character, Supplier<ConfiguredAnimation> animation) {
      this(performer, character, null, animation);
   }

   public OverridePredefinedAnimationAction(ScriptPerformer performer, Character<T> character, ConfiguredAnimation animation) {
      this(performer, character, null, () -> animation);
   }

   public OverridePredefinedAnimationAction(
      ScriptPerformer performer, Character<T> character, @Nullable MovementActionType actionType, Supplier<ConfiguredAnimation> animation
   ) {
      super(performer, character);
      this.animation = animation;
      this.type = actionType;
   }

   private void override() {
      ConfiguredAnimation configuredAnimation = this.animation.get();
      if (this.type == null) {
         this.getEntity().getData().setOverrideAnimationAll(configuredAnimation);
      } else {
         this.getEntity().getData().getOverrideAnimations().put(this.type, configuredAnimation);
      }
   }

   @Override
   public void rewindToStage() {
      if (this.type == null) {
         this.getEntity().getData().setOverrideAnimationAll(null);
      } else {
         this.getEntity().getData().getOverrideAnimations().put(this.type, null);
      }
   }

   @Override
   public void fastForwardToStage() {
      this.override();
   }

   @Override
   public void tick() {
      this.override();
   }
}

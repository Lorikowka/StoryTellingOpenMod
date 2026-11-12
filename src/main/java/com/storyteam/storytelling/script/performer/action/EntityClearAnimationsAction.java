package com.storyteam.storytelling.script.performer.action;

import com.google.common.collect.Lists;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class EntityClearAnimationsAction<E extends AnimatedEntity> extends EntityPerformerAction<E> {
   protected final UnaryOperator<ConfiguredAnimation> configurator;

   public EntityClearAnimationsAction(ScriptPerformer performer, @NotNull Character<E> character, UnaryOperator<ConfiguredAnimation> configurator) {
      super(performer, character);
      this.configurator = configurator;
   }

   @Override
   public void tick() {
      for (String layer : Lists.newArrayList(this.getEntity().getAnimationLayers())) {
         this.getEntity().setAnimation(layer, this.configurator.apply(Animations.NULL.configure()));
      }
   }

   @Override
   public void fastForwardToStage() {
      for (String layer : Lists.newArrayList(this.getEntity().getAnimationLayers())) {
         this.getEntity().setAnimation(layer, this.configurator.apply(Animations.NULL.configure()));
      }
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).build();
   }
}

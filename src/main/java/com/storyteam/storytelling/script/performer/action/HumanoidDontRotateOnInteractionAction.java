package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;

public class HumanoidDontRotateOnInteractionAction<T extends HumanoidEntity> extends EntityPerformerAction<T> {
   private final boolean dontRotateOnInteraction;

   public HumanoidDontRotateOnInteractionAction(ScriptPerformer performer, Character<T> character, boolean dontRotateOnInteraction) {
      super(performer, character);
      this.dontRotateOnInteraction = dontRotateOnInteraction;
   }

   @Override
   public void tick() {
      this.getEntity().getData().setDontRotateOnInteraction(true);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().setDontRotateOnInteraction(false);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setDontRotateOnInteraction(true);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("lockHead", this.dontRotateOnInteraction).build();
   }
}

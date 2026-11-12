package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;

public class HumanoidBlockAnimWalkingBodyRotationAction<T extends AnimatedEntity> extends EntityPerformerAction<T> {
   private final boolean blockAnimRotateWalking;

   public HumanoidBlockAnimWalkingBodyRotationAction(ScriptPerformer performer, Character<T> character, boolean blockAnimRotateWalking) {
      super(performer, character);
      this.blockAnimRotateWalking = blockAnimRotateWalking;
   }

   @Override
   public void tick() {
      this.getEntity().blockAnimRotateWalking(this.blockAnimRotateWalking);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().blockAnimRotateWalking(this.blockAnimRotateWalking);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("blockAnimRotateWalking", this.blockAnimRotateWalking).build();
   }
}

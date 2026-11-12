package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;

public class HumanoidBlockBodyRotationAction<T extends AnimatedEntity> extends EntityPerformerAction<T> {
   private final boolean blockBody;

   public HumanoidBlockBodyRotationAction(ScriptPerformer performer, Character<T> character, boolean blockBody) {
      super(performer, character);
      this.blockBody = blockBody;
   }

   @Override
   public void tick() {
      this.getEntity().blockBody(this.blockBody);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().blockBody(!this.blockBody);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().blockBody(this.blockBody);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("blockBody", this.blockBody).build();
   }
}

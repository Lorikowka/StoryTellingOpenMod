package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;

public class HumanoidRestrictHeadRotationAction<T extends AnimatedEntity> extends EntityPerformerAction<T> {
   private final boolean lockHead;

   public HumanoidRestrictHeadRotationAction(ScriptPerformer performer, Character<T> character, boolean lockHead) {
      super(performer, character);
      this.lockHead = lockHead;
   }

   @Override
   public void tick() {
      this.getEntity().lockHead(this.lockHead);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().lockHead(!this.lockHead);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().lockHead(this.lockHead);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("lockHead", this.lockHead).build();
   }
}

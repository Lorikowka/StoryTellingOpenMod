package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;

public class AnimatedSetTraversalNavigation<T extends AnimatedEntity> extends EntityPerformerAction<T> {
   private final boolean traversalNavigation;

   public AnimatedSetTraversalNavigation(ScriptPerformer performer, Character<T> character, boolean traversalNavigation) {
      super(performer, character);
      this.traversalNavigation = traversalNavigation;
   }

   @Override
   public void tick() {
      this.getEntity().traversalNavigation(this.traversalNavigation);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().traversalNavigation(!this.traversalNavigation);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().traversalNavigation(this.traversalNavigation);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("traversalNavigation", this.traversalNavigation).build();
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;

public class HumanoidIgnoreNavmeshAction<T extends HumanoidEntity> extends EntityPerformerAction<T> {
   private final boolean ignore;

   public HumanoidIgnoreNavmeshAction(ScriptPerformer performer, Character<T> character, boolean ignore) {
      super(performer, character);
      this.ignore = ignore;
   }

   @Override
   public void tick() {
      this.getEntity().ignoreNavmesh(this.ignore);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().ignoreNavmesh(!this.ignore);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().ignoreNavmesh(this.ignore);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("ignore", this.ignore).build();
   }
}

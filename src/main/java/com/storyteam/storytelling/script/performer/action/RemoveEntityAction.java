package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import net.minecraft.world.entity.Entity;

public class RemoveEntityAction<T extends Entity> extends EntityPerformerAction<T> {
   public RemoveEntityAction(ScriptPerformer performer, Character<T> character) {
      super(performer, character);
   }

   @Override
   public void tick() {
      this.getOrCreateEntity().markForDelete();
   }

   @Override
   public void fastForwardToStage() {
      this.getOrCreateEntity().markForDelete();
   }

   @Override
   public void rewindToStage() {
      this.getEntity().markForDelete();
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntitySitBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import net.minecraft.world.entity.Entity;

public class EntitySitAction<T extends Entity> extends EntityPerformerAction<T> {
   public EntitySitAction(ScriptPerformer performer, Character<T> character) {
      super(performer, character);
   }

   @Override
   public void tick() {
      EntityState<T> entity = this.getEntity();
      entity.addBehaviour(new EntitySitBehaviour<>());
   }

   @Override
   public void fastForwardToStage() {
      EntityState<T> entity = this.getEntity();
      entity.addBehaviour(new EntitySitBehaviour<>());
      entity.markForReposition();
   }

   @Override
   public void rewindToStage() {
      this.getEntity().stopBehaviour(BehaviourType.MOVEMENT);
   }
}

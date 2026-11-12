package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.CatmullRomMovementBehavior;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.world.Location;

public class CatmullRomMoveAction<T extends AnimatedEntity> extends BehaviourAction<T, CatmullRomMovementBehavior<T>> {
   private Location[] locations;
   private int time;

   public CatmullRomMoveAction(ScriptPerformer performer, Character<T> character, int time, Location... locations) {
      super(performer, character);
      this.time = time;
      this.locations = locations;
   }

   @Override
   public void fastForwardToStage() {
      if (this.behavior != null) {
         this.optionalState().ifPresent(s -> s.stopBehaviour(this.behavior.getType()));
      }

      EntityState<T> entity = this.getEntity();
      Location l = this.locations[this.locations.length - 1];
      entity.setPosition(l, true);
      entity.markForReposition();
   }

   public CatmullRomMovementBehavior<T> createBehaviour() {
      return new CatmullRomMovementBehavior<>(this.time, this.locations);
   }
}

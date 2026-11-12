package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityFollowPlayerBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Mob;

public class EntityFollowPlayerAction<T extends Mob> extends BehaviourAction<T, EntityFollowPlayerBehaviour<T>> {
   private final double speed;
   private final double minDistance;
   private final double maxDistance;

   public EntityFollowPlayerAction(ScriptPerformer performer, Character<T> character, double speed, double minDistance, double maxDistance) {
      super(performer, character);
      this.speed = speed;
      this.minDistance = minDistance;
      this.maxDistance = maxDistance;
   }

   public EntityFollowPlayerBehaviour<T> createBehaviour() {
      return new EntityFollowPlayerBehaviour<>(this.speed, this.minDistance, this.maxDistance);
   }

   @Override
   public void rewindToStage() {
      super.rewindToStage();
      this.getEntity().setPosition(null);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("character", this.character)
         .append("speed", this.speed)
         .append("minDistance", this.minDistance)
         .append("maxDistance", this.maxDistance)
         .build();
   }

   @Override
   public boolean isDone() {
      return true;
   }
}

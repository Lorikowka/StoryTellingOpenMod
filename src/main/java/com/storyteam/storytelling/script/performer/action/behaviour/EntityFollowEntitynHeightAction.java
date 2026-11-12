package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityFollowEntityOnHeightBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Mob;

public class EntityFollowEntitynHeightAction<T extends Mob> extends BehaviourAction<T, EntityFollowEntityOnHeightBehaviour<T>> {
   private final double speed;
   private final double minDistance;
   private final boolean endOnArrival;
   int heightFollow;

   public EntityFollowEntitynHeightAction(
      ScriptPerformer performer, Character<T> character, double speed, double minDistance, boolean endOnArrival, int heightFollow
   ) {
      super(performer, character);
      this.speed = speed;
      this.minDistance = minDistance;
      this.endOnArrival = endOnArrival;
      this.heightFollow = heightFollow;
   }

   public EntityFollowEntityOnHeightBehaviour<T> createBehaviour() {
      return new EntityFollowEntityOnHeightBehaviour<>(this.speed, this.minDistance, this.endOnArrival, this.heightFollow);
   }

   @Override
   public boolean isDone() {
      return super.isDone() | !this.endOnArrival;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("character", this.character)
         .append("minDistance", this.minDistance)
         .append("endOnArrival", this.endOnArrival)
         .build();
   }
}

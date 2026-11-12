package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityFollowEntityBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Mob;

public class EntityFollowEntityAction<T extends Mob> extends BehaviourAction<T, EntityFollowEntityBehaviour<T>> {
   private final Character<?> toFollow;
   private final double speed;
   private final double minDistance;
   private final boolean endOnArrival;

   public EntityFollowEntityAction(
      ScriptPerformer performer, Character<T> character, Character<?> toFollow, double speed, double minDistance, boolean endOnArrival
   ) {
      super(performer, character);
      this.toFollow = toFollow;
      this.speed = speed;
      this.minDistance = minDistance;
      this.endOnArrival = endOnArrival;
   }

   public EntityFollowEntityBehaviour<T> createBehaviour() {
      return new EntityFollowEntityBehaviour<>(this.speed, this.toFollow, this.minDistance, this.endOnArrival);
   }

   @Override
   public boolean isDone() {
      return super.isDone() | !this.endOnArrival;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("character", this.character)
         .append("toFollow", this.toFollow)
         .append("minDistance", this.minDistance)
         .append("endOnArrival", this.endOnArrival)
         .build();
   }
}

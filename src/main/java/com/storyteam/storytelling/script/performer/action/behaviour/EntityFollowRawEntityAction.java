package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityFollowRawEntityBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;

public class EntityFollowRawEntityAction<T extends PathfinderMob> extends BehaviourAction<T, EntityFollowRawEntityBehaviour<T>> {
   private final Supplier<Entity> toFollow;
   private final double speed;
   private final double minDistance;
   private final boolean endOnArrival;

   public EntityFollowRawEntityAction(
      ScriptPerformer performer, Character<T> character, Supplier<Entity> toFollow, double speed, double minDistance, boolean endOnArrival
   ) {
      super(performer, character);
      this.toFollow = toFollow;
      this.speed = speed;
      this.minDistance = minDistance;
      this.endOnArrival = endOnArrival;
   }

   public EntityFollowRawEntityBehaviour<T> createBehaviour() {
      return new EntityFollowRawEntityBehaviour(this.speed, this.toFollow, this.minDistance, this.endOnArrival);
   }

   @Override
   public boolean isDone() {
      return super.isDone() | this.endOnArrival;
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

package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityFollowVectorBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class EntityFollowVectorAction<T extends Mob> extends BehaviourAction<T, EntityFollowVectorBehaviour<T>> {
   private final Supplier<Vec3> toFollow;
   private final double speed;
   private final double minDistance;
   private final boolean endOnArrival;

   public EntityFollowVectorAction(
      ScriptPerformer performer, Character<T> character, Supplier<Vec3> toFollow, double speed, double minDistance, boolean endOnArrival
   ) {
      super(performer, character);
      this.toFollow = toFollow;
      this.speed = speed;
      this.minDistance = minDistance;
      this.endOnArrival = endOnArrival;
   }

   public EntityFollowVectorBehaviour<T> createBehaviour() {
      return new EntityFollowVectorBehaviour<>(this.speed, this.toFollow, this.minDistance, this.endOnArrival);
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

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityRandomWalkingBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.PathfinderMob;

public class EntityStartRandomWalkingAction<T extends PathfinderMob> extends EntityPerformerAction<T> {
   private final double speed;
   private final int maxPoints;
   private final double maxDistance;

   public EntityStartRandomWalkingAction(ScriptPerformer performer, Character<T> character, double speed, int maxPoints, double maxDistance) {
      super(performer, character);
      this.speed = speed;
      this.maxPoints = maxPoints;
      this.maxDistance = maxDistance;
   }

   @Override
   public void tick() {
      this.getEntity().addBehaviour(new EntityRandomWalkingBehaviour<>(this.speed, this.maxPoints, this.maxDistance));
   }

   @Override
   public void fastForwardToStage() {
      EntityState<T> entity = this.getEntity();
      entity.addBehaviour(new EntityRandomWalkingBehaviour<>(this.speed, this.maxPoints, this.maxDistance));
      entity.markForReposition();
   }

   @Override
   public void rewindToStage() {
      EntityState<T> entity = this.getEntity();
      entity.stopBehaviour(BehaviourType.MOVEMENT);
      entity.markForReposition();
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("character", this.character)
         .append("speed", this.speed)
         .append("maxPoints", this.maxPoints)
         .append("maxDistance", this.maxDistance)
         .build();
   }
}

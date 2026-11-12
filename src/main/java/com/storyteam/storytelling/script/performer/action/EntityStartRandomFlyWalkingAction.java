package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityRandomFlyWalkingBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class EntityStartRandomFlyWalkingAction<T extends PathfinderMob> extends EntityPerformerAction<T> {
   private final double speed;
   private final Vec3 start_loc;
   int minHeight;
   int maxHeight;
   int minDistance;
   int maxDistance;

   public EntityStartRandomFlyWalkingAction(
      ScriptPerformer performer, Character<T> character, double speed, String start_pos, int minHeight, int maxHeight, int minDistance, int maxDistance
   ) {
      super(performer, character);
      this.speed = speed;
      this.start_loc = new Location(start_pos);
      this.minHeight = minHeight;
      this.maxHeight = maxHeight;
      this.minDistance = minDistance;
      this.maxDistance = maxDistance;
   }

   @Override
   public void tick() {
      this.getEntity()
         .addBehaviour(new EntityRandomFlyWalkingBehaviour<>(this.speed, this.start_loc, this.minHeight, this.maxHeight, this.minDistance, this.maxDistance));
   }

   @Override
   public void fastForwardToStage() {
      EntityState<T> entity = this.getEntity();
      entity.addBehaviour(new EntityRandomFlyWalkingBehaviour<>(this.speed, this.start_loc, this.minHeight, this.maxHeight, this.minDistance, this.maxDistance));
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
      return ToStringBuilder.builder(this).append("character", this.character).append("speed", this.speed).build();
   }
}

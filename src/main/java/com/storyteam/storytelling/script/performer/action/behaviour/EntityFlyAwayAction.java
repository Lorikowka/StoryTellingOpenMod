package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityFlyAwayBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;

public class EntityFlyAwayAction<T extends Mob> extends BehaviourAction<T, EntityFlyAwayBehaviour<T>> {
   private final Supplier<List<Location>> positions;
   private final double speed;
   private final boolean randomWalk;

   public EntityFlyAwayAction(ScriptPerformer performer, Character<T> character, double speed, boolean randomWalk, Supplier<List<Location>> path) {
      super(performer, character);
      this.speed = speed;
      this.positions = path;
      this.randomWalk = randomWalk;
   }

   @Override
   public boolean isDone() {
      return super.init;
   }

   @Override
   public void reset() {
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().markForDelete();
   }

   public EntityFlyAwayBehaviour<T> createBehaviour() {
      return new EntityFlyAwayBehaviour<>(this.speed, this.randomWalk, this.positions.get());
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("speed", this.speed).append("randomWalk", this.randomWalk).build();
   }
}

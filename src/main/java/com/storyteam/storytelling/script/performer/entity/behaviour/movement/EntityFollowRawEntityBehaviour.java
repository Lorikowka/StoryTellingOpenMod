package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.performer.entity.behaviour.utils.ISightBehaviour;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class EntityFollowRawEntityBehaviour<T extends Mob> extends EntityPathBehaviour<T> implements ISightBehaviour {
   private final Supplier<Entity> toFollow;
   private final double minDistance;
   private final boolean endOnArrival;

   public EntityFollowRawEntityBehaviour(double speed, Supplier<Entity> toFollow, double minDistance, boolean endOnArrival) {
      super(speed);
      this.toFollow = toFollow;
      this.minDistance = minDistance;
      this.endOnArrival = endOnArrival;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      PathNavigation navigator = ((Mob)context.getEntity()).getNavigation();
      Optional.ofNullable(this.toFollow.get()).ifPresent(target -> {
         if (((Mob)context.getEntity()).distanceToSqr(target) < this.minDistance * this.minDistance) {
            navigator.stop();
            if (this.endOnArrival) {
               context.getState().setPosition(new Location(context.getEntity()));
            }
         } else {
            navigator.moveTo(target, this.speed);
         }
      });
   }
}

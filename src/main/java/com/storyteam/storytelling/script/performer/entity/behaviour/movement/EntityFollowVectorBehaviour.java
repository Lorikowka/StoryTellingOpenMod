package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.performer.entity.behaviour.utils.ISightBehaviour;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;

public class EntityFollowVectorBehaviour<T extends Mob> extends EntityPathBehaviour<T> implements ISightBehaviour {
   private final Supplier<Vec3> toFollow;
   private final double minDistance;
   private final boolean endOnArrival;
   private boolean finished;

   public EntityFollowVectorBehaviour(double speed, Supplier<Vec3> toFollow, double minDistance, boolean endOnArrival) {
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
               this.finished = true;
            }
         } else {
            navigator.moveTo(target.x, target.y, target.z, this.speed);
         }
      });
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      context.getState().setPosition(new Location(context.getEntity()));
      ((Mob)context.getEntity()).getNavigation().stop();
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return super.canStillUse(env, context) && (!this.endOnArrival || !this.finished);
   }
}

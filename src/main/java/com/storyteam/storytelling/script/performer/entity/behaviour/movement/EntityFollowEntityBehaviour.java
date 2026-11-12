package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.performer.entity.behaviour.utils.ISightBehaviour;
import com.storyteam.storytelling.script.performer.entity.look.targets.EntityTarget;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class EntityFollowEntityBehaviour<T extends Mob> extends EntityPathBehaviour<T> implements ISightBehaviour {
   private Character<?> toFollow;
   private Entity toFollowEntity;
   private final double minDistance;
   private final boolean endOnArrival;
   private boolean finished;

   public EntityFollowEntityBehaviour(double speed, Character<?> toFollow, double minDistance, boolean endOnArrival) {
      super(speed);
      this.toFollow = toFollow;
      this.minDistance = minDistance;
      this.endOnArrival = endOnArrival;
   }

   public EntityFollowEntityBehaviour(double speed, Entity toFollow, double minDistance, boolean endOnArrival) {
      super(speed);
      this.toFollowEntity = toFollow;
      this.minDistance = minDistance;
      this.endOnArrival = endOnArrival;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      PathNavigation navigator = ((Mob)context.getEntity()).getNavigation();
      Entity target = this.toFollowEntity == null ? env.server.getEntityRepository().getEntity((Character<Entity>)this.toFollow) : this.toFollowEntity;
      if (target != null && (((Mob)context.getEntity()).distanceToSqr(target) < this.minDistance * this.minDistance || !navigator.moveTo(target, this.speed))) {
         navigator.stop();
         if (this.endOnArrival) {
            this.finished = true;
         }
      }
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

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      super.onStart(env, context);
      Entity target = this.toFollowEntity == null ? env.server.getEntityRepository().getEntity((Character<Entity>)this.toFollow) : this.toFollowEntity;
      context.getState().getData().setLookTarget(new EntityTarget(target));
   }
}

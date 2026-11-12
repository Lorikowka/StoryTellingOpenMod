package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.performer.entity.behaviour.utils.ISightBehaviour;
import com.storyteam.storytelling.script.performer.entity.look.targets.PlayerTarget;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;

public class EntityFollowEntityOnHeightBehaviour<T extends Mob> extends EntityPathBehaviour<T> implements ISightBehaviour {
   private final double minDistance;
   private final boolean endOnArrival;
   private boolean finished;
   int heightFollow;

   public EntityFollowEntityOnHeightBehaviour(double speed, double minDistance, boolean endOnArrival, int heightFollow) {
      super(speed);
      this.minDistance = minDistance;
      this.endOnArrival = endOnArrival;
      this.heightFollow = heightFollow;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      PathNavigation navigator = ((Mob)context.getEntity()).getNavigation();
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         Vec3 target_pos = player.position().add(0.0, (double)this.heightFollow, 0.0);
         Vec3 pos = new Vec3(((Mob)context.getEntity()).position().x(), player.position().y(), ((Mob)context.getEntity()).position().z());
         if (pos.distanceToSqr(player.position()) < this.minDistance * this.minDistance
            || !navigator.moveTo(target_pos.x, target_pos.y, target_pos.z, this.speed)) {
            navigator.stop();
            if (this.endOnArrival) {
               this.finished = true;
            }
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
      context.getState().getData().setLookTarget(new PlayerTarget());
   }
}

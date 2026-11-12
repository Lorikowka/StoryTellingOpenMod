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

public class EntityFollowPlayerBehaviour<T extends Mob> extends EntityPathBehaviour<T> implements ISightBehaviour {
   private final double minDistance;
   private final double maxDistance;

   public EntityFollowPlayerBehaviour(double speed, double minDistance, double maxDistance) {
      super(speed);
      this.minDistance = minDistance;
      this.maxDistance = maxDistance;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      ServerPlayer player = ScriptUtils.getPlayer();
      PathNavigation navigator = ((Mob)context.getEntity()).getNavigation();
      if (player != null) {
         context.getState().setPosition(new Location(player));
         if (((Mob)context.getEntity()).distanceToSqr(player) < this.minDistance * this.minDistance) {
            navigator.stop();
         } else if (((Mob)context.getEntity()).distanceToSqr(player) > this.maxDistance * this.maxDistance) {
            ((Mob)context.getEntity()).teleportTo(player.getX(), player.getY(), player.getZ());
         } else {
            navigator.moveTo(player, this.speed);
         }
      }
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      super.onStart(env, context);
      context.getState().getData().setLookTarget(new PlayerTarget());
   }
}

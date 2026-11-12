package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class EntityFlyAwayBehaviour<T extends Mob> extends EntityPathBehaviour<T> {
   private final List<Location> positions;
   private List<Location> positionsLeft;
   private boolean isOnTheWay = false;
   private final boolean randomWalk;

   public EntityFlyAwayBehaviour(double speed, boolean randomWalk, List<Location> positions) {
      super(speed);
      this.randomWalk = randomWalk;
      this.positions = positions;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (ScriptUtils.getAngle(player, context.getEntity()) > 90.0 && ((Mob)context.getEntity()).distanceTo(player) > 8.0F
         || ((Mob)context.getEntity()).distanceTo(player) > 50.0F
         || !((Mob)context.getEntity()).level().dimension().location().equals(player.level().dimension().location())) {
         context.getState().markForDelete();
      }

      if (!this.isOnTheWay && this.positions != null && !this.positions.isEmpty()) {
         this.isOnTheWay = true;
         float var10001 = (float)this.speed;
         context.getRef().navigator.moveByPath(var10001, this.positions.toArray(new Location[0]));
      }

      if (context.getRef().navigator.currentJob != null) {
         Location pos = context.getRef().navigator.currentJob.positionsLeft.get(context.getRef().navigator.currentJob.positionsLeft.size() - 1);
         if (pos != null && this.positionsLeft != null && !this.positionsLeft.isEmpty()) {
            if (pos.distanceTo(this.positionsLeft.get(this.positionsLeft.size() - 1)) > 2.0) {
               float var6 = (float)this.speed;
               context.getRef().navigator.moveByPath(var6, this.positionsLeft.toArray(new Location[0]));
            } else {
               this.positionsLeft = context.getRef().navigator.currentJob.positionsLeft;
            }
         }
      }

      if (context.getRef().navigator.currentJob == null) {
         if (this.randomWalk) {
            Vec3 pos = ((Mob)context.getEntity()).getLookAngle().multiply(1.0, -1.0, 1.0).normalize().scale(40.0);
            ((Mob)context.getEntity())
               .getNavigation()
               .moveTo(
                  ((Mob)context.getEntity()).getX() + pos.x, ((Mob)context.getEntity()).getY() + 16.0, ((Mob)context.getEntity()).getZ() + pos.z, this.speed
               );
         } else {
            context.getState().markForDelete();
         }
      }
   }
}

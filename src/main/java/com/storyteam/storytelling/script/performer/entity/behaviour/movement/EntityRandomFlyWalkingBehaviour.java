package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class EntityRandomFlyWalkingBehaviour<T extends PathfinderMob> extends EntityPathBehaviour<T> {
   Vec3 start_loc = Vec3.ZERO;
   int minHeight;
   int maxHeight;
   int minDistance;
   int maxDistance;
   Random rnd = new Random();

   public EntityRandomFlyWalkingBehaviour(double speed, Vec3 start_loc, int minHeight, int maxHeight, int minDistance, int maxDistance) {
      super(speed);
      this.start_loc = start_loc;
      this.minHeight = minHeight;
      this.maxHeight = maxHeight;
      this.minDistance = minDistance;
      this.maxDistance = maxDistance;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      PathNavigation navigator = ((PathfinderMob)context.getEntity()).getNavigation();
      if (((PathfinderMob)context.getEntity()).tickCount % 600 == 0) {
         Vec3 target = this.findAirPosNearStartPos(
            (PathfinderMob)context.getEntity(), this.start_loc, this.minHeight, this.maxHeight, this.minDistance, this.maxDistance
         );
         if (target == null || target == Vec3.ZERO) {
            return;
         }

         context.getState().getData().setLookTarget(null);
         context.getRef().navigator.moveByPath((float)this.speed, new Location(target));
      }
   }

   private Vec3 findAirPosNearStartPos(PathfinderMob mob, Vec3 start_loc, int minHeight, int maxHeight, int minDistance, int maxDistance) {
      Level world = mob.level();
      Vec3 resultPos = null;
      int attempts = 100;

      while (attempts-- > 0) {
         double distance = (double)minDistance + this.rnd.nextDouble() * (double)(maxDistance - minDistance);
         double angle = this.rnd.nextDouble() * 2.0 * Math.PI;
         double offsetX = Math.cos(angle) * distance;
         double offsetZ = Math.sin(angle) * distance;
         double offsetY = (double)minHeight + this.rnd.nextDouble() * (double)(maxHeight - minHeight);
         Vec3 potentialPos = start_loc.add(offsetX, offsetY, offsetZ);
         if (this.isAirSpace(world, potentialPos)) {
            resultPos = potentialPos;
            Path path = mob.getNavigation().createPath(new BlockPos((int)potentialPos.x(), (int)potentialPos.y(), (int)potentialPos.z()), 60);
            if (path != null && path.canReach()) {
               break;
            }
         }
      }

      return resultPos != null ? resultPos : Vec3.ZERO;
   }

   private boolean isAirSpace(Level world, Vec3 pos) {
      BlockPos blockPos = new BlockPos((int)pos.x, (int)pos.y, (int)pos.z);
      return world.isEmptyBlock(blockPos);
   }
}

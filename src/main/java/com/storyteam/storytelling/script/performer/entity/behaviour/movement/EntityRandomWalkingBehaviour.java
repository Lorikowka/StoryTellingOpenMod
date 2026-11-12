package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class EntityRandomWalkingBehaviour<T extends PathfinderMob> extends EntityPathBehaviour<T> {
   private final int maxPoints;
   private final double maxDistance;

   public EntityRandomWalkingBehaviour(double speed, int maxPoints, double maxDistance) {
      super(speed);
      this.maxPoints = maxPoints;
      this.maxDistance = maxDistance;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      PathNavigation navigator = ((PathfinderMob)context.getEntity()).getNavigation();
      Random random = new Random();
      int tries = 10;

      while (navigator.isDone() && tries-- > 0) {
         if (random.nextInt(20) == 0) {
            Vec3 target = LandRandomPos.getPos((PathfinderMob)context.getEntity(), this.maxPoints * 2 + 1 - this.maxPoints, 1);
            if (target == null) {
               return;
            }

            Path path = navigator.createPath(BlockPos.containing(target), 0);
            if (path != null && path.canReach() && path.getNodeCount() <= this.maxPoints) {
               Node end = path.getEndNode();
               if (end == null || context.getState().getPos().closerThan(Vec3.atBottomCenterOf(end.asBlockPos()), this.maxDistance)) {
                  navigator.moveTo(path, this.speed);
                  break;
               }
            }
         }
      }
   }
}

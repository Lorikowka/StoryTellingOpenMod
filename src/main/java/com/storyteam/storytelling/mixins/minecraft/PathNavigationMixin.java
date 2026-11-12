package com.storyteam.storytelling.mixins.minecraft;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({PathNavigation.class})
public abstract class PathNavigationMixin {
   @Shadow
   protected float maxDistanceToWaypoint;
   @Shadow
   @Final
   protected Mob mob;
   @Shadow
   @Nullable
   protected Path path;

   @Shadow
   protected abstract Vec3 getTempMobPos();

   @Shadow
   protected abstract boolean shouldTargetNextNodeInDirection(Vec3 var1);

   @Shadow
   protected abstract void doStuckDetection(Vec3 var1);

   @Shadow
   public abstract boolean canCutCorner(BlockPathTypes var1);

   @Overwrite
   protected void followThePath() {
      Vec3 Vec3 = this.getTempMobPos();
      this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;
      BlockPos vector3i = this.path.getNextNodePos();
      double d0 = Math.abs(this.mob.getX() - ((double)vector3i.getX() + 0.5));
      double d1 = Math.abs(this.mob.getY() - (double)vector3i.getY());
      double d2 = Math.abs(this.mob.getZ() - ((double)vector3i.getZ() + 0.5));
      boolean flag = d0 <= (double)this.maxDistanceToWaypoint && d2 <= (double)this.maxDistanceToWaypoint && d1 <= 1.0;
      if (flag || this.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(Vec3)) {
         this.path.advance();
      }

      this.doStuckDetection(Vec3);
   }
}

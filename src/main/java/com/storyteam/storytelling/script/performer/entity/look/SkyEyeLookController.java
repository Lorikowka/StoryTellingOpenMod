package com.storyteam.storytelling.script.performer.entity.look;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import com.storyteam.storytelling.content.entities.SkyEyeEntity;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SkyEyeLookController<T extends Entity> extends AbstractLookController<T> {
   public int headStableTime;
   private float lastStableYHeadRot;

   @Override
   public void lookAtTarget(EntityContext<T> context) {
      if (this.target != null) {
         Vec3 target = this.target.getTarget(context);
         T entity = context.getEntity();
         if (entity instanceof SkyEyeEntity eye) {
            if (target != null) {
               eye.getGazeSystem().setGazePosition(target);
               if (Math.abs(eye.yHeadRot - this.lastStableYHeadRot) > 15.0F) {
                  this.headStableTime = 0;
                  this.lastStableYHeadRot = eye.yHeadRot;
               } else {
                  this.headStableTime++;
               }

               Vec3 newTarget = this.fixVec(entity, target);
               eye.getLookControl().setLookAt(newTarget.x, newTarget.y, newTarget.z, 1.5F, 180.0F);
               if (this.endWork) {
                  this.onRotationEnd.forEach(c -> c.accept(context));
                  this.onRotationEnd.clear();
               }
            }
         }
      }
   }

   @Override
   public void lookAtInstantTarget(EntityContext<T> context) {
      if (this.target != null) {
         Vec3 t = this.target.getTarget(context);
         T mob = context.getEntity();
         if (t != null) {
            t = this.fixVec(mob, t);
            mob.lookAt(Anchor.FEET, t);
            mob.lookAt(Anchor.EYES, t);
            if (mob instanceof IGazingEntity ge) {
               ge.getGazeSystem().setGazePosition(t);
            }
         }
      }
   }

   private Vec3 fixVec(T ent, Vec3 target) {
      if (ent instanceof SkyEyeEntity eye) {
         Vec3 eyePos = eye.getRenderPos();
         Vec3 dir = target.subtract(eyePos);
         Vec3 ePos = eye.position();
         return ePos.add(dir);
      } else {
         return target;
      }
   }
}

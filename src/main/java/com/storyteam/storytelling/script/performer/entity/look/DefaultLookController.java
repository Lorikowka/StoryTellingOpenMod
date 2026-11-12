package com.storyteam.storytelling.script.performer.entity.look;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class DefaultLookController<T extends Entity> extends AbstractLookController<T> {
   @Override
   public void lookAtTarget(EntityContext<T> context) {
      if (this.target != null) {
         Vec3 t = this.target.getTarget(context);
         T ent = context.getEntity();
         if (t != null) {
            if (ent instanceof Mob mob) {
               if (this.isQuick) {
                  this.lookAtInstantTarget(context);
                  this.endWork = true;
               } else {
                  mob.getLookControl().setLookAt(t);
                  this.checkLooking(ent, context.getState(), t);
               }
            } else {
               this.endWork = true;
            }

            if (this.endWork) {
               this.onRotationEnd.forEach(c -> c.accept(context));
               this.onRotationEnd.clear();
            }

            if (ent instanceof IGazingEntity ge) {
               ge.getGazeSystem().setGazePosition(t);
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
            mob.lookAt(Anchor.FEET, t);
            mob.lookAt(Anchor.EYES, t);
            if (mob instanceof IGazingEntity ge) {
               ge.getGazeSystem().setGazePosition(t);
            }
         }
      }
   }

   protected void checkLooking(T ent, EntityState<T> state, Vec3 target) {
      if (ent instanceof Mob mob) {
         Vec3 lookPos = target.subtract(mob.position()).multiply(1.0, 0.0, 1.0).normalize();
         Vec3 lookAngle = this.calculateViewVector(mob.getXRot(), state.getData().isBlockBody() ? mob.yHeadRot : mob.yBodyRot)
            .multiply(1.0, 0.0, 1.0)
            .normalize();
         this.endWork = Math.abs(lookPos.x - lookAngle.x) < 0.17 && Math.abs(lookPos.z - lookAngle.z) < 0.17;
      } else {
         this.endWork = true;
      }
   }
}

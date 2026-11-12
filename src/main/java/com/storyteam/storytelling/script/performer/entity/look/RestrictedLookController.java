package com.storyteam.storytelling.script.performer.entity.look;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import com.storyteam.storytelling.util.Necr0manthUtils;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class RestrictedLookController<T extends LivingEntity> extends AbstractLookController<T> {
   public double initialAngle = Double.NaN;
   public double maxBodyAngle = 50.0;
   public double maxHeadAngle = 110.0;
   public double rotationSpeed = 0.3;

   public RestrictedLookController() {
   }

   public RestrictedLookController(double maxBodyAngle, double maxHeadAngle) {
      this.maxBodyAngle = maxBodyAngle;
      this.maxHeadAngle = maxHeadAngle;
   }

   public RestrictedLookController(double maxBodyAngle, double maxHeadAngle, double rotationSpeed) {
      this(maxBodyAngle, maxHeadAngle);
      this.rotationSpeed = rotationSpeed;
   }

   public RestrictedLookController(double maxBodyAngle, double maxHeadAngle, double rotationSpeed, double initialAngle) {
      this(maxBodyAngle, maxHeadAngle, rotationSpeed);
      this.initialAngle = initialAngle;
   }

   @Override
   public void lookAtTarget(EntityContext<T> context) {
      if (Double.isNaN(this.initialAngle)) {
         this.initialAngle = (double)((LivingEntity)context.getEntity()).yHeadRot;
      }

      double initialAngle = this.initialAngle * (float) (Math.PI / 180.0);
      double maxBodyAngle = this.maxBodyAngle * (float) (Math.PI / 180.0);
      double maxHeadAngle = this.maxHeadAngle * (float) (Math.PI / 180.0);
      T entity = (T)context.getEntity();
      Vec3 targetPosition = this.target.getTarget(context);
      if (targetPosition != null) {
         Vec3 targetDirection = targetPosition.subtract(entity.getEyePosition());
         targetDirection = targetDirection.scale(1.0 / new Vec3(targetDirection.x, 0.0, targetDirection.z).length());
         double targetY = targetDirection.y;
         double targetAngle = -Math.atan2(targetDirection.x, targetDirection.z);
         double localTargetAngle = Necr0manthUtils.normalizeAngleMinusPiPi(targetAngle - initialAngle);
         float currentAngle = Necr0manthUtils.normalizeAngleMinusPiPi(entity.yHeadRot * (float) (Math.PI / 180.0));
         double currentLocalAngle = Necr0manthUtils.normalizeAngleMinusPiPi((double)currentAngle - initialAngle);
         double localBodyAngle = Necr0manthUtils.normalizeAngleMinusPiPi((double)(entity.yBodyRot * (float) (Math.PI / 180.0)) - initialAngle);
         context.getState().blockBody(Math.abs(currentLocalAngle) > maxBodyAngle && Math.abs(localBodyAngle) >= maxBodyAngle);
         double localTargetAngle1 = 0.0;
         if (localTargetAngle * currentLocalAngle >= 0.0 || Math.abs(currentLocalAngle) < 0.1) {
            if (Math.abs(localTargetAngle) > maxHeadAngle) {
               localTargetAngle1 = Math.signum(localTargetAngle) * maxHeadAngle;
            } else {
               localTargetAngle1 = localTargetAngle;
            }
         }

         double targetXRot = -Math.atan2(targetY, 1.0);
         float currentXRot = entity.xRotO * (float) (Math.PI / 180.0);
         double d = Math.sqrt(
            (localTargetAngle1 - currentLocalAngle) * (localTargetAngle1 - currentLocalAngle)
               + (targetXRot - (double)currentXRot) * (targetXRot - (double)currentXRot)
         );
         double yRotCoeff = Math.abs(localTargetAngle1 - currentLocalAngle) / d;
         double xRotCoeff = Math.abs(targetXRot - (double)currentXRot) / d;
         float newYrot = (float)(
            (double)currentAngle
               + Math.signum(localTargetAngle1 - currentLocalAngle) * Math.min(Math.abs(localTargetAngle1 - currentLocalAngle), yRotCoeff * this.rotationSpeed)
         );
         float newXrot = (float)(
            (double)currentXRot
               + Math.signum(targetXRot - (double)currentXRot) * Math.min(Math.abs(targetXRot - (double)currentXRot), xRotCoeff * this.rotationSpeed)
         );
         if (entity instanceof Mob mob) {
            mob.getLookControl().setLookAt(new Vec3(0.0, 0.0, 1.0).xRot(-newXrot).yRot(-newYrot).add(entity.getEyePosition()));
         } else {
            entity.setYRot(entity.yHeadRotO = entity.yHeadRot = entity.yRotO = newYrot);
            entity.setXRot(newXrot);
         }

         if (Math.abs(localTargetAngle1 - currentLocalAngle) < 0.3) {
            this.endWork = true;
            this.onRotationEnd.forEach(c -> c.accept(context));
            this.onRotationEnd.clear();
         }

         if (entity instanceof IGazingEntity ge) {
            ge.getGazeSystem().setGazePosition(targetPosition);
         }
      }
   }

   @Override
   public void lookAtInstantTarget(EntityContext<T> context) {
      if (this.target != null) {
         Vec3 t = this.target.getTarget(context);
         T mob = (T)context.getEntity();
         if (t != null) {
            mob.lookAt(Anchor.FEET, t);
            mob.lookAt(Anchor.EYES, t);
            if (mob instanceof IGazingEntity ge) {
               ge.getGazeSystem().setGazePosition(t);
            }
         }
      }
   }
}

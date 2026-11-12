package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.look.targets.PositionTarget;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityMovementNoPhysicsBehaviour<T extends Entity> implements ISpeedyBehavior<T> {
   final double speed;
   final Supplier<Location> position;
   Vec3 end;
   boolean rotate;

   public EntityMovementNoPhysicsBehaviour(double speed, Supplier<Location> loc, boolean rotate) {
      this.speed = speed;
      this.position = loc;
      this.rotate = rotate;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      Vec3 start = context.getEntity().position();
      context.getEntity().setDeltaMovement(this.end.subtract(start).normalize().scale(this.speed));
      if (this.rotate) {
         double d0 = this.end.x - start.x;
         double d1 = this.end.z - start.z;
         float f9 = (float)(Mth.atan2(d1, d0) * 180.0F / (float)Math.PI) - 90.0F;
         float y = this.rotlerp(context.getEntity().getYRot(), f9);
         context.getEntity().setYRot(y);
      }
   }

   protected float rotlerp(float pSourceAngle, float pTargetAngle) {
      float f = Mth.wrapDegrees(pTargetAngle - pSourceAngle);
      if (f > 90.0F) {
         f = 90.0F;
      }

      if (f < -90.0F) {
         f = -90.0F;
      }

      float f1 = pSourceAngle + f;
      if (f1 < 0.0F) {
         f1 += 360.0F;
      } else if (f1 > 360.0F) {
         f1 -= 360.0F;
      }

      return f1;
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      this.end = this.position.get();
      context.getEntity().noPhysics = true;
      context.getState().getData().setLookTarget(null);
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      context.getEntity().setDeltaMovement(Vec3.ZERO);
      context.getEntity().noPhysics = false;
      if (this.rotate && this.position.get() != null && this.position.get().hasRotation()) {
         context.getState()
            .getData()
            .setLookTarget(new PositionTarget<>(new Location(Vec3.directionFromRotation(this.position.get().getPitch(), this.position.get().getYaw())), true));
      }
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return context.getEntity().position().distanceTo(this.end) > this.speed;
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.MOVEMENT;
   }

   @Override
   public double getSpeed() {
      return this.speed * 3.0;
   }
}

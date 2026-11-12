package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.splines.CatmullRomSpline;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CatmullRomMovementBehavior<T extends AnimatedEntity> implements ISpeedyBehavior<T> {
   public int time;
   public CatmullRomSpline spline;
   public int currentTime;
   public Location[] locations;

   public CatmullRomMovementBehavior(int time, Location... locations) {
      this.time = time;
      this.currentTime = 0;
      this.locations = locations;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      if (this.spline != null) {
         Entity entity = context.getEntity();
         float p = (float)this.currentTime / (float)this.time;
         Vec3 pos = this.spline.getPoint(p);
         Vec3 speed = this.spline.getVelocity(p);
         Vec3 ePos = entity.position();
         Vec3 ads = pos.subtract(ePos);
         entity.setPos(entity.getX() + ads.x, entity.getY() + ads.y, entity.getZ() + ads.z);
         Vec3 head = entity.getEyePosition();
         Vec3 lookAt = head.add(speed.multiply(100.0, 100.0, 100.0));
         context.getState().getData().setLookTarget(context1 -> new Location(lookAt));
         this.currentTime++;
      }
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      Location location = new Location(context.getEntity());
      Location[] locations1 = new Location[this.locations.length + 1];
      locations1[0] = location;

      for (int i = 0; i < this.locations.length; i++) {
         locations1[i + 1] = this.locations[i];
      }

      this.spline = new CatmullRomSpline(locations1);
      ((AnimatedEntity)context.getEntity()).getAnimationSystem().startAnimationAt("shake", Animations.ENGINE_LOOP);
      ((AnimatedEntity)context.getEntity()).getAnimationSystem().startAnimationAt("wheels", Animations.CAR_WHEELS);
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      if (this.spline != null) {
         Vec3 speed = this.spline.getVelocity(1.0F);
         Vec3 head = ((AnimatedEntity)context.getEntity()).getEyePosition();
         Vec3 lookAt = head.add(speed.multiply(100.0, 100.0, 100.0));
         context.getState().getData().setLookTarget(context1 -> new Location(lookAt));
         ((AnimatedEntity)context.getEntity()).getAnimationSystem().stopAnimation("shake");
         ((AnimatedEntity)context.getEntity()).getAnimationSystem().stopAnimation("wheels");
      }
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return this.currentTime <= this.time;
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.MOVEMENT;
   }

   @Override
   public double getSpeed() {
      return 0.0;
   }
}

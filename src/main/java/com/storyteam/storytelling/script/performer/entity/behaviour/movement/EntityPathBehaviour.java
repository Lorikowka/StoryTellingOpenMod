package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.InterruptableBehaviour;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public abstract class EntityPathBehaviour<T extends Mob> implements InterruptableBehaviour<T>, ISpeedyBehavior<T> {
   public final double speed;
   private boolean isInterrupted;

   public EntityPathBehaviour(double speed) {
      this.speed = speed;
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      this.onContinue(env, context);
      context.getRef().navigator.stop();
      ((Mob)context.getEntity()).getNavigation().stop();
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.MOVEMENT;
   }

   @Override
   public void onContinue(ScriptEnvironment environment, EntityContext<T> context) {
      context.getRef().navigator.paused = false;
      this.isInterrupted = false;
   }

   @Override
   public void onInterrupt(ScriptEnvironment environment, EntityContext<T> context) {
      context.getRef().navigator.paused = true;
      ((Mob)context.getEntity()).getNavigation().stop();
      this.isInterrupted = true;
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return true;
   }

   @Override
   public boolean isInterrupted() {
      return this.isInterrupted;
   }

   @Override
   public double getSpeed() {
      return this.speed;
   }
}

package com.storyteam.storytelling.script.performer.entity.behaviour.complex;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.InterruptableBehaviour;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class PerformerBehaviour<P extends Performer<P>, T extends Entity> implements InterruptableBehaviour<T> {
   final Function<EntityContext<T>, P> function;
   P performer;

   public PerformerBehaviour(Function<EntityContext<T>, P> performer) {
      this.function = performer;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      this.performer.tick();
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      this.performer = this.function.apply(context);
      this.performer.setName("Behaviour Performer " + IStoryEntity.of(context.getEntity()).getStoryName());
      this.performer.onStart();
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      this.performer.onStop();
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return !this.performer.isCompleted();
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.COMPLEX;
   }

   @Override
   public void onInterrupt(ScriptEnvironment environment, EntityContext<T> context) {
      this.performer.setPaused(true);
   }

   @Override
   public void onContinue(ScriptEnvironment environment, EntityContext<T> context) {
      this.performer.setPaused(false);
   }

   @Override
   public boolean isInterrupted() {
      return this.performer.isPaused();
   }
}

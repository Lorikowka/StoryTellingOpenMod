package com.storyteam.storytelling.script.performer.entity.behaviour.interaction;

import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.PlayerTarget;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class InteractionBehaviour<T extends Entity> implements IStoryBehaviour<T> {
   private ILookTarget<T> cacheLook;

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      context.getState().interruptBehaviours(this);
      this.cacheLook = context.getState().getData().getLookTarget();
      context.getState().getData().setLookTarget(new PlayerTarget<>());
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      context.getState().getData().setLookTarget(this.cacheLook);
      context.getState().continueBehaviours(this);
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return false;
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.INTERACTION;
   }
}

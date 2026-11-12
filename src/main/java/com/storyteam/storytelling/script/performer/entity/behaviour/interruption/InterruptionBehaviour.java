package com.storyteam.storytelling.script.performer.entity.behaviour.interruption;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.Arrays;
import java.util.HashMap;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class InterruptionBehaviour<T extends Entity> implements IStoryBehaviour<T> {
   public boolean rollbackAnimationsImmediately = false;
   private final String[] interruptLayers;
   private final HashMap<String, ConfiguredAnimation> cashAnimations = new HashMap<>();
   private ILookTarget<T> lookData;

   public InterruptionBehaviour(String... interruptLayers) {
      this.interruptLayers = interruptLayers;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      Arrays.stream(this.interruptLayers)
         .forEach(
            layer -> this.cashAnimations
                  .put(layer, context.getState().getAnimation(layer) == null ? Animations.NULL.configure() : context.getState().getAnimation(layer))
         );
      this.lookData = context.getState().getData().getLookTarget();
      context.getState().interruptBehaviours(this);
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      if (context.getState().getData().getLookTarget() != this.lookData) {
         context.getState().getData().setLookTarget(this.lookData);
      }

      if (this.rollbackAnimationsImmediately) {
         this.cashAnimations.forEach(context.getState()::setAnimation);
      } else {
         context.getState().getData().getLookController().onRotationEnd(c -> this.cashAnimations.forEach(c.getState()::setAnimation));
      }
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return true;
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.INTERRUPTION;
   }
}

package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SimpleBehaviourAction<T extends Entity> extends BehaviourAction<T, IStoryBehaviour<T>> {
   final Function<EntityContext<T>, Boolean> action;
   final BehaviourType type;

   public SimpleBehaviourAction(
      ScriptPerformer performer, Character<T> character, boolean parallel, BehaviourType type, Function<EntityContext<T>, Boolean> action
   ) {
      super(performer, character, parallel, false);
      this.action = action;
      this.type = type;
   }

   @Override
   public IStoryBehaviour<T> createBehaviour() {
      return new IStoryBehaviour<T>() {
         @Override
         public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
         }

         @Override
         public void onStart(ScriptEnvironment env, EntityContext<T> context) {
         }

         @Override
         public void onStop(ScriptEnvironment env, EntityContext<T> context) {
         }

         @Override
         public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
            return SimpleBehaviourAction.this.action.apply(context);
         }

         @NotNull
         @Override
         public BehaviourType getType() {
            return SimpleBehaviourAction.this.type;
         }
      };
   }
}

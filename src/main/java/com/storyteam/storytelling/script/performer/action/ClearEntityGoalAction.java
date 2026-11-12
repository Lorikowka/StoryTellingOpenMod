package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.story.characters.Character;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ClearEntityGoalAction<T extends Entity> extends EntityPerformerAction<T> {
   @Nullable
   BehaviourType type;

   public ClearEntityGoalAction(ScriptPerformer performer, Character<T> character) {
      super(performer, character);
   }

   public ClearEntityGoalAction(ScriptPerformer performer, Character<T> character, @Nullable BehaviourType type) {
      super(performer, character);
      this.type = type;
   }

   @Override
   public void tick() {
      this.stopBehaviour();
   }

   private void stopBehaviour() {
      EntityState<T> state = this.getEntity();
      if (this.type == null) {
         state.clearBehaviours();
      } else {
         state.stopBehaviour(this.type);
      }
   }

   @Override
   public void fastForwardToStage() {
      this.stopBehaviour();
   }
}

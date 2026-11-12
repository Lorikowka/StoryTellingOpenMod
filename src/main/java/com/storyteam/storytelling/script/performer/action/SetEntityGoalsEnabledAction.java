package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class SetEntityGoalsEnabledAction<T extends Mob> extends EntityPerformerAction<T> {
   private final boolean goals;

   public SetEntityGoalsEnabledAction(ScriptPerformer performer, @NotNull Character<T> character, boolean goals) {
      super(performer, character);
      this.goals = goals;
   }

   @Override
   public void tick() {
      this.getEntity().getData().setGoalsEnabled(this.goals);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setGoalsEnabled(this.goals);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().setGoalsEnabled(false);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("goals", this.goals).build();
   }
}

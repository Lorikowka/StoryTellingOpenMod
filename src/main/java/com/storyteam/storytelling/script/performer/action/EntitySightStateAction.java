package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntitySightStateAction<T extends Entity> extends EntityPerformerAction<T> {
   private final boolean state;

   public EntitySightStateAction(ScriptPerformer performer, @NotNull Character<T> character, boolean state) {
      super(performer, character);
      this.state = state;
   }

   @Override
   public void tick() {
      this.getEntity().freeSight(this.state);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().freeSight(this.state);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().freeSight(!this.state);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("state", this.state).build();
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CharacterInitializationAction<T extends Entity> extends EntityPerformerAction<T> {
   public CharacterInitializationAction(ScriptPerformer performer, @NotNull Character<T> character) {
      super(performer, character);
   }

   @Override
   public void tick() {
      super.tick();
      this.getOrCreateEntity();
   }

   @Override
   public void fastForwardToStage() {
      super.fastForwardToStage();
      this.getOrCreateEntity();
   }
}

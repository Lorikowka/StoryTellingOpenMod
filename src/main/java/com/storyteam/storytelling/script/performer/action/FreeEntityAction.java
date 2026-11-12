package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.List;
import net.minecraft.world.entity.Entity;

public class FreeEntityAction extends ScriptPerformerAction {
   private final boolean deleteFromRepo;
   protected final List<Character<? extends Entity>> characters;

   public FreeEntityAction(ScriptPerformer performer, List<Character<? extends Entity>> characters, boolean deleteFromRepo) {
      super(performer);
      this.deleteFromRepo = deleteFromRepo;
      this.characters = characters;
   }

   public void removeEntities(boolean removeFromRepo) {
      for (Character<? extends Entity> character : this.characters) {
         this.script.getRepository().removeEntity(character, removeFromRepo);
      }
   }

   @Override
   public void tick() {
      this.removeEntities(this.deleteFromRepo);
   }

   @Override
   public void stop() {
      this.removeEntities(this.deleteFromRepo);
   }

   @Override
   public void fastForwardToStage() {
      this.removeEntities(this.deleteFromRepo);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("characters", this.characters).append("deleteFromRepo", this.deleteFromRepo).build();
   }
}

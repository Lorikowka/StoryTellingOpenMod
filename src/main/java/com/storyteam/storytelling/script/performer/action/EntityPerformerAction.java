package com.storyteam.storytelling.script.performer.action;

import com.google.common.base.Preconditions;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EntityPerformerAction<T extends Entity> extends ScriptPerformerAction {
   protected final Character<T> character;

   public EntityPerformerAction(ScriptPerformer performer, @NotNull Character<T> character) {
      super(performer);
      Preconditions.checkNotNull(character, "Character in EntityPerformerAction should never be null.");
      this.character = character;
   }

   @Nullable
   public EntityState<T> getActiveEntityState() {
      return this.script.getRepository().getActiveState(this.character);
   }

   @NotNull
   public EntityState<T> getEntity() {
      return this.getOrCreateEntity();
   }

   public EntityState<T> getOrCreateEntity() {
      return this.script.getRepository().getOrCreate(this.character);
   }

   public EntityState<T> getOrCreateEntity(boolean forceAdd) {
      return this.script.getRepository().getOrCreate(this.character, forceAdd);
   }

   public EntityState<T> removeEntity(boolean removeFromRepo) {
      return this.script.getRepository().removeEntity(this.character, removeFromRepo);
   }

   public Character<T> getCharacter() {
      return this.character;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).build();
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntitySetDisplayNameAction<T extends Entity> extends EntityPerformerAction<T> {
   @Nullable
   private final Supplier<Component> name;

   public EntitySetDisplayNameAction(ScriptPerformer performer, @NotNull Character<T> character, @Nullable Supplier<Component> name) {
      super(performer, character);
      this.name = name;
   }

   @Override
   public void tick() {
      this.getEntity().getData().setDisplayName(this.name == null ? null : this.name.get());
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setDisplayName(this.name == null ? null : this.name.get());
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().setDisplayName(null);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("name", this.name == null ? null : this.name.get()).build();
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class EntityTypeOverrideAction<T extends Entity> extends EntityPerformerAction<T> {
   private final EntityType<? extends T> typeOverride;

   public EntityTypeOverrideAction(ScriptPerformer performer, @NotNull Character<T> character, EntityType<? extends T> typeOverride) {
      super(performer, character);
      this.typeOverride = typeOverride;
   }

   @Override
   public void tick() {
      this.getEntity().setEntityTypeOverride(this.typeOverride);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().setEntityTypeOverride(this.typeOverride);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().setEntityTypeOverride(null);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("typeOverride", this.typeOverride).build();
   }
}

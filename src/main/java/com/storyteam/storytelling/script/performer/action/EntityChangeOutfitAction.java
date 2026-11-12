package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityStateData;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class EntityChangeOutfitAction<T extends AnimatedEntity> extends EntityPerformerAction<T> {
   private final Supplier<Outfit> outfit;

   public EntityChangeOutfitAction(ScriptPerformer performer, @NotNull Character<T> character, Supplier<Outfit> outfit) {
      super(performer, character);
      this.outfit = outfit;
   }

   @Override
   public void tick() {
      EntityStateData<T> data = this.getEntity().getData();
      data.setVanity(data.getVanity().apply(this.outfit.get()));
   }

   @Override
   public void fastForwardToStage() {
      EntityStateData<T> data = this.getEntity().getData();
      data.setVanity(data.getVanity().apply(this.outfit.get()));
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("outfit", this.outfit).build();
   }
}

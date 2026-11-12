package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntitySetTextureAction<T extends Entity> extends EntityPerformerAction<T> {
   private final ResourceLocation texture;

   public EntitySetTextureAction(ScriptPerformer performer, @NotNull Character<T> character, ResourceLocation texture) {
      super(performer, character);
      this.texture = texture;
   }

   @Override
   public void tick() {
      this.getEntity().getData().setTextureOverride(this.texture);
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setTextureOverride(this.texture);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("texture", this.texture).build();
   }
}

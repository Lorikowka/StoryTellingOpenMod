package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.resources.ResourceLocation;

public class SetPlayerTextureAction extends ScriptPerformerAction {
   private final ResourceLocation texture;

   public SetPlayerTextureAction(ScriptPerformer performer, ResourceLocation texture) {
      super(performer);
      this.texture = texture;
   }

   @Override
   public void tick() {
      ScriptIO.WORLD_EXTRA_NBT.putString("player_texture", this.texture == null ? "" : this.texture.toString());
   }

   @Override
   public void rewindToStage() {
      ScriptIO.WORLD_EXTRA_NBT.putString("player_texture", "");
   }

   @Override
   public void fastForwardToStage() {
      ScriptIO.WORLD_EXTRA_NBT.putString("player_texture", this.texture == null ? "" : this.texture.toString());
   }

   @Override
   public boolean isDone() {
      return true;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("texture", this.texture).build();
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class StartEntityTracking<T extends Entity> extends EntityPerformerAction<T> {
   boolean startTracking;

   public StartEntityTracking(ScriptPerformer performer, @NotNull Character<T> character, boolean startTracking) {
      super(performer, character);
      this.startTracking = startTracking;
   }

   @Override
   public void tick() {
      this.getEntity().getData().getShouldUpdateChunk().set(Boolean.valueOf(this.startTracking));
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().getShouldUpdateChunk().set(Boolean.valueOf(!this.startTracking));
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().getShouldUpdateChunk().set(Boolean.valueOf(this.startTracking));
   }
}

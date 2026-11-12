package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SpectateEntityAction<T extends Entity> extends EntityPerformerAction<T> {
   public SpectateEntityAction(ScriptPerformer performer, @NotNull Character<T> character) {
      super(performer, character);
   }

   @Override
   public void tick() {
      EntityState<T> entity = this.getEntity();
      ServerPlayer player = ScriptUtils.getPlayer();
      if (entity != null && player != null) {
         entity.optionalValidOnly().ifPresent(player::setCamera);
      }
   }
}

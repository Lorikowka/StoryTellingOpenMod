package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.server.level.ServerPlayer;

public class SpectateStopEntityAction extends ScriptPerformerAction {
   public SpectateStopEntityAction(ScriptPerformer performer) {
      super(performer);
   }

   @Override
   public void tick() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         player.setCamera(player);
      }
   }
}

package com.storyteam.storytelling.system._core.morph.performer;

import com.storyteam.storytelling.script.performer.builder.IScriptPerformerBuilderExt;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import com.storyteam.storytelling.system._core.morph.PlayerMorph;

public interface IMorphPerformerBuilder extends IScriptPerformerBuilderExt {
   default ScriptPerformerBuilder thenRunMorph(PlayerMorph morph) {
      return this.self().thenUsePlayer(pl -> ((IAnimatedPlayer)pl).morphInto(morph));
   }

   default ScriptPerformerBuilder thenHandleMorph(PlayerMorph before, PlayerMorph after) {
      return this.self().thenHandlePlayer((p, pl) -> ((IAnimatedPlayer)pl).morphInto(p ? after : before));
   }
}

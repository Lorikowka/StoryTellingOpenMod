package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.system._core.st_cutscene.STCutsceneData;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;

public class TDCutsceneTpOrInitAction<P extends Performer<P>> extends PerformerAction<P> {
   public STCutsceneData data;

   public TDCutsceneTpOrInitAction(P performer, STCutsceneData data) {
      super(performer);
      this.data = data;
   }

   @Override
   public void tick() {
      super.tick();
      ServersideCutsceneHandler.initOrTpCamera(this.data);
   }
}

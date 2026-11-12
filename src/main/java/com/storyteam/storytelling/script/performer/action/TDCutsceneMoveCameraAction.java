package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.system._core.st_cutscene.STCutsceneData;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;

public class TDCutsceneMoveCameraAction<P extends Performer<P>> extends PerformerAction<P> {
   public STCutsceneData data;

   public TDCutsceneMoveCameraAction(P performer, STCutsceneData data) {
      super(performer);
      this.data = data;
   }

   @Override
   public void tick() {
      super.tick();
      ServersideCutsceneHandler.moveCamera(this.data);
   }
}

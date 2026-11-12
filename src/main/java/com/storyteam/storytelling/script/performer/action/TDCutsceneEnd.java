package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;

public class TDCutsceneEnd<P extends Performer<P>> extends PerformerAction<P> {
   public TDCutsceneEnd(P performer) {
      super(performer);
   }

   @Override
   public void tick() {
      super.tick();
      ServersideCutsceneHandler.stopCamera();
   }
}

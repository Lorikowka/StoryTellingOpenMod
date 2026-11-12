package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.network.utils.WebMessageHandler;
import com.storyteam.storytelling.script.performer.ScriptPerformer;

public class RemoveWebMessageAction extends ScriptPerformerAction {
   private final String id;

   public RemoveWebMessageAction(ScriptPerformer performer, String id) {
      super(performer);
      this.id = id;
   }

   @Override
   public void tick() {
      WebMessageHandler.removeInstructionLine(this.id, this.performer.script.getId());
   }

   @Override
   public void fastForwardToStage() {
      WebMessageHandler.removeInstructionLine(this.id, this.performer.script.getId());
   }
}

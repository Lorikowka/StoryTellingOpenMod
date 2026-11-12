package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.network.utils.WebMessageHandler;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;

public abstract class ScriptPerformerAction extends PerformerAction<ScriptPerformer> {
   protected final ScriptInstance script;
   public boolean sendNotice = false;
   public boolean sendWarning = false;

   public ScriptPerformerAction(ScriptPerformer performer) {
      super(performer);
      this.script = performer.script;
   }

   @Override
   public void onScriptInstanceCreated() {
      super.onScriptInstanceCreated();
   }

   public final void sendInstructionLine(String message) {
      WebMessageHandler.sendInstructionLine(this.performer.getName() + "_" + this.performer.getActionIndex(), this.script.getId(), message, false);
      this.sendWarning = true;
   }

   @Override
   public void reset0() {
      super.reset0();
      WebMessageHandler.removeInstructionLine(this.performer.getName() + "_" + this.performer.getActionIndex(), this.script.getId());
   }
}

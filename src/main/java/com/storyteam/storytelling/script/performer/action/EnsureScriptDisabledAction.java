package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.ScriptEnvironment;

public class EnsureScriptDisabledAction extends ScriptPerformerAction {
   private final int[] ids;

   public EnsureScriptDisabledAction(ScriptPerformer performer, int... ids) {
      super(performer);
      this.ids = ids;
   }

   @Override
   public void tick() {
      ScriptEnvironment env = this.script.env;

      for (int id : this.ids) {
         if (env.getScript(id) != null) {
            env.stop(id);
         }
      }
   }

   @Override
   public void fastForwardToStage() {
      ScriptEnvironment env = this.script.env;

      for (int id : this.ids) {
         if (env.getScript(id) != null) {
            env.stop(id);
         }
      }
   }
}

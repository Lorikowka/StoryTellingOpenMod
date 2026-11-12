package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.ScriptEnvironment;

public class EnsureScriptEnabledAction extends ScriptPerformerAction {
   private final int[] ids;
   private boolean forceStart = false;

   public EnsureScriptEnabledAction(ScriptPerformer performer, int... ids) {
      super(performer);
      this.ids = ids;
   }

   public EnsureScriptEnabledAction(ScriptPerformer performer, boolean forceStart, int... ids) {
      super(performer);
      this.ids = ids;
      this.forceStart = forceStart;
   }

   @Override
   public void tick() {
      ScriptEnvironment env = this.script.env;

      for (int id : this.ids) {
         if (env.getScript(id) == null || this.forceStart) {
            env.start(id);
         }
      }
   }

   @Override
   public void fastForwardToStage() {
      ScriptEnvironment env = this.script.env;

      for (int id : this.ids) {
         if (env.getScript(id) == null || this.forceStart) {
            env.start(id);
         }
      }
   }
}

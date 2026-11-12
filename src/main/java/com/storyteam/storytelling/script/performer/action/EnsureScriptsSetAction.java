package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import org.apache.commons.lang3.ArrayUtils;

public class EnsureScriptsSetAction extends ScriptPerformerAction {
   private final int[] ids;

   public EnsureScriptsSetAction(ScriptPerformer performer, int... ids) {
      super(performer);
      this.ids = ids;
   }

   @Override
   public void tick() {
      ScriptEnvironment env = this.script.env;

      for (int id : env.scriptInstances.values().stream().map(ScriptInstance::getId).toList()) {
         if (!ArrayUtils.contains(this.ids, id) && this.performer.script.getId() != id) {
            env.stop(id);
         }
      }

      for (int idx : this.ids) {
         if (env.getScript(idx) == null) {
            env.start(idx);
         }
      }
   }

   @Override
   public void fastForwardToStage() {
      ScriptEnvironment env = this.script.env;

      for (int id : env.scriptInstances.values().stream().map(ScriptInstance::getId).toList()) {
         if (!ArrayUtils.contains(this.ids, id) && this.performer.script.getId() != id) {
            env.stop(id);
         }

         ScriptInstance instance = env.getScript(id);
         if (ArrayUtils.contains(this.ids, id) && this.performer.script.getId() != id && instance != null && instance.isInvalid()) {
            instance.valid = true;
            env.start(id);
         }
      }

      for (int id : this.ids) {
         if (env.getScript(id) == null) {
            env.start(id);
         }
      }
   }
}

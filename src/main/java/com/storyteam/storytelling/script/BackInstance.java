package com.storyteam.storytelling.script;

import com.storyteam.storytelling.script.performer.entity.EntityControlPriority;
import com.storyteam.storytelling.script.story.ScriptEnvironment;

public class BackInstance extends ScriptInstance {
   public BackInstance(ScriptEnvironment env, int id) {
      super(env, id);
      this.setPriority(EntityControlPriority.LOWEST);
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.instances.backs.mechanics.S1301BackController;
import com.storyteam.storytelling.script.performer.ScriptPerformer;

public class ScriptAutostartAction extends ScriptPerformerAction {
   private final int[] ids;
   private boolean ignore;

   public ScriptAutostartAction(ScriptPerformer performer, boolean ignore, int... ids) {
      super(performer);
      this.ids = ids;
      this.ignore = ignore;
   }

   @Override
   public void tick() {
      if (this.ignore) {
         S1301BackController.ignoreScript(this.ids);
      } else {
         S1301BackController.heedScript(this.ids);
      }
   }

   @Override
   public void fastForwardToStage() {
      if (this.ignore) {
         S1301BackController.ignoreScript(this.ids);
      } else {
         S1301BackController.heedScript(this.ids);
      }
   }

   @Override
   public void rewindToStage() {
      if (this.ignore) {
         S1301BackController.heedScript(this.ids);
      } else {
         S1301BackController.ignoreScript(this.ids);
      }
   }
}

package com.storyteam.storytelling.system.time.performer.task;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.system.time.core.TimeController;

public class TimeTaskRemoveAction extends ScriptPerformerAction {
   String key;

   public TimeTaskRemoveAction(ScriptPerformer performer, String taskId) {
      super(performer);
      this.key = taskId;
   }

   @Override
   public void tick() {
      TimeController.get().removeTimeTask(this.key);
   }

   @Override
   public void fastForwardToStage() {
      TimeController.get().removeTimeTask(this.key);
   }

   @Override
   public void rewindToStage() {
      TimeController.get().removeTimeTask(this.key);
   }
}

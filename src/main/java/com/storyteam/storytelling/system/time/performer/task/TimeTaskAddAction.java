package com.storyteam.storytelling.system.time.performer.task;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.time.core.TimeTask;

public class TimeTaskAddAction extends ScriptPerformerAction {
   TimeTask task;
   String key;

   public TimeTaskAddAction(ScriptPerformer performer, String taskId, TimeTask task) {
      super(performer);
      this.task = task;
      this.key = taskId;
   }

   @Override
   public void tick() {
      this.setTimeRunner();
   }

   @Override
   public void fastForwardToStage() {
      this.setTimeRunner();
   }

   @Override
   public void rewindToStage() {
      TimeController.get().stopTimeTask(this.key);
   }

   public void setTimeRunner() {
      TimeController controller = TimeController.get();
      controller.unlockTime(false);
      controller.addTask(this.key, this.task);
   }
}

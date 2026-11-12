package com.storyteam.storytelling.system.time.performer.task;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.time.data.TimeOfDay;

public class TimeSetAction extends ScriptPerformerAction {
   private final Integer newCurrentDay;
   private final String time;

   public TimeSetAction(ScriptPerformer performer, Integer newCurrentDay, String time) {
      super(performer);
      this.newCurrentDay = newCurrentDay;
      this.time = time;
   }

   @Override
   public void tick() {
      this.setTime();
   }

   @Override
   public void rewindToStage() {
      this.setTime();
   }

   @Override
   public void fastForwardToStage() {
      this.setTime();
   }

   @Override
   public void globalFastForward() {
      this.setTime();
   }

   private void setTime() {
      TimeController.get().setTime(this.newCurrentDay, TimeOfDay.parse(this.time).time());
   }
}

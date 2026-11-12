package com.storyteam.storytelling.system.time.performer.task;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.time.data.TimeMark;
import com.storyteam.storytelling.system.time.data.TimeState;

public class TimeMarkAction extends ScriptPerformerAction {
   private final TimeMark mark;
   private final String id;

   public TimeMarkAction(ScriptPerformer performer, String id, TimeMark mark) {
      super(performer);
      this.mark = mark;
      this.id = id;
   }

   @Override
   public void onScriptInstanceCreated() {
      super.onScriptInstanceCreated();
      TimeState timeState = this.performer.getState(TimeState.TYPE);
      timeState.registerTimeMark(this.id, this.mark);
   }

   @Override
   public void tick() {
      TimeState timeState = this.performer.getState(TimeState.TYPE);
      timeState.goThrough(this.id);
   }

   @Override
   public void rewindToStage() {
      TimeState timeState = this.performer.getState(TimeState.TYPE);
      timeState.goBack(this.id);
   }

   @Override
   public void fastForwardToStage() {
      TimeState timeState = this.performer.getState(TimeState.TYPE);
      timeState.goThrough(this.id);
      TimeController.get().setTime(this.mark.getTimeOfDay().time());
   }
}

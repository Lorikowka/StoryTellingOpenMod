package com.storyteam.storytelling.system.time.performer;

import com.storyteam.storytelling.script.performer.builder.IScriptPerformerBuilderExt;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.system.time.core.TimeTask;
import com.storyteam.storytelling.system.time.data.TimeMark;
import com.storyteam.storytelling.system.time.data.TimeOfDay;
import com.storyteam.storytelling.system.time.performer.task.TimeMarkAction;
import com.storyteam.storytelling.system.time.performer.task.TimeSetAction;
import com.storyteam.storytelling.system.time.performer.task.TimeTaskAddAction;
import com.storyteam.storytelling.system.time.performer.task.TimeTaskRemoveAction;
import com.storyteam.storytelling.system.time.performer.task.WaitTimeBetweenAction;

public interface ITimePerformerBuilder extends IScriptPerformerBuilderExt {
   default ScriptPerformerBuilder setTime(String time) {
      return this.self().append(performer -> new TimeSetAction(performer, null, time));
   }

   default ScriptPerformerBuilder setTime(int newCurrentDay, String time) {
      return this.self().append(performer -> new TimeSetAction(performer, newCurrentDay, time));
   }

   default ScriptPerformerBuilder addTimeTask(String taskId, TimeTask task) {
      return this.self().append(performer -> new TimeTaskAddAction(performer, taskId, task));
   }

   default ScriptPerformerBuilder removeTimeTask(String taskId) {
      return this.self().append(performer -> new TimeTaskRemoveAction(performer, taskId));
   }

   default ScriptPerformerBuilder timeMarker(String time) {
      return this.timeMarker(time, false);
   }

   default ScriptPerformerBuilder timeMarker(String time, boolean setOnPass) {
      return this.self().append(performer -> new TimeMarkAction(performer, time, new TimeMark(TimeOfDay.parse(time), setOnPass)));
   }

   default ScriptPerformerBuilder timeMarker(String id, String time) {
      return this.timeMarker(id, time, false);
   }

   default ScriptPerformerBuilder timeMarker(String id, String time, boolean setOnPass) {
      return this.self().append(performer -> new TimeMarkAction(performer, id, new TimeMark(TimeOfDay.parse(time), setOnPass)));
   }

   default ScriptPerformerBuilder waitForTimeBetween(String lowerEdge, String upperEdge) {
      return this.self().append(performer -> new WaitTimeBetweenAction<>(performer, lowerEdge, upperEdge));
   }
}

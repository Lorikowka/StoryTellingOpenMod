package com.storyteam.storytelling.system.time.data;

import com.google.common.base.Preconditions;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.state.State;
import com.storyteam.storytelling.system.time.core.TimeCondition;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.time.core.TimeTask;
import com.storyteam.storytelling.util.data_structure.NodeTreeMap;

public class TimeState extends State<ScriptPerformer> {
   public static final State.Type<ScriptPerformer, TimeState> TYPE = create(TimeState::new);
   private final String id;
   private final NodeTreeMap<String, TimeMark> timeStates = new NodeTreeMap<>();

   public TimeState(ScriptPerformer performer) {
      super(performer);
      String name = performer.getName() == null ? "" : performer.getName();
      this.id = name + "-time_state";
   }

   public void registerTimeMark(String markId, TimeMark mark) {
      Preconditions.checkArgument(!this.timeStates.containsKey(markId), "Time mark with id %s already exists!", markId);
      if (this.timeStates.isEmpty()) {
         TimeController controller = TimeController.get();
         TimeCondition stopAt = new TimeCondition(mark.getTimeOfDay());
         controller.addTask(this.id, TimeTask.builder().updateEvent(false).stopAt(stopAt).priority(1).build());
         controller.resumeTimeTask(this.id);
      }

      this.timeStates.put(markId, mark);
   }

   public TimeOfDay getTimeState(String markId) {
      Preconditions.checkArgument(this.timeStates.containsKey(markId), "No time state found for id %s", markId);
      return this.timeStates.get(markId).getTimeOfDay();
   }

   @Override
   public void onScriptStop() {
      TimeController controller = TimeController.get();
      controller.removeTimeTask(this.id);
      controller.unlockTime(false);
   }

   public void goBack(String stateId) {
      if (!this.timeStates.isEmpty()) {
         TimeController controller = TimeController.get();
         NodeTreeMap.ImmutableNode<String, TimeMark> active = this.timeStates.getNode(stateId);
         TimeCondition stopAt = active.hasPrevious() ? new TimeCondition(active.previous().getValue().getTimeOfDay()) : TimeCondition.infinity();
         controller.addTask(this.id, TimeTask.builder().speed(0.1F).updateEvent(false).stopAt(stopAt).priority(1).build());
         controller.setTime(active.getValue().getTimeOfDay().time());
      }
   }

   public void goThrough(String stateId) {
      if (!this.timeStates.isEmpty()) {
         TimeController controller = TimeController.get();
         NodeTreeMap.ImmutableNode<String, TimeMark> active = this.timeStates.getNode(stateId);
         controller.unlockTime(false);
         TimeCondition stopAt = active.hasNext() ? new TimeCondition(active.next().getValue().getTimeOfDay()) : TimeCondition.infinity();
         controller.addTask(
            this.id,
            TimeTask.builder().lockOnEnd(true).speed(0.1F).stopAt(stopAt).priority(1).build(),
            Math.min(controller.getTime(), active.getValue().getTimeOfDay().time())
         );
         if (active.getValue().isSetTime()) {
            controller.setTime(active.getValue().getTimeOfDay().time());
         }
      }
   }
}

package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.logging.LoggingController;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.state.stage.StageState;
import com.storyteam.storytelling.util.ToStringBuilder;

public class LabelAction<P extends Performer<P>> extends PerformerAction<P> {
   protected final String name;
   private final boolean sendDiscordLog;

   public LabelAction(P performer, String name, boolean sendDiscordLog) {
      super(performer);
      this.sendDiscordLog = sendDiscordLog;
      this.name = name;
   }

   @Override
   public void onInit() {
      super.onInit();
      StageState<P> state = (StageState<P>) this.performer.getState(StageState.TYPE);
      state.tryToAdd(this.name);
   }

   @Override
   public boolean isDone() {
      if (this.performer instanceof ScriptPerformer sp && this.sendDiscordLog) {
         LoggingController.capturePerformerLabel(sp.script, this.name, this.performer.getName());
      }

      return true;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("name", this.name).build();
   }
}

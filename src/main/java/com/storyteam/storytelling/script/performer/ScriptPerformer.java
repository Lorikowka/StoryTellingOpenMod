package com.storyteam.storytelling.script.performer;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.state.PlayerScriptState;
import com.storyteam.storytelling.script.performer.state.stage.StageEvent;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;

public class ScriptPerformer extends Performer<ScriptPerformer> implements IAutoNBTSerializable {
   public final ScriptInstance script;

   public ScriptPerformer(ScriptInstance script) {
      this.script = script;
   }

   public PlayerScriptState getPlayerState() {
      return this.getState(PlayerScriptState.TYPE);
   }

   @Override
   public void onStagePass(String labelName) {
      super.onStagePass(labelName);
      this.script.passEvent2Stage(EventType.STAGE_PASS, new StageEvent<>(labelName, this));
   }
}

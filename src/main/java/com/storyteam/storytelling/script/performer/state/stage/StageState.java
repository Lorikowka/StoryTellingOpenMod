package com.storyteam.storytelling.script.performer.state.stage;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.state.State;
import java.util.LinkedHashSet;
import org.zeith.hammerlib.util.java.Cast;

public class StageState<P extends Performer<P>> extends State<P> {
   public static final State.Type TYPE = new State.Type(p -> new StageState((Performer) p));
   public final LinkedHashSet<String> stages = new LinkedHashSet<>();

   public StageState(P performer) {
      super(performer);
   }

   public void tryToAdd(String name) {
      if (!this.hasStage(name)) {
         this.stages.add(name);
      }
   }

   public boolean hasStage(String name) {
      return this.stages.contains(name);
   }

   @Override
   public <T> void handleEvent(P performer, EventType<T> type, T event) {
      super.handleEvent(performer, type, event);
      if (type == EventType.STAGE_PASS) {
         StageEvent<? extends Performer<?>> stageEvent = (StageEvent<? extends Performer<?>>)event;
         if (performer != stageEvent.host() && performer.containsLabel(stageEvent.name())) {
            performer.gotoLabel(stageEvent.name(), false);
         }
      }
   }
}

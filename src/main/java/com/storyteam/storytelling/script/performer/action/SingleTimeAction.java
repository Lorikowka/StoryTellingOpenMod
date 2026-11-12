package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;

public class SingleTimeAction<P extends Performer<P>> extends PerformerAction<P> {
   private final Runnable run;

   public SingleTimeAction(P performer, Runnable run) {
      super(performer);
      this.run = run;
   }

   @Override
   public void tick() {
      this.run.run();
   }
}

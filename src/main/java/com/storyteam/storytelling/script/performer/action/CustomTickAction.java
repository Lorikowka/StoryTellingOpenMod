package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import java.util.function.Supplier;

public class CustomTickAction<P extends Performer<P>> extends PerformerAction<P> {
   private final Runnable onTick;
   private final Runnable onStop;
   private final Runnable onFastForward;
   private final Runnable onRewind;
   private final Supplier<Boolean> completionCallback;

   public CustomTickAction(P performer, Runnable onTick, Runnable onStop, Runnable onFastForward, Runnable onRewind, Supplier<Boolean> completionCallback) {
      super(performer);
      this.onTick = onTick;
      this.onStop = onStop;
      this.onFastForward = onFastForward;
      this.onRewind = onRewind;
      this.completionCallback = completionCallback;
   }

   @Override
   public void tick() {
      this.onTick.run();
   }

   @Override
   public void stop() {
      this.onStop.run();
   }

   @Override
   public void fastForwardToStage() {
      this.onFastForward.run();
   }

   @Override
   public void rewindToStage() {
      this.onRewind.run();
   }

   @Override
   public boolean isDone() {
      return this.completionCallback.get();
   }
}

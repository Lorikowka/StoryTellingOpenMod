package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Consumer;

public class HandleStateAction<P extends Performer<P>> extends PerformerAction<P> {
   private final HandleStateAction.StateHandler handler;

   public HandleStateAction(P performer, HandleStateAction.StateHandler handler) {
      super(performer);
      this.handler = handler;
   }

   public HandleStateAction(P performer, final Runnable before, final Runnable after) {
      super(performer);
      this.handler = new HandleStateAction.StateHandler() {
         @Override
         public void onPassed() {
            after.run();
         }

         @Override
         public void onRewind() {
            before.run();
         }
      };
   }

   public HandleStateAction(P performer, final Consumer<Boolean> isPassed) {
      super(performer);
      this.handler = new HandleStateAction.StateHandler() {
         @Override
         public void onPassed() {
            isPassed.accept(true);
         }

         @Override
         public void onRewind() {
            isPassed.accept(false);
         }
      };
   }

   @Override
   public void tick() {
      this.handler.onPassed();
   }

   @Override
   public void fastForwardToStage() {
      this.handler.onPassed();
   }

   @Override
   public void rewindToStage() {
      this.handler.onRewind();
   }

   @Override
   public void globalFastForward() {
      this.handler.globalFastForward();
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("handler", this.handler).build();
   }

   public interface StateHandler {
      default void onPassed() {
      }

      default void onRewind() {
      }

      default void globalFastForward() {
      }
   }
}

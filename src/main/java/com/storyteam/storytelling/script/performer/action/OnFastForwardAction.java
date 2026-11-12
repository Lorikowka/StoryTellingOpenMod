package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.util.ToStringBuilder;

public class OnFastForwardAction<P extends Performer<P>> extends PerformerAction<P> {
   private final OnFastForwardAction.StateHandler handler;

   public OnFastForwardAction(P performer, OnFastForwardAction.StateHandler handler) {
      super(performer);
      this.handler = handler;
   }

   public OnFastForwardAction(P performer, final Runnable action) {
      super(performer);
      this.handler = new OnFastForwardAction.StateHandler() {
         @Override
         public void onFastForward() {
            action.run();
         }
      };
   }

   @Override
   public void fastForwardToStage() {
      this.handler.onFastForward();
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("handler", this.handler).build();
   }

   public interface StateHandler {
      default void onFastForward() {
      }
   }
}

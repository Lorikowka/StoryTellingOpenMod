package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import lombok.Generated;

public class FinishAction<P extends Performer<P>> extends PerformerAction<P> {
   private final boolean withoutFastForward;

   public FinishAction(P performer, boolean withoutFastForward) {
      super(performer);
      this.withoutFastForward = withoutFastForward;
   }

   @Override
   public void tick() {
      this.performer.gotoSavepoint("final", !this.withoutFastForward);
   }

   @Override
   public void fastForwardToStage() {
      this.performer.gotoSavepoint("final", true);
   }

   @Generated
   public boolean isWithoutFastForward() {
      return this.withoutFastForward;
   }
}

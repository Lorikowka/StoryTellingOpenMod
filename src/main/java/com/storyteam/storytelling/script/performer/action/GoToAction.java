package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;

public class GoToAction<P extends Performer<P>> extends PerformerAction<P> {
   private final Supplier<String> label;
   private final boolean isState;
   private final boolean withoutFastRun;

   public GoToAction(P performer, Supplier<String> label, boolean isState, boolean withoutFastRun) {
      super(performer);
      this.label = label;
      this.isState = isState;
      this.withoutFastRun = withoutFastRun;
   }

   @Override
   public void tick() {
      if (this.isState) {
         this.performer.gotoSavepoint(this.label.get(), this.withoutFastRun);
      } else {
         this.performer.gotoLabel(this.label.get(), this.withoutFastRun);
      }
   }

   @Override
   public boolean isDone() {
      return false;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("stage", this.label.get()).append("withoutFastRun", this.withoutFastRun).build();
   }
}

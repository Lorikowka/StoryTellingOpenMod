package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.Title;
import com.storyteam.storytelling.util.ToStringBuilder;

public class TitleAction extends ScriptPerformerAction {
   private final Title title;
   private int ticks = 0;

   public TitleAction(ScriptPerformer performer, Title title) {
      super(performer);
      this.title = title;
   }

   @Override
   public void tick() {
      if (this.ticks == 0) {
         this.title.send();
      }

      this.ticks++;
   }

   @Override
   public void reset() {
      this.ticks = 0;
   }

   @Override
   public void fastForwardToStage() {
      this.ticks = this.title.getCombinedDurationTicks();
   }

   @Override
   public void rewindToStage() {
      this.ticks = 0;
   }

   @Override
   public boolean isDone() {
      return this.ticks > this.title.getCombinedDurationTicks();
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("title", this.title).build();
   }
}

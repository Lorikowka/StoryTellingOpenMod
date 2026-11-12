package com.storyteam.storytelling.script.performer.action.modificators;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ChanceModifier<P extends Performer<P>> implements PerformerModifier<P> {
   float chance;

   public ChanceModifier(float chance) {
      this.chance = chance;
   }

   public void modify(@NotNull P performer, @NotNull List<PerformerAction<P>> actionTrace) {
      var last = new PerformerActionDecorator<P>(performer, actionTrace.remove(actionTrace.size() - 1)) {
         boolean init;
         boolean doAction;

         @Override
         public void tick() {
            if (!this.init) {
               this.init = true;
               this.doAction = Math.random() <= (double)ChanceModifier.this.chance;
            }

            if (this.doAction) {
               super.tick();
            }
         }

         @Override
         public boolean isDone() {
            return this.init && (!this.doAction || super.isDone());
         }
      };
      actionTrace.add(last);
   }
}

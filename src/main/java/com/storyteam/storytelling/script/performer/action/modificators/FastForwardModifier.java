package com.storyteam.storytelling.script.performer.action.modificators;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class FastForwardModifier<P extends Performer<P>> implements PerformerModifier<P> {
   final boolean override;
   final Consumer<PerformerAction<P>> action;

   public FastForwardModifier(boolean override, Consumer<PerformerAction<P>> action) {
      this.override = override;
      this.action = action;
   }

   public FastForwardModifier(boolean override, P performer) {
      this.override = override;
      this.action = action -> performer.gotoSavepoint("final");
   }

   public void modify(@NotNull P performer, @NotNull List<PerformerAction<P>> actionTrace) {
      if (actionTrace.isEmpty()) {
         this.throwIllegalStateException(0, performer.getName());
      }

      var last = new PerformerActionDecorator<P>(performer, actionTrace.remove(actionTrace.size() - 1)) {
         @Override
         public void fastForwardToStage() {
            if (!FastForwardModifier.this.override) {
               super.fastForwardToStage();
            }

            FastForwardModifier.this.action.accept(this.innerAction);
         }
      };
      actionTrace.add(last);
   }

   @Override
   public String getName() {
      return "Fast Forward To Stage";
   }
}

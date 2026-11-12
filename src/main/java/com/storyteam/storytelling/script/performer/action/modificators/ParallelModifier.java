package com.storyteam.storytelling.script.performer.action.modificators;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.ParallelPerformersAction;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ParallelModifier<P extends Performer<P>> implements PerformerModifier<P> {
   final ParallelPerformersAction.CompletionCondition completionCondition;

   public ParallelModifier(ParallelPerformersAction.CompletionCondition completionCondition) {
      this.completionCondition = completionCondition;
   }

   public void modify(@NotNull P performer, @NotNull List<PerformerAction<P>> actionTrace) {
      if (actionTrace.size() < 2) {
         this.throwIllegalStateException(2, performer.getName());
      }

      PerformerAction<P> current = actionTrace.remove(actionTrace.size() - 1);
      final var previous = new PerformerActionDecorator<P>(performer, actionTrace.remove(actionTrace.size() - 1)) {
         @Override
         public boolean shouldContinue() {
            return true;
         }
      };
      var parallel = new PerformerActionDecorator<P>(performer, current) {
         @Override
         public boolean isDone() {
            return ParallelModifier.this.completionCondition == ParallelPerformersAction.CompletionCondition.ALL_COMPLETED
               ? super.isDone() && previous.isDone()
               : super.isDone() || previous.isDone();
         }

         @Override
         public String toString() {
            return String.format("[%s]%s", ParallelModifier.this.getName(), super.toString());
         }
      };
      actionTrace.add(previous);
      actionTrace.add(parallel);
   }

   @Override
   public String getName() {
      return this.completionCondition == ParallelPerformersAction.CompletionCondition.ALL_COMPLETED ? "Параллельность: все." : "Параллельность: любое.";
   }
}

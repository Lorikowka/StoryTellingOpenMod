package com.storyteam.storytelling.script.performer.action.modificators;

import com.storyteam.storytelling.script.performer.IPerformer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface PerformerModifier<P extends IPerformer<P>> {
   void modify(@NotNull P var1, @NotNull List<PerformerAction<P>> var2);

   default String getName() {
      return "Noname Modifier";
   }

   default void throwIllegalStateException(int taskCount, String performerName) {
      throw new IllegalStateException(
         String.format("Performer %s should have at least %d actions before [%s] modifier", performerName, taskCount, this.getName())
      );
   }
}

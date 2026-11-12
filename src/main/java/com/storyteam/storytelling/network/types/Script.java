package com.storyteam.storytelling.network.types;

import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Script(int script, boolean active, boolean paused, double progress, int action, int totalActions) {
   @Contract("_ -> new")
   @NotNull
   public static Script fromScriptInstance(@NotNull ScriptInstance scriptInstance) {
      return fromScriptInstance(scriptInstance, true);
   }

   @Contract("_, _ -> new")
   @NotNull
   public static Script fromScriptInstance(@NotNull ScriptInstance scriptInstance, boolean active) {
      ScriptPerformer main = scriptInstance.getMainPerformer() != null ? scriptInstance.getMainPerformer() : scriptInstance.getPerformers().iterator().next();
      double percent = 0.0;
      if (main != null) {
         percent = (double)main.getActionIndex() / (double)main.getActionsAmount() * 100.0;
      }

      if (Double.isNaN(percent)) {
         percent = 0.0;
      }

      return new Script(scriptInstance.getId(), active, scriptInstance.isPaused(), percent, main.getActionIndex(), main.getActionsAmount());
   }

   @Contract("_, _, _ -> new")
   @NotNull
   public static Script fromScriptInstance(@NotNull ScriptInstance scriptInstance, boolean active, double percent) {
      if (Double.isNaN(percent)) {
         percent = 0.0;
      }

      ScriptPerformer main = scriptInstance.getMainPerformer() != null ? scriptInstance.getMainPerformer() : scriptInstance.getPerformers().iterator().next();
      return new Script(scriptInstance.getId(), active, scriptInstance.isPaused(), percent, main.getActionIndex(), main.getActionsAmount());
   }
}

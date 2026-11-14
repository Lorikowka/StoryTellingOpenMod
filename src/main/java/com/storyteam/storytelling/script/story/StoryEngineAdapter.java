package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/** Historical Lua quest operation names, now backed by Storytelling's own journal. */
public final class StoryEngineAdapter {
   private StoryEngineAdapter() {
   }

   public enum Operation {
      ENSURE_QUEST("ensureQuest", 3),
      EDIT_QUEST("editQuestDescription", 2),
      ADD_TASK("addManualTask", 3),
      START_QUEST("startQuestAll", 1),
      RESET_QUEST("resetQuestAll", 1),
      COMPLETE_TASK("completeTaskAll", 2),
      COMPLETE_QUEST("completeQuestAll", 1),
      FAIL_QUEST("failQuestAll", 1),
      NOTIFY("notify", 2);

      private final String method;
      private final int argumentCount;

      Operation(String method, int argumentCount) {
         this.method = method;
         this.argumentCount = argumentCount;
      }
   }

   public static void invoke(Operation operation, String... arguments) {
      if (arguments.length != operation.argumentCount) {
         throw new IllegalArgumentException("Wrong number of arguments for " + operation.method);
      }
      if (operation == Operation.NOTIFY) {
         ServerPlayer player = ScriptUtils.getPlayer();
         if (player != null) player.sendSystemMessage(Component.literal(arguments[0] + ": " + arguments[1]));
      } else {
         IStoryServer server = IStoryServer.get();
         if (server == null) throw new IllegalStateException("Story server is unavailable");
         server.getScriptEnv().questJournal.apply(operation, arguments);
      }
   }
}

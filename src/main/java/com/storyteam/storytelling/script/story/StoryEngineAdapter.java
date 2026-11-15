package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.net.PacketStoryHud;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.net.Network;

/** Historical Lua quest operation names, now backed by Storytelling's own journal. */
public final class StoryEngineAdapter {
   private StoryEngineAdapter() {
   }

   public enum Operation {
      ENSURE_QUEST("ensureQuest", 3),
      EDIT_QUEST("editQuestDescription", 2),
      ADD_TASK("addManualTask", 3),
      ADD_SUBTASK("addManualSubtask", 4),
      ADD_HIDDEN_SUBTASK("addHiddenSubtask", 4),
      START_QUEST("startQuestAll", 1),
      RESET_QUEST("resetQuestAll", 1),
      COMPLETE_TASK("completeTaskAll", 2),
      COMPLETE_SUBTASK("completeSubtaskAll", 3),
      COMPLETE_QUEST("completeQuestAll", 1),
      FAIL_QUEST("failQuestAll", 1),
      NOTIFY("notify", 2),
      SET_QUEST_SOURCE("setQuestSource", 2);

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
         if (player != null) Network.sendTo(player, new PacketStoryHud(false, arguments[0] + ": " + arguments[1], 100));
      } else {
         IStoryServer server = IStoryServer.get();
         if (server == null) throw new IllegalStateException("Story server is unavailable");
         QuestJournal journal = server.getScriptEnv().questJournal;
         List<QuestJournal.Entry> before = journal.snapshot();
         journal.apply(operation, arguments);
         List<QuestJournal.Entry> after = journal.snapshot();
         String notification = questNotification(operation, arguments[0], before, after);
         if (notification != null) Network.sendToAll(new PacketStoryHud(false, notification, 90));
      }
   }

   static String questNotification(Operation operation, String id,
                                   List<QuestJournal.Entry> before, List<QuestJournal.Entry> after) {
      QuestJournal.Entry previous = before.stream().filter(entry -> entry.id().equals(id)).findFirst().orElse(null);
      QuestJournal.Entry current = after.stream().filter(entry -> entry.id().equals(id)).findFirst().orElse(null);
      if (previous == null && current == null || previous != null && previous.equals(current)) return null;
      String title = current != null ? current.title() : previous.title();
      String ending = current == null ? "сброшено" : switch (current.status()) {
         case ACTIVE -> previous == null || previous.status() != QuestJournal.Status.ACTIVE ? "начато" : "обновлено";
         case COMPLETED -> "завершено";
         case FAILED -> "провалено";
         case AVAILABLE -> "обновлено";
      };
      return "Задание «" + title + "» " + ending + "!";
   }
}

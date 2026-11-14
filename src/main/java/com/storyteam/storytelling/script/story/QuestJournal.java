package com.storyteam.storytelling.script.story;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

/** Server-owned, world-wide quest state. Quest definitions and progress survive world reloads. */
public final class QuestJournal {
   public enum Status { AVAILABLE, ACTIVE, COMPLETED, FAILED }

   private final Map<String, Quest> quests = new LinkedHashMap<>();

   public void apply(StoryEngineAdapter.Operation operation, String... args) {
      if (operation == StoryEngineAdapter.Operation.NOTIFY) return;
      Quest quest = quests.get(args[0]);
      if (operation == StoryEngineAdapter.Operation.ENSURE_QUEST) {
         if (quest == null) quests.put(args[0], new Quest(args[0], args[1], args[2]));
         else { quest.title = args[1]; quest.description = args[2]; }
         return;
      }
      if (quest == null) throw new IllegalStateException("Unknown quest: " + args[0] + "; call quest_create first");
      switch (operation) {
         case EDIT_QUEST -> quest.description = args[1];
         case ADD_TASK -> quest.tasks.computeIfAbsent(args[1], id -> new Task(id, args[2])).title = args[2];
         case START_QUEST -> { if (quest.status == Status.AVAILABLE) quest.status = Status.ACTIVE; }
         case RESET_QUEST -> { quest.status = Status.AVAILABLE; quest.tasks.values().forEach(task -> task.done = false); }
         case COMPLETE_TASK -> {
            Task task = quest.tasks.get(args[1]);
            if (task == null) throw new IllegalStateException("Unknown task " + args[1] + " in quest " + args[0]);
            if (quest.status == Status.ACTIVE) task.done = true;
         }
         case COMPLETE_QUEST -> quest.status = Status.COMPLETED;
         case FAIL_QUEST -> quest.status = Status.FAILED;
         default -> throw new IllegalArgumentException("Unsupported quest operation " + operation);
      }
   }

   public List<String> displayLines() {
      List<String> lines = new ArrayList<>();
      for (Quest quest : quests.values()) {
         if (quest.status == Status.AVAILABLE) continue;
         String marker = switch (quest.status) {
            case ACTIVE -> "§b●";
            case COMPLETED -> "§a✓";
            case FAILED -> "§c✕";
            case AVAILABLE -> "§7○";
         };
         lines.add(marker + " §f" + quest.title);
         if (!quest.description.isBlank()) lines.add("  §7" + quest.description);
         for (Task task : quest.tasks.values()) lines.add("  " + (task.done ? "§a✓" : "§7○") + " §f" + task.title);
         lines.add("");
      }
      return lines;
   }

   public CompoundTag save() {
      CompoundTag root = new CompoundTag();
      ListTag list = new ListTag();
      for (Quest quest : quests.values()) {
         CompoundTag entry = new CompoundTag();
         entry.putString("id", quest.id);
         entry.putString("title", quest.title);
         entry.putString("description", quest.description);
         entry.putString("status", quest.status.name());
         ListTag tasks = new ListTag();
         for (Task task : quest.tasks.values()) {
            CompoundTag item = new CompoundTag();
            item.putString("id", task.id);
            item.putString("title", task.title);
            item.putBoolean("done", task.done);
            tasks.add(item);
         }
         entry.put("tasks", tasks);
         list.add(entry);
      }
      root.put("quests", list);
      return root;
   }

   public void load(CompoundTag root) {
      quests.clear();
      ListTag list = root.getList("quests", Tag.TAG_COMPOUND);
      for (int i = 0; i < list.size(); i++) {
         CompoundTag entry = list.getCompound(i);
         String id = entry.getString("id");
         if (id.isBlank()) continue;
         Quest quest = new Quest(id, entry.getString("title"), entry.getString("description"));
         try { quest.status = Status.valueOf(entry.getString("status")); }
         catch (IllegalArgumentException ignored) { quest.status = Status.AVAILABLE; }
         ListTag tasks = entry.getList("tasks", Tag.TAG_COMPOUND);
         for (int j = 0; j < tasks.size(); j++) {
            CompoundTag item = tasks.getCompound(j);
            String taskId = item.getString("id");
            if (taskId.isBlank()) continue;
            Task task = new Task(taskId, item.getString("title"));
            task.done = item.getBoolean("done");
            quest.tasks.put(taskId, task);
         }
         quests.put(id, quest);
      }
   }

   private static final class Quest {
      private final String id;
      private String title;
      private String description;
      private Status status = Status.AVAILABLE;
      private final Map<String, Task> tasks = new LinkedHashMap<>();
      private Quest(String id, String title, String description) {
         this.id = id; this.title = title; this.description = description;
      }
   }

   private static final class Task {
      private final String id;
      private String title;
      private boolean done;
      private Task(String id, String title) { this.id = id; this.title = title; }
   }
}

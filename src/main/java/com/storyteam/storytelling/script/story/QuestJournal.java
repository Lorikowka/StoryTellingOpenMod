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
   public record TaskEntry(String title, boolean done, boolean child) { }
   public record Entry(String id, String title, String source, String description, Status status, List<TaskEntry> tasks) { }

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
         case SET_QUEST_SOURCE -> quest.source = args[1];
         case EDIT_QUEST -> quest.description = args[1];
         case ADD_TASK -> {
            Task task = quest.tasks.computeIfAbsent(args[1], id -> new Task(id, args[2], ""));
            if (!task.parentId.isEmpty()) throw new IllegalArgumentException("Task ID already belongs to a subtask: " + args[1]);
            task.title = args[2];
         }
         case ADD_SUBTASK, ADD_HIDDEN_SUBTASK -> {
            Task parent = topLevelTask(quest, args[1]);
            if (args[2].isBlank() || args[2].equals(parent.id)) throw new IllegalArgumentException("Invalid subtask ID: " + args[2]);
            boolean newChild = !quest.tasks.containsKey(args[2]);
            Task child = quest.tasks.computeIfAbsent(args[2], id -> new Task(id, args[3], parent.id));
            if (!child.parentId.equals(parent.id)) throw new IllegalArgumentException("Subtask ID already used: " + args[2]);
            child.title = args[3];
            child.hidden = operation == StoryEngineAdapter.Operation.ADD_HIDDEN_SUBTASK;
            if (newChild && quest.status == Status.ACTIVE) parent.done = false;
         }
         case START_QUEST -> {
            // A new script run is a new attempt, even if this quest was completed before.
            quest.status = Status.ACTIVE;
            quest.tasks.values().forEach(task -> task.done = false);
         }
         case RESET_QUEST -> { quest.status = Status.AVAILABLE; quest.tasks.values().forEach(task -> task.done = false); }
         case COMPLETE_TASK -> {
            Task task = quest.tasks.get(args[1]);
            if (task == null) throw new IllegalStateException("Unknown task " + args[1] + " in quest " + args[0]);
            if (quest.status == Status.ACTIVE) {
               task.done = true;
               if (task.parentId.isEmpty()) quest.tasks.values().stream().filter(child -> child.parentId.equals(task.id))
                  .forEach(child -> child.done = true);
               else completeParentIfReady(quest, task.parentId);
               completeQuestIfReady(quest);
            }
         }
         case COMPLETE_SUBTASK -> {
            Task parent = topLevelTask(quest, args[1]);
            Task child = quest.tasks.get(args[2]);
            if (child == null || !child.parentId.equals(parent.id))
               throw new IllegalStateException("Unknown subtask " + args[2] + " under " + parent.id);
            if (quest.status == Status.ACTIVE) {
               child.done = true;
               completeParentIfReady(quest, parent.id);
               completeQuestIfReady(quest);
            }
         }
         case COMPLETE_QUEST -> quest.status = Status.COMPLETED;
         case FAIL_QUEST -> quest.status = Status.FAILED;
         default -> throw new IllegalArgumentException("Unsupported quest operation " + operation);
      }
   }

   private static Task topLevelTask(Quest quest, String taskId) {
      Task task = quest.tasks.get(taskId);
      if (task == null || !task.parentId.isEmpty()) throw new IllegalStateException("Unknown parent task: " + taskId);
      return task;
   }

   private static void completeParentIfReady(Quest quest, String parentId) {
      Task parent = topLevelTask(quest, parentId);
      List<Task> children = quest.tasks.values().stream().filter(task -> task.parentId.equals(parentId)).toList();
      if (!children.isEmpty() && children.stream().allMatch(task -> task.done)) parent.done = true;
   }

   private static void completeQuestIfReady(Quest quest) {
      if (quest.status == Status.ACTIVE && !quest.tasks.isEmpty()
         && quest.tasks.values().stream().allMatch(task -> task.done)) quest.status = Status.COMPLETED;
   }

   private static List<Task> orderedTasks(Quest quest) {
      List<Task> ordered = new ArrayList<>();
      for (Task task : quest.tasks.values()) {
         if (!task.parentId.isEmpty()) continue;
         ordered.add(task);
         for (Task child : quest.tasks.values()) if (child.parentId.equals(task.id)) ordered.add(child);
      }
      return ordered;
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
         for (Task task : orderedTasks(quest)) {
            if (task.hidden && !task.done) continue;
            lines.add((task.parentId.isEmpty() ? "  " : "    ")
            + (task.done ? "§a✓" : "§7○") + " §f" + task.title);
         }
         lines.add("");
      }
      return lines;
   }

   public List<Entry> snapshot() {
      List<Entry> entries = new ArrayList<>();
      for (Quest quest : quests.values()) {
         if (quest.status == Status.AVAILABLE) continue;
         List<TaskEntry> tasks = orderedTasks(quest).stream().filter(task -> !task.hidden || task.done)
            .map(task -> new TaskEntry(task.title, task.done, !task.parentId.isEmpty())).toList();
         entries.add(new Entry(quest.id, quest.title, quest.source, quest.description, quest.status, tasks));
      }
      return List.copyOf(entries);
   }

   public CompoundTag save() {
      CompoundTag root = new CompoundTag();
      ListTag list = new ListTag();
      for (Quest quest : quests.values()) {
         CompoundTag entry = new CompoundTag();
         entry.putString("id", quest.id);
         entry.putString("title", quest.title);
         entry.putString("source", quest.source);
         entry.putString("description", quest.description);
         entry.putString("status", quest.status.name());
         ListTag tasks = new ListTag();
         for (Task task : quest.tasks.values()) {
            CompoundTag item = new CompoundTag();
            item.putString("id", task.id);
            item.putString("title", task.title);
            item.putString("parent", task.parentId);
            item.putBoolean("done", task.done);
            item.putBoolean("hidden", task.hidden);
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
         quest.source = entry.getString("source");
         try { quest.status = Status.valueOf(entry.getString("status")); }
         catch (IllegalArgumentException ignored) { quest.status = Status.AVAILABLE; }
         ListTag tasks = entry.getList("tasks", Tag.TAG_COMPOUND);
         for (int j = 0; j < tasks.size(); j++) {
            CompoundTag item = tasks.getCompound(j);
            String taskId = item.getString("id");
            if (taskId.isBlank()) continue;
            Task task = new Task(taskId, item.getString("title"), item.getString("parent"));
            task.done = item.getBoolean("done");
            task.hidden = item.getBoolean("hidden");
            quest.tasks.put(taskId, task);
         }
         quests.put(id, quest);
      }
   }

   private static final class Quest {
      private final String id;
      private String title;
      private String source = "";
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
      private final String parentId;
      private boolean done;
      private boolean hidden;
      private Task(String id, String title, String parentId) {
         this.id = id; this.title = title; this.parentId = parentId;
      }
   }
}

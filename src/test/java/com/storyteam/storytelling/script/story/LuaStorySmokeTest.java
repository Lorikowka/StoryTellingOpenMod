package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.client.content.overlays.Message;
import com.storyteam.storytelling.client.screen.QuestJournalSelection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public final class LuaStorySmokeTest {
   public static void main(String[] args) throws Exception {
      LuaStory fresh = LuaStory.parse(Files.readString(Path.of(args[0]), StandardCharsets.UTF_8), "900001.lua");
      if (fresh.id() != 900001 || !fresh.hasPart("main")) {
         throw new AssertionError("New Lua story did not load");
      }
      LuaStory journalTest = LuaStory.parse(Files.readString(Path.of(args[2]), StandardCharsets.UTF_8), "900036.lua");
      if (journalTest.id() != 900036 || !journalTest.hasPart("main")) {
         throw new AssertionError("Multiple-quest journal test did not load");
      }
      LuaStory subtaskStory = LuaStory.parse(Files.readString(Path.of(args[3]), StandardCharsets.UTF_8), "900037.lua");
      if (subtaskStory.id() != 900037 || !subtaskStory.hasPart("main")) {
         throw new AssertionError("Subtask journal test did not load");
      }
      LuaStory hiddenStory = LuaStory.parse(Files.readString(Path.of(args[4]), StandardCharsets.UTF_8), "900038.lua");
      if (hiddenStory.id() != 900038 || !hiddenStory.hasPart("main")) {
         throw new AssertionError("Hidden-subtask journal test did not load");
      }
      LuaStory itemStory = LuaStory.parse(Files.readString(Path.of(args[5]), StandardCharsets.UTF_8), "900041.lua");
      if (itemStory.id() != 900041 || !itemStory.hasPart("main")) {
         throw new AssertionError("Item receipt test story did not load");
      }
      LuaStory expanded = LuaStory.parse("""
         story(900004)
         part("main")
         think("Мысль")
         wait_ticks(20)
         wait_chat("готово")
         wait_flag("chapter_ready")
         blackout(10)
         flash(5)
         wait_pos(-34.5, 56, -9.5, 1.0, "minecraft:overworld", true)
         teleport_player(0.5, 64, 0.5, "minecraft:overworld", 90, 0)
         wait_item("minecraft:apple", 2)
         quest_create("routine_work", "Работа", "Описание")
         quest_source("routine_work", "Проводник")
         quest_task("routine_work", "exit", "Выйти")
         quest_start("routine_work")
         quest_task_complete("routine_work", "exit")
         quest_complete("routine_work")
         """, "expanded.lua");
      if (expanded.id() != 900004 || !expanded.hasPart("main")) {
         throw new AssertionError("Expanded Lua actions did not load");
      }
      LuaStory npcStory = LuaStory.parse("""
         story(900006)
         npc("guide", "minecraft:villager", "Проводник")
         part("main")
         npc_spawn("guide", 3.5, -60, 2.5)
         anchor("after_spawn")
         npc_move("guide", 6.5, -60, 2.5, 0.12)
         npc_wait_interact("guide")
         npc_remove("guide")
         """, "npc.lua");
      if (npcStory.id() != 900006 || !npcStory.hasPart("main")) {
         throw new AssertionError("NPC Lua actions did not load");
      }
      LuaStory gltfNpc = LuaStory.parse("""
         story(900008)
         npc_model("guide", "guide.glb", "Проводник")
         npc_hitbox("guide", 0.7, 1.4)
         part("main")
         npc_spawn("guide", 1, 64, 1)
         speaker_npc(1, "guide")
         speech(1, "Проверка портрета")
         npc_animation("guide", "wave", "once")
         npc_move("guide", 3, 64, 1, 0.2)
         npc_use_block("guide", 4, 64, 1)
         npc_animation("guide", "wave", "hold")
         npc_animation("guide", "auto", "loop")
         """, "gltf-npc.lua");
      if (gltfNpc.id() != 900008) throw new AssertionError("glTF NPC Lua actions did not load");
      try {
         LuaStory.parse("story(900013)\npart(\"main\")\nnpc_use_block(\"missing\", 1, 64, 1)", "invalid-use-block.lua");
         throw new AssertionError("Unknown NPC was accepted for npc_use_block");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("must be defined")) throw expected;
      }
      try {
         LuaStory.parse("story(900014)\nnpc_model(\"guide\", \"guide.glb\", \"Guide\")\npart(\"main\")\nnpc_use_block(\"guide\", 1.5, 64, 1)", "fractional-use-block.lua");
         throw new AssertionError("Fractional block coordinate was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("whole numbers")) throw expected;
      }
      LuaStory gltfJsonNpc = LuaStory.parse("""
         story(900011)
         npc_model("guide", "guide.gltf", "Проводник")
         part("main")
         wait_f8()
         """, "gltf-json-npc.lua");
      if (gltfJsonNpc.id() != 900011) throw new AssertionError(".gltf NPC Lua actions did not load");
      try {
         LuaStory.parse("""
            story(900012)
            npc_model("guide", "guide.glb", "Проводник")
            part("main")
            npc_animation("guide", "wave", "repeat")
            wait_f8()
            """, "invalid-animation-mode.lua");
         throw new AssertionError("Invalid NPC animation mode was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("mode once, hold or loop")) throw expected;
      }
      try {
         LuaStory.parse("story(900009)\nnpc_model(\"bad\", \"../other.glb\", \"Bad\")\npart(\"main\")\nwait_f8()", "invalid-model.lua");
         throw new AssertionError("Path traversal in NPC model was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("NPC model")) throw expected;
      }
      try {
         LuaStory.parse("story(900010)\nnpc_model(\"bad\", \"bad.glb\", \"Bad\")\nnpc_hitbox(\"bad\", 0, 1)\npart(\"main\")\nwait_f8()", "invalid-hitbox.lua");
         throw new AssertionError("Invalid NPC hitbox was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("npc_hitbox")) throw expected;
      }
      try {
         LuaStory.parse("story(900005)\npart(\"main\")\nwait_pos(0, 0, 0, -1)", "invalid-position.lua");
         throw new AssertionError("Invalid wait_pos radius was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("Invalid wait_pos")) {
            throw expected;
         }
      }
      QuestJournal quests = new QuestJournal();
      quests.apply(StoryEngineAdapter.Operation.ENSURE_QUEST, "test_quest", "Задание", "Описание");
      quests.apply(StoryEngineAdapter.Operation.SET_QUEST_SOURCE, "test_quest", "Регир");
      quests.apply(StoryEngineAdapter.Operation.ADD_TASK, "test_quest", "task", "Цель");
      quests.apply(StoryEngineAdapter.Operation.START_QUEST, "test_quest");
      quests.apply(StoryEngineAdapter.Operation.COMPLETE_TASK, "test_quest", "task");
      QuestJournal restoredQuests = new QuestJournal();
      restoredQuests.load(quests.save());
      if (restoredQuests.displayLines().size() < 3) throw new AssertionError("Quest journal did not survive NBT round trip");
      var journalEntries = restoredQuests.snapshot();
      if (journalEntries.size() != 1 || journalEntries.get(0).status() != QuestJournal.Status.COMPLETED
         || !journalEntries.get(0).tasks().get(0).done()
         || !journalEntries.get(0).source().equals("Регир")) {
         throw new AssertionError("Quest journal snapshot lost status or task progress");
      }
      restoredQuests.apply(StoryEngineAdapter.Operation.COMPLETE_QUEST, "test_quest");
      restoredQuests.apply(StoryEngineAdapter.Operation.START_QUEST, "test_quest");
      var replay = restoredQuests.snapshot().get(0);
      if (replay.status() != QuestJournal.Status.ACTIVE || replay.tasks().get(0).done()) {
         throw new AssertionError("Restarting a quest did not clear completed progress");
      }
      restoredQuests.apply(StoryEngineAdapter.Operation.FAIL_QUEST, "test_quest");
      restoredQuests.apply(StoryEngineAdapter.Operation.START_QUEST, "test_quest");
      if (restoredQuests.snapshot().get(0).status() != QuestJournal.Status.ACTIVE) {
         throw new AssertionError("Restarting a failed quest did not reactivate it");
      }
      QuestJournal nested = new QuestJournal();
      nested.apply(StoryEngineAdapter.Operation.ENSURE_QUEST, "nested", "Встреча", "Описание");
      nested.apply(StoryEngineAdapter.Operation.ADD_TASK, "nested", "prepare", "Подготовиться");
      nested.apply(StoryEngineAdapter.Operation.ADD_SUBTASK, "nested", "prepare", "place", "Выбрать место");
      nested.apply(StoryEngineAdapter.Operation.ADD_SUBTASK, "nested", "prepare", "time", "Назначить время");
      nested.apply(StoryEngineAdapter.Operation.START_QUEST, "nested");
      nested.apply(StoryEngineAdapter.Operation.COMPLETE_SUBTASK, "nested", "prepare", "place");
      QuestJournal restoredNested = new QuestJournal();
      restoredNested.load(nested.save());
      var partial = restoredNested.snapshot().get(0).tasks();
      if (partial.size() != 3 || partial.get(0).done() || !partial.get(1).child()
         || !partial.get(1).done() || partial.get(2).done()) {
         throw new AssertionError("Subtask hierarchy or partial progress did not survive NBT");
      }
      restoredNested.apply(StoryEngineAdapter.Operation.COMPLETE_SUBTASK, "nested", "prepare", "time");
      if (!restoredNested.snapshot().get(0).tasks().get(0).done()
         || restoredNested.snapshot().get(0).status() != QuestJournal.Status.COMPLETED) {
         throw new AssertionError("Completing all subtasks did not complete their parent and quest");
      }
      restoredNested.apply(StoryEngineAdapter.Operation.START_QUEST, "nested");
      if (restoredNested.snapshot().get(0).tasks().stream().anyMatch(QuestJournal.TaskEntry::done)) {
         throw new AssertionError("Restarting a quest did not reset subtasks");
      }
      restoredNested.apply(StoryEngineAdapter.Operation.ADD_HIDDEN_SUBTASK,
         "nested", "prepare", "secret", "Найти проход");
      if (restoredNested.snapshot().get(0).tasks().size() != 3) {
         throw new AssertionError("Unfinished hidden subtask was exposed in journal");
      }
      restoredNested.apply(StoryEngineAdapter.Operation.COMPLETE_SUBTASK, "nested", "prepare", "place");
      restoredNested.apply(StoryEngineAdapter.Operation.COMPLETE_SUBTASK, "nested", "prepare", "time");
      if (restoredNested.snapshot().get(0).status() != QuestJournal.Status.ACTIVE) {
         throw new AssertionError("Hidden subtask did not keep quest active");
      }
      QuestJournal hiddenReloaded = new QuestJournal();
      hiddenReloaded.load(restoredNested.save());
      if (hiddenReloaded.snapshot().get(0).tasks().size() != 3
         || hiddenReloaded.snapshot().get(0).status() != QuestJournal.Status.ACTIVE) {
         throw new AssertionError("Hidden subtask was not preserved across world reload");
      }
      hiddenReloaded.apply(StoryEngineAdapter.Operation.COMPLETE_SUBTASK, "nested", "prepare", "secret");
      if (hiddenReloaded.snapshot().get(0).status() != QuestJournal.Status.COMPLETED
         || hiddenReloaded.snapshot().get(0).tasks().size() != 4) {
         throw new AssertionError("Revealed hidden subtask did not finish quest");
      }
      CompoundTag playerData = new CompoundTag();
      LuaItemReceipts.record(playerData, "first-run", 7);
      CompoundTag restoredPlayerData = playerData.copy();
      if (!LuaItemReceipts.has(restoredPlayerData, "first-run", 7)
         || LuaItemReceipts.has(restoredPlayerData, "second-run", 7)
         || LuaItemReceipts.has(restoredPlayerData, "first-run", 8)) {
         throw new AssertionError("Item receipt did not distinguish runs and actions across NBT reload");
      }
      Path selectionDirectory = Files.createTempDirectory("storytelling-journal-selection-");
      try {
         Path selectionFile = selectionDirectory.resolve("selection.properties");
         var selection = new QuestJournalSelection.Value(QuestJournal.Status.COMPLETED, "test_quest", 2, 14);
         QuestJournalSelection.write(selectionFile, selection);
         if (!selection.equals(QuestJournalSelection.read(selectionFile))) {
            throw new AssertionError("Quest journal selection did not survive a file round trip");
         }
         Files.delete(selectionFile);
      } finally { Files.delete(selectionDirectory); }
      LuaBlockProject blocks = LuaBlockProject.create(900035);
      blocks.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.SPEECH, "Привет, \"мир\"!"));
      blocks.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.WAIT_F8, ""));
      blocks.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.WAIT_TICKS, "20"));
      String generated = blocks.generate();
      if (LuaStory.parse(generated, "900035.lua").id() != 900035 || !generated.contains("\\\"мир\\\""))
         throw new AssertionError("The block editor did not generate valid escaped Lua");
      LuaBlockProject branching = LuaBlockProject.create(900040);
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.CHOICE, "route",
         java.util.List.of("Как поступим?", "Пойти", "Остаться")));
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.BRANCH, "route",
         java.util.List.of("1", "go", "stay")));
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.ANCHOR, "stay"));
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.SPEECH, "Остаёмся"));
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.GOTO, "final"));
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.ANCHOR, "go"));
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.SET_FLAG, "went",
         java.util.List.of("true")));
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.SPEECH, "Идём"));
      String branchSource = branching.generate();
      if (LuaStory.parse(branchSource, "900040.lua").id() != 900040
         || !branchSource.contains("var_if(\"route\", \"1\", \"go\", \"stay\")")) {
         throw new AssertionError("Block choices and forward branches did not generate valid Lua");
      }
      branching.blocks().add(new LuaBlockProject.Block(LuaBlockProject.Kind.GOTO, "stay"));
      try {
         branching.generate();
         throw new AssertionError("Block editor accepted a backward branch");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("ниже")) throw expected;
      }
      Path storyDirectory = Files.createTempDirectory("storytelling-lua-stories-");
      try {
         LuaStory.initializeStoryDirectory(storyDirectory);
         try (var files = Files.list(storyDirectory)) {
            if (files.findAny().isPresent()) throw new AssertionError("The author directory is not empty");
         }
         Files.copy(Path.of(args[0]), storyDirectory.resolve("900001.lua"));
         Path registeredGltfStory = storyDirectory.resolve("900032.lua");
         Files.copy(Path.of(args[1]), registeredGltfStory);
         String editedExample = Files.readString(storyDirectory.resolve("900001.lua")) + "\n-- edited by user\n";
         Files.writeString(storyDirectory.resolve("900001.lua"), editedExample);
         LuaStory.initializeStoryDirectory(storyDirectory);
         if (!editedExample.equals(Files.readString(storyDirectory.resolve("900001.lua")))) {
            throw new AssertionError("Existing Lua story was overwritten");
         }
         ScriptEnvironment environment = new ScriptEnvironment(null);
         environment.addChatMessage(new Message("guide", Component.literal("Проводник"), Component.empty(),
            0, 200, 15, Component.literal("Помни меня")));
         ScriptEnvironment restored = new ScriptEnvironment(null);
         restored.deserializeChatHistory(environment.serializeChatHistory());
         if (restored.getChatHistory().size() != 1
            || !"Помни меня".equals(restored.getChatHistory().get(0).message().getString())) {
            throw new AssertionError("Story chat history did not survive NBT round trip");
         }
         environment.registerLuaStories(storyDirectory);
         if (!environment.luaStories.containsKey(900001) || !environment.constructors.containsKey(900001)) {
            throw new AssertionError("New Lua story was not registered");
         }
         if (!environment.luaStories.containsKey(900032) || !environment.constructors.containsKey(900032)) {
            throw new AssertionError("The 900032 glTF story file was not registered");
         }
         LuaScriptInstance firstRun = (LuaScriptInstance) environment.createInstance(900001);
         if (firstRun.getMainPerformer() == null) {
            throw new AssertionError("New Lua story has no main performer");
         }
         CompoundTag savedRun = firstRun.serializeNBT();
         if (!firstRun.runToken.equals(LuaScriptInstance.readRunToken(savedRun, "missing"))) {
            throw new AssertionError("Lua run token did not survive script serialization");
         }
         if (!"legacy".equals(LuaScriptInstance.readRunToken(new CompoundTag(), "legacy")))
            throw new AssertionError("Old Lua saves did not receive a fresh receipt token");
         LuaScriptInstance nextRun = (LuaScriptInstance) environment.createInstance(900001);
         if (firstRun.runToken.equals(nextRun.runToken)) {
            throw new AssertionError("Fresh Lua run reused an old item receipt token");
         }
         LuaStoryValidator.Report clean = LuaStoryValidator.validate(storyDirectory, java.util.Set.of());
         if (clean.checked() != 2 || !clean.valid()) {
            throw new AssertionError("Valid Lua files failed validation: " + clean.issues());
         }
         Files.writeString(storyDirectory.resolve("900090.lua"), "story(900090)\npart(\"main\")\nspeech(1, \"unfinished\n");
         Files.writeString(storyDirectory.resolve("900091.lua"), "story(900092)\npart(\"main\")\nwait_f8()\n");
         LuaStoryValidator.Report invalid = LuaStoryValidator.validate(storyDirectory, java.util.Set.of(900001));
         if (invalid.checked() != 4
            || invalid.issues().stream().noneMatch(issue -> issue.file().equals("900090.lua"))
            || invalid.issues().stream().noneMatch(issue -> issue.file().equals("900091.lua") && issue.message().contains("filename"))
            || invalid.issues().stream().noneMatch(issue -> issue.file().equals("900001.lua") && issue.message().contains("Java script"))) {
            throw new AssertionError("Validator missed syntax, filename or ID errors: " + invalid.issues());
         }
         Path authorFile = storyDirectory.resolve("draft.txt");
         StoryFileWriter.write(authorFile, "first", true);
         StoryFileWriter.write(authorFile, "second", true);
         if (!"second".equals(Files.readString(authorFile))
            || !"first".equals(Files.readString(storyDirectory.resolve("draft.txt.bak")))) {
            throw new AssertionError("Story file backup or replacement failed");
         }
      } finally {
         try (var paths = Files.walk(storyDirectory)) {
            for (Path path : paths.sorted(java.util.Comparator.reverseOrder()).toList()) {
               path.toFile().deleteOnExit();
            }
         }
      }
      System.out.println("Lua stories parsed and registered: 900001, 900032, 900036, 900037, 900038 and 900041");
   }
}

package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.content.overlays.Message;
import com.storyteam.storytelling.client.screen.QuestJournalSelection;
import com.storyteam.storytelling.init.ScriptCategory;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.action.ChatMessageAction;
import com.storyteam.storytelling.script.performer.action.BlockArea;
import com.storyteam.storytelling.script.performer.action.DelayAction;
import com.storyteam.storytelling.script.performer.action.F8PressAction;
import com.storyteam.storytelling.script.performer.action.SpeechMessageAction;
import com.storyteam.storytelling.script.story.camera.CameraShotAction;
import com.storyteam.storytelling.script.story.camera.NpcCameraFlightAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public final class LuaStorySmokeTest {
   public static void main(String[] args) throws Exception {
      checkSafeModules();
      checkStoryPacks();
      checkSpeechSpeakers();
      checkNpcInstances();
      checkNpcPersistentState();
      checkDoorAction();
      checkTrapdoorAction();
      checkSpeechResumeState();
      checkF8ResumeState();
      checkDelayResumeState();
      checkChatResumeState();
      checkCameraResumeState();
      checkJumpCancelsAction();
      checkParallelWaitSave();
      LuaStory parallelExample = LuaStory.parse(Files.readString(Path.of(args[13]), StandardCharsets.UTF_8), "900063.lua");
      if (parallelExample.id() != 900063 || !parallelExample.hasPart("short_wait") || !parallelExample.hasPart("long_wait")) {
         throw new AssertionError("Parallel wait example did not load");
      }
      checkSceneScope();
      checkWaitArea();
      if (!java.util.Arrays.equals(ScriptCategory.of(-100, -999).availableScripts(id -> id == -100), new int[] {-100})
         || java.util.Arrays.stream(ScriptCategory.allWithoutMechanics().getScripts()).anyMatch(id -> id == -100))
         throw new AssertionError("Legacy script categories expose unavailable or mechanical entries");
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
      LuaStory hudStory = LuaStory.parse(Files.readString(Path.of(args[6]), StandardCharsets.UTF_8), "900042.lua");
      if (hudStory.id() != 900042 || !hudStory.hasPart("main"))
         throw new AssertionError("HUD hint story did not load");
      LuaStory leverStory = LuaStory.parse(Files.readString(Path.of(args[7]), StandardCharsets.UTF_8), "900043.lua");
      if (leverStory.id() != 900043 || !leverStory.hasPart("main"))
         throw new AssertionError("Idempotent lever story did not load");
      LuaStory chapter = LuaStory.parse(Files.readString(Path.of(args[8]), StandardCharsets.UTF_8), "900050.lua");
      if (chapter.id() != 900050 || !chapter.hasPart("main"))
         throw new AssertionError("Integration chapter did not load");
      LuaStory sceneExample = LuaStory.parse(Files.readString(Path.of(args[9]), StandardCharsets.UTF_8), "900055.lua");
      if (sceneExample.id() != 900055 || !sceneExample.hasPart("main"))
         throw new AssertionError("Scene-state example did not load");
      LuaStory migrationExample = LuaStory.parse(Files.readString(Path.of(args[10]), StandardCharsets.UTF_8), "900056.lua");
      if (migrationExample.id() != 900056 || !migrationExample.hasPart("main"))
         throw new AssertionError("NPC state migration example did not load");
      LuaStory doorExample = LuaStory.parse(Files.readString(Path.of(args[11]), StandardCharsets.UTF_8), "900057.lua");
      if (doorExample.id() != 900057 || !doorExample.hasPart("main"))
         throw new AssertionError("NPC door example did not load");
      LuaStory trapdoorExample = LuaStory.parse(Files.readString(Path.of(args[12]), StandardCharsets.UTF_8), "900058.lua");
      if (trapdoorExample.id() != 900058 || !trapdoorExample.hasPart("main"))
         throw new AssertionError("NPC trapdoor example did not load");
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
      var started = quests.snapshot();
      if (!"Задание «Задание» начато!".equals(StoryEngineAdapter.questNotification(
         StoryEngineAdapter.Operation.START_QUEST, "test_quest", java.util.List.of(), started)))
         throw new AssertionError("Quest start notification was not generated");
      quests.apply(StoryEngineAdapter.Operation.COMPLETE_TASK, "test_quest", "task");
      var finished = quests.snapshot();
      if (!"Задание «Задание» завершено!".equals(StoryEngineAdapter.questNotification(
         StoryEngineAdapter.Operation.COMPLETE_TASK, "test_quest", started, finished))
         || StoryEngineAdapter.questNotification(StoryEngineAdapter.Operation.COMPLETE_TASK,
            "test_quest", finished, finished) != null)
         throw new AssertionError("Quest change notification repeated or had the wrong status");
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
         firstRun.sceneVariables.set("phase", "opening");
         firstRun.getFlags().set("chapter", "one");
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
         if (!savedRun.contains("sceneVariables", 10)) {
            throw new AssertionError("Scene variables were not serialized with the Lua run");
         }
         nextRun.sceneVariables.deserializeNBT(savedRun.getCompound("sceneVariables"));
         if (!"opening".equals(nextRun.sceneVariables.get("phase"))) {
            throw new AssertionError("Scene variables did not survive NBT round trip");
         }
         nextRun.sceneVariables.getFlags().clear();
         if (nextRun.sceneVariables.has("phase") || !"one".equals(firstRun.getFlags().get("chapter"))) {
            throw new AssertionError("Scene reset affected script flags or retained scene variables");
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
      System.out.println("Lua stories parsed and registered: 900001, 900032, 900036, 900037, 900038, 900041, 900042, 900043, 900050, 900055, 900056, 900057 and 900058");
   }

   private static void checkParallelWaitSave() throws Exception {
      ScriptEnvironment environment = new ScriptEnvironment(null);
      environment.luaStories.put(900063, LuaStory.parse("""
         story(900063)
         part("main")
         parallel_start("short")
         parallel_start("long")
         parallel_join("short", "long")
         part("short")
         wait_ticks(1)
         wait_ticks(10)
         part("long")
         wait_ticks(20)
         """, "parallel-waits.lua"));
      if (!environment.luaStories.get(900063).hasPart("long")) {
         throw new AssertionError("Parallel wait branch was not parsed");
      }
      try {
         LuaStory.parse("story(900064)\npart('main')\nparallel_start('missing')\nparallel_join('missing')", "bad-parallel.lua");
         throw new AssertionError("Missing parallel branch was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("Parallel branch")) throw expected;
      }
      LuaScriptInstance original = new LuaScriptInstance(environment, 900063);
      original.startParallelBranch("short");
      original.startParallelBranch("long");
      // The smoke runner has no Forge client/server bootstrap, so advance the checkpointed
      // actions directly and set the first branch's action index as a completed step would.
      DelayAction<?> shortOriginalWait = (DelayAction<?>) original.getParallelPerformer("short").getActions().get(1);
      DelayAction<?> longOriginalWait = (DelayAction<?>) original.getParallelPerformer("long").getActions().get(0);
      shortOriginalWait.tick();
      longOriginalWait.tick();
      longOriginalWait.tick();
      longOriginalWait.tick();
      var indexField = Performer.class.getDeclaredField("actionIndex");
      indexField.setAccessible(true);
      indexField.setInt(original.getParallelPerformer("short"), 1);
      CompoundTag saved = original.serializeNBT();
      LuaScriptInstance restored = new LuaScriptInstance(environment, 900063);
      restored.deserializeNBT(saved);
      ScriptPerformer shortBranch = restored.getParallelPerformer("short");
      ScriptPerformer longBranch = restored.getParallelPerformer("long");
      if (shortBranch == null || longBranch == null || shortBranch.getActionIndex() != 1
         || longBranch.getActionIndex() != 0 || restored.isParallelBranchDone("short")
         || restored.isParallelBranchDone("long")) {
         throw new AssertionError("Parallel waits lost their independent action indexes after save");
      }
      DelayAction<?> shortWait = (DelayAction<?>) shortBranch.getActions().get(1);
      DelayAction<?> longWait = (DelayAction<?>) longBranch.getActions().get(0);
      if (shortWait.saveActionState().getInt("remaining") != 9
         || longWait.saveActionState().getInt("remaining") != 17) {
         throw new AssertionError("Parallel waits lost remaining ticks after save");
      }
      indexField.setInt(longBranch, longBranch.getActionsAmount());
      if (!restored.isParallelBranchDone("long") || restored.isParallelBranchDone("short")) {
         throw new AssertionError("Parallel branch completion affected another branch");
      }
      LuaScriptInstance restoredAgain = new LuaScriptInstance(environment, 900063);
      restoredAgain.deserializeNBT(restored.serializeNBT());
      if (!restoredAgain.isParallelBranchDone("long") || restoredAgain.getParallelPerformer("short") == null) {
         throw new AssertionError("Completed parallel branch was restarted after save");
      }
   }

   private static void checkWaitArea() {
      BlockArea area = BlockArea.between(2, -59, 9, 0, -61, 8);
      if (!area.contains(0, -61, 8) || !area.contains(2, -59, 9)
         || !area.contains(1, -60, 8) || area.contains(3, -60, 8)
         || area.contains(1, -62, 8)) {
         throw new AssertionError("wait_area bounds are not inclusive or normalized");
      }
      LuaStory story = LuaStory.parse("story(900059)\npart(\"main\")\nwait_area(2, -59, 9, 0, -61, 8)",
         "wait-area.lua");
      if (story.id() != 900059 || !story.hasPart("main"))
         throw new AssertionError("wait_area did not register a story step");
      try {
         LuaStory.parse("story(900060)\npart(\"main\")\nwait_area(0, 0, 0, 1, 1, 1, \"bad dimension\")",
            "invalid-area.lua");
         throw new AssertionError("Invalid wait_area dimension was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("Invalid wait_area")) throw expected;
      }
   }

   private static void checkSceneScope() {
      LuaStory.parse("""
         story(900055)
         part('main')
         var_set('phase', 'opening', 'scene')
         scene_reset()
         var_if('phase', 'opening', 'old', 'new', 'scene')
         anchor('old')
         wait_f8()
         anchor('new')
         wait_f8()
         """, "scene-state.lua");
   }

   private static void checkNpcPersistentState() {
      ScriptEnvironment original = new ScriptEnvironment(null);
      original.setVariable("npc:lua_900053_guide_a", "trust", "5", null);
      original.setVariable("npc:lua_900053_guide_b", "trust", "2", null);
      ScriptEnvironment restored = new ScriptEnvironment(null);
      restored.loadNpcVariables(original.saveNpcVariables());
      if (!"5".equals(restored.getVariable("npc:lua_900053_guide_a", "trust", null))
         || !"2".equals(restored.getVariable("npc:lua_900053_guide_b", "trust", null))) {
         throw new AssertionError("NPC variables were lost or shared across instances after NBT reload");
      }
      ScriptEnvironment migration = new ScriptEnvironment(null);
      migration.setVariable("npc:lua_900056_old_guide", "trust", "5", null);
      migration.setVariable("npc:lua_900056_old_guide", "met", "yes", null);
      migration.setVariable("npc:lua_900056_new_guide", "trust", "9", null);
      if (!migration.migrateNpcVariables(900056, "old_guide", "new_guide")
         || migration.migrateNpcVariables(900056, "old_guide", "new_guide")) {
         throw new AssertionError("NPC state migration was not idempotent");
      }
      ScriptEnvironment migratedReload = new ScriptEnvironment(null);
      migratedReload.loadNpcVariables(migration.saveNpcVariables());
      if (migratedReload.getVariable("npc:lua_900056_old_guide", "met", null) != null
         || !"yes".equals(migratedReload.getVariable("npc:lua_900056_new_guide", "met", null))
         || !"9".equals(migratedReload.getVariable("npc:lua_900056_new_guide", "trust", null))) {
         throw new AssertionError("NPC state migration lost values or overwrote newer data");
      }
      LuaStory.parse("""
         story(900056)
         npc{key='new_guide',type='minecraft:villager',name='Guide'}
         part('main')
         npc_state_migrate('old_guide', 'new_guide')
         var_if('met', 'yes', 'known', 'new', 'npc:new_guide')
         anchor('known')
         wait_f8()
         anchor('new')
         wait_f8()
         """, "npc-state-migration.lua");
      LuaStory.parse("""
         story(900053)
         local guide = npc{key='guide_a',type='minecraft:villager',name='Guide'}
         part('main')
         guide:state_set('trust', 5)
         guide:state_if('trust', 5, 'trusted', 'unknown')
         anchor('trusted')
         wait_f8()
         anchor('unknown')
         wait_f8()
         """, "npc-state.lua");
      try {
         LuaStory.parse("story(900053)\npart('main')\nvar_set('trust', 5, 'npc:missing')", "missing-npc-state.lua");
         throw new AssertionError("Unknown NPC state scope was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("Unknown variable scope or NPC")) throw expected;
      }
   }

   private static void checkDoorAction() {
      LuaStory.parse("""
         story(900057)
         local guide = npc{key='door_guide',type='minecraft:villager',name='Guide'}
         part('main')
         guide:set_door(1, 64, 9, true)
         npc_set_door('door_guide', 1, 64, 9, false, 'minecraft:overworld')
         """, "npc-set-door.lua");
      try {
         LuaStory.parse("story(900057)\npart('main')\nnpc_set_door('missing', 1, 64, 9, true)", "missing-door-npc.lua");
         throw new AssertionError("Door action accepted an unknown NPC");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("Unknown NPC for npc_set_door")) throw expected;
      }
   }

   private static void checkTrapdoorAction() {
      LuaStory.parse("""
         story(900058)
         local guide = npc{key='trapdoor_guide',model='reg.gltf',name='Guide'}
         part('main')
         guide:set_trapdoor(1, 64, 9, true)
         npc_set_trapdoor('trapdoor_guide', 1, 64, 9, false, 'minecraft:overworld')
         """, "npc-set-trapdoor.lua");
      try {
         LuaStory.parse("story(900058)\npart('main')\nnpc_set_trapdoor('missing', 1, 64, 9, true)", "missing-trapdoor-npc.lua");
         throw new AssertionError("Trapdoor action accepted an unknown NPC");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("Unknown NPC for npc_set_trapdoor")) throw expected;
      }
   }

   private static void checkJumpCancelsAction() {
      class TestPerformer extends Performer<TestPerformer> { }
      TestPerformer performer = new TestPerformer();
      int[] stops = {0};
      performer.addAction(new PerformerAction<>(performer) {
         @Override public void stop() { stops[0]++; }
      });
      performer.addAction(new PerformerAction<>(performer) { });
      performer.jumpTo(1, true);
      if (stops[0] != 1 || performer.getActionIndex() != 1)
         throw new AssertionError("Jump did not cancel the active action");
   }

   private static void checkCameraResumeState() {
      CompoundTag started = new CompoundTag();
      started.putBoolean("started", true);

      ScriptPerformer originalShot = new ScriptPerformer(null);
      CameraShotAction shot = new CameraShotAction(originalShot, 0, 64, 0, 0, 0, 100);
      originalShot.addAction(shot);
      shot.restoreActionState(started);
      ScriptPerformer restoredShot = new ScriptPerformer(null);
      CameraShotAction resumedShot = new CameraShotAction(restoredShot, 0, 64, 0, 0, 0, 100);
      restoredShot.addAction(resumedShot);
      restoredShot.deserializeNBT(originalShot.serializeNBT());
      if (!resumedShot.isDone()) throw new AssertionError("Interrupted camera shot replayed after reload");

      ScriptPerformer originalFlight = new ScriptPerformer(null);
      NpcCameraFlightAction flight = new NpcCameraFlightAction(originalFlight, null, 60, 600, 1.6);
      originalFlight.addAction(flight);
      flight.restoreActionState(started);
      ScriptPerformer restoredFlight = new ScriptPerformer(null);
      NpcCameraFlightAction resumedFlight = new NpcCameraFlightAction(restoredFlight, null, 60, 600, 1.6);
      restoredFlight.addAction(resumedFlight);
      restoredFlight.deserializeNBT(originalFlight.serializeNBT());
      if (!resumedFlight.isDone()) throw new AssertionError("Interrupted NPC camera flight replayed after reload");
   }

   private static void checkSpeechResumeState() {
      ScriptPerformer original = new ScriptPerformer(null);
      SpeechMessageAction displayed = new SpeechMessageAction(original, 0, null,
         () -> Component.literal("Сохранённая реплика"), true);
      original.addAction(displayed);
      displayed.restoreDisplayState(true, false);

      ScriptPerformer restored = new ScriptPerformer(null);
      SpeechMessageAction resumed = new SpeechMessageAction(restored, 0, null,
         () -> Component.literal("Сохранённая реплика"), true);
      restored.addAction(resumed);
      restored.deserializeNBT(original.serializeNBT());
      if (!resumed.wasPrinted() || resumed.wasRead())
         throw new AssertionError("Displayed speech state was lost after save reload");

      CompoundTag legacy = original.serializeNBT();
      legacy.remove("activeAction");
      ScriptPerformer olderRestored = new ScriptPerformer(null);
      SpeechMessageAction olderSpeech = new SpeechMessageAction(olderRestored, 0, null,
         () -> Component.literal("Сохранённая реплика"), true);
      olderRestored.addAction(olderSpeech);
      olderRestored.deserializeNBT(legacy);
      if (!olderSpeech.wasPrinted() || olderSpeech.wasRead())
         throw new AssertionError("Alpha.79 speech checkpoint was not restored");
   }

   private static void checkF8ResumeState() {
      ScriptPerformer original = new ScriptPerformer(null);
      F8PressAction pressed = new F8PressAction(original);
      original.addAction(pressed);
      pressed.handleEvent(EventType.F8_PRESS, null);

      ScriptPerformer restored = new ScriptPerformer(null);
      F8PressAction resumed = new F8PressAction(restored);
      restored.addAction(resumed);
      restored.deserializeNBT(original.serializeNBT());
      if (!resumed.isDone())
         throw new AssertionError("F8 press was lost after save reload");
   }

   private static void checkDelayResumeState() {
      ScriptPerformer original = new ScriptPerformer(null);
      int[] calls = {0};
      DelayAction<ScriptPerformer> waiting = new DelayAction<>(original, () -> { calls[0]++; return 6; });
      original.addAction(waiting);
      if (calls[0] != 1) throw new AssertionError("Delay source was evaluated more than once");
      waiting.tick();
      waiting.tick();

      ScriptPerformer restored = new ScriptPerformer(null);
      DelayAction<ScriptPerformer> resumed = new DelayAction<>(restored, () -> 99);
      restored.addAction(resumed);
      restored.deserializeNBT(original.serializeNBT());
      CompoundTag state = resumed.saveActionState();
      if (state.getInt("delay") != 6 || state.getInt("remaining") != 4)
         throw new AssertionError("Remaining wait ticks were lost after save reload");
   }

   private static void checkChatResumeState() {
      ScriptPerformer original = new ScriptPerformer(null);
      ChatMessageAction waiting = new ChatMessageAction(original, "готово"::equals, null, true);
      original.addAction(waiting);
      waiting.handleEvent(EventType.CHAT_MESSAGE_SENT, "готово");
      for (int i = 0; i < 3; i++) waiting.isDone();

      ScriptPerformer restored = new ScriptPerformer(null);
      ChatMessageAction resumed = new ChatMessageAction(restored, "готово"::equals, null, true);
      restored.addAction(resumed);
      restored.deserializeNBT(original.serializeNBT());
      CompoundTag state = resumed.saveActionState();
      if (!state.getBoolean("matched") || state.getInt("remainingDelay") != 7)
         throw new AssertionError("Accepted chat message was lost after save reload");
      for (int i = 0; i < 7; i++) {
         if (resumed.isDone()) throw new AssertionError("Chat wait completed before its saved delay");
      }
      if (!resumed.isDone()) throw new AssertionError("Chat wait did not complete after its saved delay");
   }

   private static void checkNpcInstances() {
      String source = """
         story(900053)
         local template = { key = 'original', type = 'minecraft:villager', name = 'Guide' }
         local first = npc_instance(template, 'guide_a')
         local second = npc_instance(template, 'guide_b', 'Second guide')
         if template.key ~= 'original' or first.key ~= 'guide_a' or second.key ~= 'guide_b' then
            error('NPC template was mutated or instance keys collided')
         end
         part('main')
         first:spawn(0, 64, 0)
         second:spawn(2, 64, 0)
         wait_f8()
         """;
      if (LuaStory.parse(source, "npc-instances.lua").id() != 900053) {
         throw new AssertionError("NPC instances did not load");
      }
      try {
         LuaStory.parse(source.replace("'guide_b'", "'guide_a'"), "duplicate-instance.lua");
         throw new AssertionError("Duplicate NPC instance key was accepted");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("duplicate NPC")) throw expected;
      }
   }

   private static void checkSpeechSpeakers() {
      try {
         LuaStory.parse("story(900052)\npart('main')\nspeech(1, 'hello')", "missing-speaker.lua");
         throw new AssertionError("Speech without a speaker passed validation");
      } catch (IllegalArgumentException expected) {
         if (!expected.getMessage().contains("Speaker 1 is not declared")) throw expected;
      }
      if (LuaStory.parse("story(900052)\npart('main')\nspeaker(1, 'System')\nspeech(1, 'hello')", "speaker.lua").id() != 900052) {
         throw new AssertionError("Declared speaker was rejected");
      }
   }

   private static void checkStoryPacks() throws Exception {
      Path root = Files.createTempDirectory("story-pack-test-");
      Path pack = Files.createDirectories(root.resolve("packs/demo"));
      Files.writeString(root.resolve("900001.lua"), "story(900001)\npart(\"main\")\nwait_f8()\n");
      Files.writeString(pack.resolve("storypack.json"), """
         {"id":"demo","version":"1.0.0","api":1,"stories":["900052.lua"]}
         """);
      Files.writeString(pack.resolve("lines.lua"), "return { text = 'ready' }");
      Files.writeString(pack.resolve("900052.lua"), "local lines = require('lines')\nif lines.text ~= 'ready' then error('module root') end\nstory(900052)\npart(\"main\")\nwait_f8()\n");
      try {
         StoryPackCatalog.Scan scan = StoryPackCatalog.scan(root);
         if (scan.files().size() != 2 || !scan.issues().isEmpty()
            || !LuaStoryValidator.validate(root, java.util.Set.of()).valid()) {
            throw new AssertionError("Legacy and packaged stories were not discovered together");
         }
         if (LuaStory.load(StoryPackCatalog.resolveStoryFile(root, "packs/demo/900052.lua")).id() != 900052) {
            throw new AssertionError("Packaged story cannot be reloaded by path");
         }
         Files.writeString(pack.resolve("900053.lua"), "story(900053)\npart(\"main\")\nwait_f8()\n");
         try {
            StoryPackCatalog.resolveStoryFile(root, "packs/demo/900053.lua");
            throw new AssertionError("Unlisted pack story was reloadable");
         } catch (IllegalArgumentException expected) { }
         Files.writeString(pack.resolve("storypack.json"), """
            {"id":"demo","version":"1.0.0","api":2,"stories":["900052.lua"]}
            """);
         if (LuaStoryValidator.validate(root, java.util.Set.of()).valid()) {
            throw new AssertionError("Unsupported pack API was accepted");
         }
      } finally {
         try (var paths = Files.walk(root)) {
            for (Path path : paths.sorted(java.util.Comparator.reverseOrder()).toList()) path.toFile().deleteOnExit();
         }
      }
   }

   private static void checkSafeModules() throws Exception {
      Path root = Files.createTempDirectory("story-lua-modules-");
      try {
         Files.createDirectories(root.resolve("characters"));
         Files.createDirectories(root.resolve("cycle"));
         Files.writeString(root.resolve("characters/hero.lua"), """
            loads = (loads or 0) + 1
            return { name = "Проводник", loads = loads }
            """, StandardCharsets.UTF_8);
         Files.writeString(root.resolve("900099.lua"), """
            local a = require("characters.hero")
            local b = require("characters.hero")
            if a ~= b or b.loads ~= 1 then error("module cache failed") end
            story(900099)
            part("main")
            speaker(0, a.name)
            speech(0, "Проверка модуля")
            """, StandardCharsets.UTF_8);
         if (LuaStory.load(root.resolve("900099.lua")).id() != 900099)
            throw new AssertionError("Lua character module did not load");

         Files.writeString(root.resolve("cycle/a.lua"), "return require(\"cycle.b\")", StandardCharsets.UTF_8);
         Files.writeString(root.resolve("cycle/b.lua"), "return require(\"cycle.a\")", StandardCharsets.UTF_8);
         Files.writeString(root.resolve("900100.lua"), "story(900100)\nrequire(\"cycle.a\")\npart(\"main\")\nwait_f8()", StandardCharsets.UTF_8);
         try {
            LuaStory.load(root.resolve("900100.lua"));
            throw new AssertionError("Cyclic Lua module import was accepted");
         } catch (IllegalArgumentException expected) {
            if (!expected.getMessage().contains("Cyclic Lua module import")) throw expected;
         }

         Files.writeString(root.resolve("900101.lua"), "story(900101)\nrequire(\"../outside\")\npart(\"main\")\nwait_f8()", StandardCharsets.UTF_8);
         try {
            LuaStory.load(root.resolve("900101.lua"));
            throw new AssertionError("Path traversal in Lua module import was accepted");
         } catch (IllegalArgumentException expected) {
            if (!expected.getMessage().contains("Invalid Lua module name")) throw expected;
         }
         Files.writeString(root.resolve("900101.lua"), "story(900101)\nrequire(\"" + "a".repeat(129)
            + "\")\npart(\"main\")\nwait_f8()", StandardCharsets.UTF_8);
         try {
            LuaStory.load(root.resolve("900101.lua"));
            throw new AssertionError("Oversized Lua module name was accepted");
         } catch (IllegalArgumentException expected) {
            if (!expected.getMessage().contains("Invalid Lua module name")) throw expected;
         }

         Files.writeString(root.resolve("characters/false_value.lua"), "return false", StandardCharsets.UTF_8);
         Files.writeString(root.resolve("characters/no_value.lua"), "noValueLoads = (noValueLoads or 0) + 1", StandardCharsets.UTF_8);
         Files.writeString(root.resolve("900102.lua"), """
            story(900102)
            if require("characters.false_value") ~= false then error("false module lost") end
            if require("characters.false_value") ~= false then error("false cache lost") end
            if require("characters.no_value") ~= true then error("nil module must return true") end
            if require("characters.no_value") ~= true or noValueLoads ~= 1 then error("nil cache lost") end
            part("main")
            wait_f8()
            """, StandardCharsets.UTF_8);
         if (LuaStory.load(root.resolve("900102.lua")).id() != 900102)
            throw new AssertionError("Lua module false/nil result was not cached");

         Files.writeString(root.resolve("characters/exact.lua"), "--" + "x".repeat(256 * 1024 - 2), StandardCharsets.UTF_8);
         Files.writeString(root.resolve("900103.lua"), "story(900103)\nrequire(\"characters.exact\")\npart(\"main\")\nwait_f8()", StandardCharsets.UTF_8);
         if (LuaStory.load(root.resolve("900103.lua")).id() != 900103)
            throw new AssertionError("Lua module at byte limit was rejected");
         Files.writeString(root.resolve("characters/large.lua"), "--" + "x".repeat(256 * 1024), StandardCharsets.UTF_8);
         Files.writeString(root.resolve("900103.lua"), "story(900103)\nrequire(\"characters.large\")\npart(\"main\")\nwait_f8()", StandardCharsets.UTF_8);
         try {
            LuaStory.load(root.resolve("900103.lua"));
            throw new AssertionError("Oversized Lua module was accepted");
         } catch (IllegalArgumentException expected) {
            if (!expected.getMessage().contains("Lua module exceeds")) throw expected;
         }

         Files.createDirectories(root.resolve("many"));
         StringBuilder sixtyFour = new StringBuilder("story(900104)\n");
         for (int i = 0; i < 64; i++) {
            Files.writeString(root.resolve("many/m" + i + ".lua"), "return true", StandardCharsets.UTF_8);
            sixtyFour.append("require(\"many.m").append(i).append("\")\n");
         }
         Files.writeString(root.resolve("900104.lua"), sixtyFour + "part(\"main\")\nwait_f8()", StandardCharsets.UTF_8);
         if (LuaStory.load(root.resolve("900104.lua")).id() != 900104)
            throw new AssertionError("The 64th Lua module was rejected");
         Files.writeString(root.resolve("many/m64.lua"), "return true", StandardCharsets.UTF_8);
         Files.writeString(root.resolve("900105.lua"), sixtyFour + "require(\"many.m64\")\npart(\"main\")\nwait_f8()", StandardCharsets.UTF_8);
         try {
            LuaStory.load(root.resolve("900105.lua"));
            throw new AssertionError("The 65th Lua module was accepted");
         } catch (IllegalArgumentException expected) {
            if (!expected.getMessage().contains("Too many Lua modules")) throw expected;
         }

         Path outside = Files.createTempFile("story-outside-", ".lua");
         try {
            try {
               LuaStoryPaths.checkedStoryFile(root, outside);
               throw new AssertionError("Lua story outside ST/scripts was accepted");
            } catch (IllegalArgumentException expected) {
               if (!expected.getMessage().contains("directly inside")) throw expected;
            }
         } finally {
            Files.deleteIfExists(outside);
         }
      } finally {
         try (var files = Files.walk(root)) {
            for (Path file : files.sorted(java.util.Comparator.reverseOrder()).toList()) Files.deleteIfExists(file);
         }
      }
   }
}

package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.client.content.overlays.Message;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.network.chat.Component;

public final class LuaStorySmokeTest {
   public static void main(String[] args) throws Exception {
      LuaStory s7 = LuaStory.parse(Files.readString(Path.of(args[0]), StandardCharsets.UTF_8), "s7.lua");
      if (s7.id() != 7 || !s7.hasPart("setup") || !s7.hasPart("main")) {
         throw new AssertionError("S7 Lua story did not load");
      }
      LuaStory fresh = LuaStory.parse(Files.readString(Path.of(args[1]), StandardCharsets.UTF_8), "900001.lua");
      if (fresh.id() != 900001 || !fresh.hasPart("main")) {
         throw new AssertionError("New Lua story did not load");
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
         quest_create("routine_work", "Работа", "Описание")
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
         npc_animation("guide", "wave", "hold")
         npc_animation("guide", "auto", "loop")
         """, "gltf-npc.lua");
      if (gltfNpc.id() != 900008) throw new AssertionError("glTF NPC Lua actions did not load");
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
      StoryEngineAdapter.invoke(StoryEngineAdapter.Operation.START_QUEST, "test_quest");
      if (!"test_quest".equals(com.storyengine.api.StoryApi.lastQuestId)) {
         throw new AssertionError("Optional Story Engine adapter did not invoke the public API");
      }
      Path storyDirectory = Files.createTempDirectory("storytelling-lua-stories-");
      try {
         LuaStory.initializeStoryDirectory(storyDirectory);
         Path registeredGltfStory = storyDirectory.resolve("900008.lua");
         Files.copy(Path.of(args[2]), registeredGltfStory);
         if (!Files.isRegularFile(storyDirectory.resolve("s7.lua")) || !Files.isRegularFile(storyDirectory.resolve("900001.lua"))) {
            throw new AssertionError("Default Lua stories were not installed");
         }
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
         if (!environment.luaStories.containsKey(900008) || !environment.constructors.containsKey(900008)) {
            throw new AssertionError("The 900008 glTF story file was not registered");
         }
         if (environment.createInstance(900001).getMainPerformer() == null) {
            throw new AssertionError("New Lua story has no main performer");
         }
      } finally {
         try (var paths = Files.walk(storyDirectory)) {
            for (Path path : paths.sorted(java.util.Comparator.reverseOrder()).toList()) {
               path.toFile().deleteOnExit();
            }
         }
      }
      System.out.println("Lua stories parsed and registered: S7, 900001, and 900008");
   }
}

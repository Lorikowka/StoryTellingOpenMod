package com.storyteam.storytelling.network;

import com.storyteam.storytelling.network.packets.ChatMessagePacket;
import com.storyteam.storytelling.network.packets.script.ClearLogLinesPacket;
import com.storyteam.storytelling.network.packets.script.ScriptUpdatePacket;
import com.storyteam.storytelling.network.packets.script.StartScriptPacket;
import com.storyteam.storytelling.network.packets.script.StopScriptPacket;
import com.storyteam.storytelling.network.types.Script;
import com.storyteam.storytelling.script.ScriptInstance;
import org.jetbrains.annotations.NotNull;

public class StoryServerHooks {
   public static void updateScript(@NotNull ScriptInstance script, boolean active) {
      StoryServer.getInstance().sendPacket(new ScriptUpdatePacket(Script.fromScriptInstance(script, active)), 1);
   }

   public static void updateScript(@NotNull ScriptInstance script, boolean active, double percent) {
      StoryServer.getInstance().sendPacket(new ScriptUpdatePacket(Script.fromScriptInstance(script, active, percent)), 1);
   }

   public static void startScript(@NotNull ScriptInstance script) {
      StoryServer.getInstance().sendPacket(new StartScriptPacket(script.getId()), 1);
   }

   public static void stopScript(@NotNull ScriptInstance script) {
      StoryServer.getInstance().sendPacket(new StopScriptPacket(script.getId()), 1);
   }

   public static void clearInstruction(@NotNull ScriptInstance script) {
      StoryServer.getInstance().sendPacket(new ClearLogLinesPacket(script.getId()), 1);
   }

   public static void onCommandRun(String command) {
      StoryServer.getInstance().sendPacket(new ChatMessagePacket("/" + command));
   }

   public static void sendCrashReport(String message, String log) {
   }
}

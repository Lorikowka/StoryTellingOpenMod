package com.storyteam.storytelling.network;

import com.storyteam.storytelling.script.ScriptInstance;
import org.jetbrains.annotations.NotNull;

public class StoryServerHooks {
   public static void updateScript(@NotNull ScriptInstance script, boolean active) {
   }

   public static void updateScript(@NotNull ScriptInstance script, boolean active, double percent) {
   }

   public static void startScript(@NotNull ScriptInstance script) {
   }

   public static void stopScript(@NotNull ScriptInstance script) {
   }

   public static void clearInstruction(@NotNull ScriptInstance script) {
   }

   public static void onCommandRun(String command) {
   }

   public static void sendCrashReport(String message, String log) {
   }
}

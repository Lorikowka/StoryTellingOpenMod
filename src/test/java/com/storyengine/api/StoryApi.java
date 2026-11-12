package com.storyengine.api;

/** Test double for the optional Story Engine API; never packaged into the mod JAR. */
public final class StoryApi {
   public static String lastQuestId;

   public static void startQuestAll(String id) {
      lastQuestId = id;
   }
}

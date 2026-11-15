package com.storyteam.storytelling.script.story;

import net.minecraft.nbt.CompoundTag;

/** Records grants in the same player data as the inventory. */
public final class LuaItemReceipts {
   private static final String ROOT = "StorytellingLuaItemReceipts";

   private LuaItemReceipts() { }

   public static boolean has(CompoundTag playerData, String runToken, int actionIndex) {
      return playerData.getCompound(ROOT).getBoolean(key(runToken, actionIndex));
   }

   public static void record(CompoundTag playerData, String runToken, int actionIndex) {
      CompoundTag receipts = playerData.getCompound(ROOT);
      receipts.putBoolean(key(runToken, actionIndex), true);
      playerData.put(ROOT, receipts);
   }

   private static String key(String runToken, int actionIndex) {
      if (runToken == null || runToken.isBlank() || actionIndex < 0)
         throw new IllegalArgumentException("Invalid Lua item receipt key");
      return runToken + "." + actionIndex;
   }
}

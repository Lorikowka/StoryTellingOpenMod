package com.storyteam.storytelling.system.simulator.cognition_tab.utils;

import java.util.HashMap;
import java.util.Map;

public class CustomUpgradeHolder {
   private static int upgradeIndex = 0;
   private static final Map<Integer, Boolean> upgrades = new HashMap<>();

   public static int getUpgradeIndex() {
      return upgradeIndex++;
   }

   public static boolean getCustomUpgradeState(int upgradeIndex) {
      return upgrades.getOrDefault(upgradeIndex, false);
   }

   public static void setCustomUpgradeState(int upgradeIndex, boolean state) {
      upgrades.put(upgradeIndex, state);
   }
}

package com.storyteam.storytelling.system.simulator.me_tab.fragment;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentInstanceStorage;
import com.storyteam.storytelling.system.simulator.me_tab.word.AnimatedWordQueue;
import com.storyteam.storytelling.system.simulator.me_tab.word.data.AnimatedWordData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.level.ServerPlayer;

public class FragmentInstanceManager {
   private static final Map<String, FragmentInstance> ACTIVE = new HashMap<>();
   private static final Map<String, FragmentInstance> HISTORY = new HashMap<>();

   public static FragmentInstance getActive(FragmentData data) {
      return ACTIVE.get(data.getId());
   }

   public static boolean isActive(FragmentData data) {
      return getActive(data) != null;
   }

   public static FragmentInstance getHistory(FragmentData data) {
      return HISTORY.get(data.getId());
   }

   public static Collection<FragmentInstance> allActive() {
      return ACTIVE.values();
   }

   public static Collection<FragmentInstance> allHistory() {
      return HISTORY.values();
   }

   public static FragmentData registerActive(FragmentData data) {
      ACTIVE.put(data.getId(), new FragmentInstance(data));
      return data;
   }

   public static void addActive(FragmentData data) {
      if (!ACTIVE.containsKey(data.getId()) && !HISTORY.containsKey(data.getId())) {
         ACTIVE.put(data.getId(), new FragmentInstance(data));
         AnimatedWordQueue.addWord(new AnimatedWordData(data));
      }
   }

   public static void removeActive(FragmentData data) {
      if (getActive(data) != null) {
         getActive(data).setPendingAction(FragmentInstance.MoveAction.REMOVE);
      }
   }

   private static void addActive(FragmentInstance fragmentInstance) {
      if (!ACTIVE.containsKey(fragmentInstance.getData().getId()) && !HISTORY.containsKey(fragmentInstance.getData().getId())) {
         ACTIVE.put(fragmentInstance.getData().getId(), fragmentInstance);
      }
   }

   public static void addHistory(FragmentData data) {
      HISTORY.putIfAbsent(data.getId(), new FragmentInstance(data));
   }

   private static void addHistory(FragmentInstance fragmentInstance) {
      HISTORY.putIfAbsent(fragmentInstance.getData().getId(), fragmentInstance);
   }

   public static void clearActive() {
      ACTIVE.clear();
   }

   public static void clearHistory() {
      HISTORY.clear();
   }

   public static void tickAll(ServerPlayer player) {
      for (FragmentInstance inst : new ArrayList<>(ACTIVE.values())) {
         inst.tick(player);
         processPendingAction(inst, player);
      }

      for (FragmentInstance inst : new ArrayList<>(HISTORY.values())) {
         processPendingAction(inst, player);
      }
   }

   private static void processPendingAction(FragmentInstance inst, ServerPlayer player) {
      switch (inst.getPendingAction()) {
         case TO_HISTORY:
            ACTIVE.remove(inst.getData().getId());
            HISTORY.put(inst.getData().getId(), inst);
            break;
         case TO_ACTIVE:
            HISTORY.remove(inst.getData().getId());
            ACTIVE.put(inst.getData().getId(), inst);
            break;
         case REMOVE:
            ACTIVE.remove(inst.getData().getId());
            if (HISTORY.containsKey(inst.getData().getId())) {
               inst.rollbackActions(player);
               HISTORY.remove(inst.getData().getId());
            }
         case NONE:
      }

      inst.setPendingAction(FragmentInstance.MoveAction.NONE);
   }

   public static void loadFromStorage(FragmentInstanceStorage.LoadResult result) {
      clearActive();
      clearHistory();
      result.active().forEach(FragmentInstanceManager::addActive);
      result.history().forEach(FragmentInstanceManager::addHistory);
   }
}

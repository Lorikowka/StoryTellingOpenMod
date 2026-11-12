package com.storyteam.storytelling.system.simulator.me_tab.fragment.storage;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FragmentInstanceStorage {
   private static final String FRAGMENTS_DIRECTORY = "fragments";
   private static final String FILE_NAME = "fragments.dat";

   @SubscribeEvent
   public static void scriptIoSave(ScriptIOEvent.Save event) {
      File file = event.getSystemDirectory("fragments");
      if (file.isDirectory() || file.mkdirs()) {
         save(file);
      }
   }

   @SubscribeEvent
   public static void scriptIoLoad(ScriptIOEvent.Load event) {
      File file = event.getSystemDirectory("fragments");
      if (file.isDirectory() || file.mkdirs()) {
         FragmentInstanceStorage.LoadResult result = load(file);
         FragmentInstanceManager.loadFromStorage(result);
      }
   }

   public static void save(File directory) {
      try {
         File file = new File(directory, "fragments.dat");
         CompoundTag root = new CompoundTag();
         CompoundTag activeTag = new CompoundTag();
         FragmentInstanceManager.allActive().forEach(inst -> activeTag.put(inst.getData().getId(), inst.serializeNBT()));
         root.put("active", activeTag);
         CompoundTag historyTag = new CompoundTag();
         FragmentInstanceManager.allHistory().forEach(inst -> historyTag.put(inst.getData().getId(), inst.serializeNBT()));
         root.put("history", historyTag);
         ScriptIO.save(root, file);
      } catch (Throwable var5) {
         if (var5 instanceof RuntimeException runtime) throw runtime;
         if (var5 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var5);
      }
   }

   public static FragmentInstanceStorage.LoadResult load(File directory) {
      try {
         File file = new File(directory, "fragments.dat");
         if (!file.exists()) {
            return new FragmentInstanceStorage.LoadResult(List.of(), List.of());
         } else {
            CompoundTag root = ScriptIO.read(file);
            if (root == null) {
               return new FragmentInstanceStorage.LoadResult(List.of(), List.of());
            } else {
               List<FragmentInstance> active = new ArrayList<>();
               List<FragmentInstance> history = new ArrayList<>();
               if (root.contains("active")) {
                  CompoundTag activeTag = root.getCompound("active");

                  for (String key : activeTag.getAllKeys()) {
                     FragmentInstance inst = createInstanceOrSkip(key, activeTag.getCompound(key));
                     if (inst != null) {
                        active.add(inst);
                     }
                  }
               }

               if (root.contains("history")) {
                  CompoundTag historyTag = root.getCompound("history");

                  for (String keyx : historyTag.getAllKeys()) {
                     FragmentInstance inst = createInstanceOrSkip(keyx, historyTag.getCompound(keyx));
                     if (inst != null) {
                        history.add(inst);
                     }
                  }
               }

               return new FragmentInstanceStorage.LoadResult(active, history);
            }
         }
      } catch (Throwable var9) {
         if (var9 instanceof RuntimeException runtime) throw runtime;
         if (var9 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var9);
      }
   }

   private static FragmentInstance createInstanceOrSkip(String key, CompoundTag tag) {
      FragmentData data = FragmentStorage.FRAGMENTS_MAP.get(key);
      if (data == null) {
         Storytelling.LOG.warn("Skipping unknown fragment instance: {}", key);
         return null;
      } else {
         FragmentInstance inst = new FragmentInstance(data);
         inst.deserializeNBT(tag);
         return inst;
      }
   }

   public static record LoadResult(List<FragmentInstance> active, List<FragmentInstance> history) {
   }
}

package com.storyteam.storytelling.system.simulator.me_tab.word;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.simulator.me_tab.word.data.AnimatedWordData;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AnimatedWordQueue {
   private static final Queue<AnimatedWordData> queue = new ArrayDeque<>();
   private static final String WORD_QUEUE_DIRECTORY = "word_queue";
   private static final String FILE_NAME = "word_queue.dat";

   public static void addWord(AnimatedWordData word) {
      queue.add(word);
   }

   public static CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      ListTag list = new ListTag();

      for (AnimatedWordData animatedWordData : queue) {
         CompoundTag wordTag = animatedWordData.serializeNBT();
         list.add(wordTag);
      }

      tag.put("word_queue", list);
      return tag;
   }

   public static void load(CompoundTag tag) {
      queue.clear();
      if (tag.contains("word_queue", 9)) {
         ListTag list = tag.getList("word_queue", 10);

         for (int i = 0; i < list.size(); i++) {
            CompoundTag wordTag = list.getCompound(i);
            AnimatedWordData data = new AnimatedWordData();
            data.deserializeNBT(wordTag);
            if (data.getText() != null) {
               queue.add(data);
            }
         }
      }
   }

   @SubscribeEvent
   public static void scriptIoSave(ScriptIOEvent.Save event) {
      File file = event.getSystemDirectory("word_queue");
      if (file.isDirectory() || file.mkdirs()) {
         save(file);
      }
   }

   @SubscribeEvent
   public static void scriptIoLoad(ScriptIOEvent.Load event) {
      File file = event.getSystemDirectory("word_queue");
      if (file.isDirectory() || file.mkdirs()) {
         load(file);
      }
   }

   public static void save(File directory) {
      try {
         File file = new File(directory, "word_queue.dat");
         CompoundTag root = save();
         ScriptIO.save(root, file);
      } catch (Throwable var3) {
         if (var3 instanceof RuntimeException runtime) throw runtime;
         if (var3 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var3);
      }
   }

   public static void load(File directory) {
      try {
         File file = new File(directory, "word_queue.dat");
         if (file.exists()) {
            CompoundTag root = ScriptIO.read(file);
            if (root != null) {
               load(root);
            }
         }
      } catch (Throwable var3) {
         if (var3 instanceof RuntimeException runtime) throw runtime;
         if (var3 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var3);
      }
   }

   @Generated
   public static Queue<AnimatedWordData> getQueue() {
      return queue;
   }
}

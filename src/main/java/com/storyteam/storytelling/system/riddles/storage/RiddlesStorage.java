package com.storyteam.storytelling.system.riddles.storage;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.riddles.data.RiddlesDef;
import com.storyteam.storytelling.util.ScriptUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RiddlesStorage {
   private static final String ROOT_DAT_FILE = "RiddlesST.dat";
   private static final String ROOT_TAG_NAME = "riddles";
   private static final Map<String, RiddlesDef> RIDDLES = new HashMap<>();

   public static void register(String id, RiddlesDef riddle) {
      RIDDLES.put(id, riddle);
   }

   public static RiddlesDef get(String id) {
      return RIDDLES.get(id);
   }

   public static Collection<RiddlesDef> all() {
      return RIDDLES.values();
   }

   public static List<RiddlesDef> allAsList() {
      return new ArrayList<>(RIDDLES.values());
   }

   public static Collection<String> getAllIds() {
      return RIDDLES.keySet();
   }

   public static void setSolvedForPersona(Persona persona, String riddleId, boolean solved) {
      CompoundTag rootTag = getRootTag();
      String personaName = persona.name();
      CompoundTag personaTag = rootTag.contains(personaName) ? rootTag.getCompound(personaName) : new CompoundTag();
      ListTag solvedList = personaTag.contains("solved") ? personaTag.getList("solved", 8) : new ListTag();
      if (solved) {
         boolean alreadySolved = solvedList.stream().anyMatch(tag -> ((StringTag)tag).getAsString().equals(riddleId));
         if (!alreadySolved) {
            solvedList.add(StringTag.valueOf(riddleId));
         }
      } else {
         solvedList.removeIf(tag -> ((StringTag)tag).getAsString().equals(riddleId));
      }

      personaTag.put("solved", solvedList);
      rootTag.put(personaName, personaTag);
   }

   public static boolean isSolvedByPersona(Persona persona, String riddleId) {
      CompoundTag rootTag = getRootTag();
      String personaName = persona.name();
      if (!rootTag.contains(personaName)) {
         return false;
      } else {
         CompoundTag personaTag = rootTag.getCompound(personaName);
         if (!personaTag.contains("solved")) {
            return false;
         } else {
            ListTag solvedList = personaTag.getList("solved", 8);
            return solvedList.stream().map(tag -> ((StringTag)tag).getAsString()).anyMatch(id -> id.equals(riddleId));
         }
      }
   }

   public static void setCanReadForPersona(Persona persona, boolean canRead) {
      CompoundTag rootTag = getRootTag();
      String personaName = persona.name();
      CompoundTag personaTag = rootTag.contains(personaName) ? rootTag.getCompound(personaName) : new CompoundTag();
      personaTag.putBoolean("canRead", canRead);
      rootTag.put(personaName, personaTag);
   }

   public static boolean canReadByPersona(Persona persona) {
      CompoundTag rootTag = getRootTag();
      String personaName = persona.name();
      if (!rootTag.contains(personaName)) {
         return false;
      } else {
         CompoundTag personaTag = rootTag.getCompound(personaName);
         return personaTag.getBoolean("canRead");
      }
   }

   @SubscribeEvent
   public static void onSave(ScriptIOEvent.Save event) {
      File dir = event.getSystemDirectory("riddles");
      if (!dir.exists()) {
         dir.mkdirs();
      }

      File file = new File(dir, "RiddlesST.dat");
      save(file);
   }

   @SubscribeEvent
   public static void onLoad(ScriptIOEvent.Load event) {
      File dir = event.getSystemDirectory("riddles");
      if (dir.exists()) {
         File file = new File(dir, "RiddlesST.dat");
         load(file);
      }
   }

   private static void save(File file) {
      CompoundTag tag = new CompoundTag();

      for (Entry<String, RiddlesDef> entry : RIDDLES.entrySet()) {
         CompoundTag riddleTag = new CompoundTag();
         riddleTag.putString("coordinate", entry.getValue().getCoordinate());
         riddleTag.putString("text", entry.getValue().getText());
         riddleTag.putString("answer", entry.getValue().getAnswer());
         riddleTag.putInt("reward", entry.getValue().getReward());
         riddleTag.putBoolean("visible", entry.getValue().isVisible());
         tag.put(entry.getKey(), riddleTag);
      }

      try {
         NbtIo.write(tag, file);
      } catch (IOException var5) {
         var5.printStackTrace();
      }
   }

   private static void load(File file) {
      if (file.exists()) {
         try {
            CompoundTag tag = NbtIo.read(file);

            for (Entry<String, RiddlesDef> entry : RIDDLES.entrySet()) {
               String key = entry.getKey();
               RiddlesDef riddle = entry.getValue();
               if (tag.contains(key)) {
                  CompoundTag riddleTag = tag.getCompound(key);
                  riddle.setId(key);
                  riddle.setCoordinate(riddleTag.getString("coordinate"));
                  riddle.setText(riddleTag.getString("text"));
                  riddle.setAnswer(riddleTag.getString("answer"));
                  riddle.setReward(riddleTag.getInt("reward"));
                  riddle.setVisible(riddleTag.getBoolean("visible"));
               }
            }

            save(file);
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }
   }

   private static CompoundTag getRootTag() {
      return getOrCreateTag(ScriptIO.WORLD_EXTRA_NBT, "riddles");
   }

   private static CompoundTag getOrCreateTag(CompoundTag tag, String tagName) {
      return ScriptUtils.getTagFromTag(tag, tagName);
   }
}

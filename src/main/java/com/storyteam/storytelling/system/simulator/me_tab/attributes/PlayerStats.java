package com.storyteam.storytelling.system.simulator.me_tab.attributes;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeData;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import java.io.File;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerStats {
   private static int level = 0;
   public static boolean isGuiWithAttributeScreenOpen = false;
   private static final Map<AttributeType, AttributeData> attributes = new EnumMap<>(AttributeType.class);
   private static final String STATS_DIRECTORY = "player_me_stats";
   private static final String FILE_NAME = "player_me_stats.dat";

   public static void setLevel(int level) {
      PlayerStats.level = Math.max(level, 0);
   }

   public static void addLevel(int level) {
      PlayerStats.level = PlayerStats.level + Math.max(level, 0);
   }

   public static AttributeData getBody() {
      return attributes.get(AttributeType.BODY);
   }

   public static AttributeData getMind() {
      return attributes.get(AttributeType.MIND);
   }

   public static AttributeData getWillpower() {
      return attributes.get(AttributeType.WILLPOWER);
   }

   public static void addBodyVP(int value, int parts) {
      getBody().add(value, parts);
   }

   public static void addMindVP(int value, int parts) {
      getMind().add(value, parts);
   }

   public static void addWillpowerVP(int value, int parts) {
      getWillpower().add(value, parts);
   }

   public static void addBodyP(int parts) {
      getBody().addParts(parts);
   }

   public static void addMindP(int parts) {
      getMind().addParts(parts);
   }

   public static void addWillpowerP(int parts) {
      getWillpower().addParts(parts);
   }

   public static void addBodyV(int value) {
      getBody().addValue(value);
   }

   public static void addMindV(int value) {
      getMind().addValue(value);
   }

   public static void addWillpowerV(int value) {
      getWillpower().addValue(value);
   }

   public static Collection<AttributeData> getAttributes() {
      return attributes.values();
   }

   public static AttributeData getAttribute(AttributeType type) {
      return attributes.get(type);
   }

   private static CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      tag.putInt("level", level);
      ListTag list = new ListTag();

      for (Entry<AttributeType, AttributeData> entry : attributes.entrySet()) {
         CompoundTag attrTag = new CompoundTag();
         attrTag.putString("id", entry.getKey().name());
         attrTag.put("data", entry.getValue().save());
         list.add(attrTag);
      }

      tag.put("attributes", list);
      return tag;
   }

   private static void load(CompoundTag tag) {
      level = tag.getInt("level");
      attributes.clear();

      for (AttributeType type : AttributeType.values()) {
         attributes.put(type, new AttributeData(type));
      }

      ListTag list = tag.getList("attributes", 10);

      for (int i = 0; i < list.size(); i++) {
         CompoundTag attrTag = list.getCompound(i);
         String id = attrTag.getString("id");

         try {
            AttributeType type = AttributeType.valueOf(id);
            AttributeData data = new AttributeData(type);
            data.load(attrTag.getCompound("data"));
            attributes.put(type, data);
         } catch (IllegalArgumentException var7) {
            System.err.println("Такой характеристики больше нет: " + id);
         }
      }
   }

   @SubscribeEvent
   public static void scriptIoSave(ScriptIOEvent.Save event) {
      File file = event.getSystemDirectory("player_me_stats");
      if (file.isDirectory() || file.mkdirs()) {
         save(file);
      }
   }

   @SubscribeEvent
   public static void scriptIoLoad(ScriptIOEvent.Load event) {
      File file = event.getSystemDirectory("player_me_stats");
      if (file.isDirectory() || file.mkdirs()) {
         load(file);
      }
   }

   public static void save(File directory) {
      try {
         File file = new File(directory, "player_me_stats.dat");
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
         File file = new File(directory, "player_me_stats.dat");
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
   public static int getLevel() {
      return level;
   }

   static {
      for (AttributeType type : AttributeType.values()) {
         attributes.put(type, new AttributeData(type));
      }
   }
}

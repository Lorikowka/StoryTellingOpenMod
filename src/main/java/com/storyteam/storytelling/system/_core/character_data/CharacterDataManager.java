package com.storyteam.storytelling.system._core.character_data;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.story.characters.Character;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.util.java.Cast;

public class CharacterDataManager {
   private static final Map<String, CompoundTag> CACHE = new ConcurrentHashMap<>();
   private static final Map<String, Map<String, CharacterDataAttachment>> ATTACHMENTS = new ConcurrentHashMap<>();
   private static Path CHARS_PATH;

   public static <T extends CharacterDataAttachment> T getExistingAttachment(@NotNull Character<?> charId, String name, Function<String, T> instance) {
      return getExistingAttachment(charId.getReferenceId(), name, instance);
   }

   public static <T extends CharacterDataAttachment> T getOrCreateAttachment(@NotNull Character<?> charId, String name, Function<String, T> instance) {
      return getOrCreateAttachment(charId.getReferenceId(), name, instance);
   }

   public static void deleteAttachment(Character<?> charId, String name) {
      deleteAttachment(charId.getReferenceId(), name);
   }

   public static CompoundTag getOrCreate(Character<?> charId) {
      return getOrCreate(charId.getReferenceId());
   }

   public static void erase(Character<?> charId) {
      erase(charId.getReferenceId());
   }

   public static <T extends CharacterDataAttachment> T getExistingAttachment(String charId, String name, Function<String, T> instance) {
      return (T)Cast.cast(ATTACHMENTS.computeIfAbsent(charId, c -> new HashMap<>()).computeIfAbsent(name, id -> {
         CompoundTag data = getOrCreate(charId);
         if (data.contains(id, 10)) {
            T t = instance.apply(id);
            t.deserializeNBT(data.getCompound(id));
            return t;
         } else {
            return null;
         }
      }));
   }

   public static boolean hasAttachment(Character<?> character, String name) {
      return hasAttachment(character.getReferenceId(), name);
   }

   public static boolean hasAttachment(String charId, String name) {
      Map<String, CharacterDataAttachment> attachments = ATTACHMENTS.get(charId);
      return attachments != null && attachments.containsKey(name);
   }

   public static void deleteAttachment(String charId, String name) {
      Map<String, CharacterDataAttachment> attachments = ATTACHMENTS.get(charId);
      if (attachments != null) {
         attachments.remove(name);
      }

      getOrCreate(charId).remove(name);
   }

   public static <T extends CharacterDataAttachment> T getOrCreateAttachment(String charId, String name, Function<String, T> instance) {
      return (T)Cast.cast(ATTACHMENTS.computeIfAbsent(charId, c -> new HashMap<>()).compute(name, (id, v) -> {
         if (v != null) {
            return (CharacterDataAttachment)v;
         } else {
            CompoundTag data = getOrCreate(charId);
            T t = instance.apply(id);
            if (data.contains(id, 10)) {
               t.deserializeNBT(data.getCompound(id));
            }

            return t;
         }
      }));
   }

   public static CompoundTag getOrCreate(String charId) {
      return CACHE.computeIfAbsent(charId, CharacterDataManager::load);
   }

   public static void erase(String charId) {
      CACHE.remove(charId);
      if (CHARS_PATH != null) {
         CHARS_PATH.resolve(charId + ".dat").toFile().delete();
      }
   }

   private static CompoundTag load(String charId) {
      if (CHARS_PATH == null) {
         return new CompoundTag();
      } else {
         File df = CHARS_PATH.resolve(charId + ".dat").toFile();
         File df2 = CHARS_PATH.resolve(charId + ".dat2").toFile();
         if (!df.isFile() && !df2.isFile()) {
            return new CompoundTag();
         } else {
            try {
               try {
                  if (df2.isFile()) {
                     return NbtIo.readCompressed(df2);
                  }
               } catch (IOException var4) {
               }

               return NbtIo.readCompressed(df);
            } catch (IOException var5) {
               return new CompoundTag();
            }
         }
      }
   }

   public static void reset() {
      CACHE.clear();
      ATTACHMENTS.clear();
      CHARS_PATH = null;
   }

   public static void load(Path storytellingDir) {
      reset();
      CHARS_PATH = storytellingDir.resolve("character_extras");
   }

   public static void save(Path storytellingDir) {
      Path pth = storytellingDir.resolve("character_extras");
      pth.toFile().mkdirs();

      for (Entry<String, Map<String, CharacterDataAttachment>> e : ATTACHMENTS.entrySet()) {
         String ch = e.getKey();
         CompoundTag tag = getOrCreate(ch);
         Map<String, CharacterDataAttachment> attachments = e.getValue();

         for (Entry<String, CharacterDataAttachment> e2 : attachments.entrySet()) {
            tag.put(e2.getKey(), e2.getValue().serializeNBT());
         }
      }

      for (Entry<String, CompoundTag> e : CACHE.entrySet()) {
         CompoundTag tag = e.getValue();
         if (!tag.isEmpty()) {
            Path p = pth.resolve(e.getKey() + ".dat2");

            try {
               NbtIo.writeCompressed(tag, p.toFile());
               Path supposed = pth.resolve(e.getKey() + ".dat");
               File sf2 = p.toFile();
               File sf = supposed.toFile();
               sf.delete();
               sf2.renameTo(sf);
            } catch (IOException var9) {
               p.toFile().delete();
               throw new RuntimeException(var9);
            }
         }
      }
   }

   public static void purge() throws IOException {
      if (CHARS_PATH != null) {
         Iterator<Path> itr = Files.walk(CHARS_PATH).iterator();

         while (itr.hasNext()) {
            Path p = itr.next();
            if (Files.isRegularFile(p) && p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".dat")) {
               CompoundTag tag = NbtIo.readCompressed(p.toFile());
               if (tag.isEmpty() && Files.deleteIfExists(p)) {
                  Storytelling.LOG.info("Purged {} as its an empty tag file.", p);
               }
            }
         }
      }
   }
}

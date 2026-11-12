package com.storyteam.storytelling.events.listeners;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.system._core.character_data.CharacterDataManager;
import com.storyteam.storytelling.system.time.core.TimeController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent.Load;
import net.minecraftforge.event.level.LevelEvent.Save;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventBusSubscriber
public class ScriptIO {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(ScriptIO.class);
   public static CompoundTag WORLD_EXTRA_NBT = new CompoundTag();
   public static Path WORLD_PATH = FMLPaths.GAMEDIR.get().resolve(".storytelling");
   public static Path STRUCTURES_PATH = WORLD_PATH.resolve("structures");

   public static CompoundTag getScriptTag(int script) {
      return getSubTag("Scripts", "S" + Math.abs(script));
   }

   public static CompoundTag getSubTag(String... path) {
      CompoundTag tag = WORLD_EXTRA_NBT;

      for (String s : path) {
         CompoundTag sub = tag.getCompound(s);
         if (!tag.contains(s)) {
            tag.put(s, sub);
         }

         tag = sub;
      }

      return tag;
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void save(Save event) {
      if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
         IStoryServer story = IStoryServer.get(level.getServer());
         File dir = story.getStorytellingDir().toFile();
         if (!dir.isDirectory()) {
            dir.mkdirs();
         }

         CharacterDataManager.save(dir.toPath());
         save(story.getEntityRepository().serializeNBT(), new File(dir, "entity_repository.dat"));
         save(TimeController.get().serializeNBT(), new File(dir, "time_storage.dat"));
         File scripts = new File(dir, "scripts");
         if (scripts.isDirectory() || scripts.mkdirs()) {
            story.getScriptEnv().save(scripts);
         }
         save(story.getScriptEnv().serializeChatHistory(), new File(dir, "chat_history.dat"));

         Storytelling.BUS.post(new ScriptIOEvent.Save(dir, event));
         save(WORLD_EXTRA_NBT, new File(dir, "extra.dat"));
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void load(Load event) {
      if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
         WORLD_EXTRA_NBT = new CompoundTag();
         IStoryServer story = IStoryServer.get(level.getServer());
         File dir = (WORLD_PATH = story.getStorytellingDir()).toFile();
         CharacterDataManager.load(WORLD_PATH);
         STRUCTURES_PATH = WORLD_PATH.resolve("structures");
         if (!dir.isDirectory()) {
            dir.mkdirs();
         }

         CompoundTag nbt = read(new File(dir, "entity_repository.dat"));
         if (nbt != null) {
            story.getEntityRepository().deserializeNBT(nbt);
         }

         nbt = read(new File(dir, "extra.dat"));
         if (nbt != null) {
            WORLD_EXTRA_NBT = nbt;
         }

         nbt = read(new File(dir, "chat_history.dat"));
         story.getScriptEnv().deserializeChatHistory(nbt == null ? new CompoundTag() : nbt);

         nbt = read(new File(dir, "time_storage.dat"));
         if (nbt != null) {
            TimeController.get().deserializeNBT(nbt);
         }

         File scripts = new File(dir, "scripts");
         if (scripts.isDirectory()) {
            story.getScriptEnv().load(scripts);
         }

         Storytelling.BUS.post(new ScriptIOEvent.Load(dir, event));
      }
   }

   public static CompoundTag readOrDefault(File in, Supplier<CompoundTag> defaultValue) {
      CompoundTag tag = read(in);
      if (tag == null) {
         tag = defaultValue.get();
      }

      return tag;
   }

   public static CompoundTag read(File in) {
      try {
         return in.isFile() ? NbtIo.readCompressed(in) : null;
      } catch (IOException var2) {
         log.error("Failed to read file", var2);
         return null;
      }
   }

   public static boolean save(CompoundTag nbt, File out) {
      try {
         save(out);
         NbtIo.writeCompressed(nbt, out);
         return true;
      } catch (IOException var3) {
         log.error("Failed to save file", var3);
         return false;
      }
   }

   public static void save(File out) {
      File old = new File(out.getAbsolutePath() + ".old");
      if (old.isFile()) {
         old.delete();
      }

      if (out.isFile()) {
         out.renameTo(old);
      }
   }
}

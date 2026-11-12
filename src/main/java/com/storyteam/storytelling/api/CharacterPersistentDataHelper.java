package com.storyteam.storytelling.api;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.apache.commons.io.FileUtils;
import org.zeith.hammerlib.net.Network;

public class CharacterPersistentDataHelper {
   public static void save(String refId, Entity entity) {
      File storage = getStoragePath(refId).toFile();
      StoryData.optionalOf(entity).map(StoryData::serializeNBT).ifPresent(nbt -> {
         if (!storage.exists()) {
            try {
               FileUtils.touch(storage);
            } catch (IOException var4) {
            }
         }

         Storytelling.LOG.info("Character's data for '{}' is going to be saved.", refId);
         ScriptIO.save(nbt, storage);
      });
   }

   public static void read(String refId, Entity entity) {
      CompoundTag read = ScriptIO.read(getStoragePath(refId).toFile());
      if (read != null) {
         StoryData.optionalOf(entity).ifPresent(data -> {
            Storytelling.LOG.info("Character's data for '{}' was restored from storage.", refId);
            data.deserializeNBT(read);
            Network.sendToDimension(entity.level(), data.getProperties().createGlobalUpdate());
         });
      }
   }

   public static Path getStoragePath(String refId) {
      return ScriptIO.WORLD_PATH.resolve("characters/" + refId + ".dat");
   }
}

package com.storyteam.storytelling.system.simulator.cognition_tab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.storyteam.storytelling.Storytelling;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CognitionPoseConfig {
   public static boolean editorMod = false;
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   private static final String CONFIG_NAME = "cognition_poses.json";
   private static final File CONFIG_FILE = new File("config", "cognition_poses.json");
   private static final Map<ResourceLocation, Cognition.Pos> POS_CACHE = new HashMap<>();

   @OnlyIn(Dist.CLIENT)
   private static JsonObject loadRaw() {
      JsonObject json = new JsonObject();
      ResourceLocation loc = new ResourceLocation("storytelling", "cognition_poses.json");
      Resource resource = (Resource)Minecraft.getInstance().getResourceManager().getResource(loc).orElse(null);
      if (resource != null) {
         try (InputStreamReader reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8)) {
            JsonObject assetConfig = JsonParser.parseReader(reader).getAsJsonObject();

            for (String key : assetConfig.keySet()) {
               json.add(key, assetConfig.get(key));
            }
         } catch (Exception var12) {
            Storytelling.LOG.error(var12);
         }
      }

      if (CONFIG_FILE.exists()) {
         try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();

            for (String key : config.keySet()) {
               json.add(key, config.get(key));
            }
         } catch (Exception var10) {
            Storytelling.LOG.error(var10);
         }
      }

      return json;
   }

   @OnlyIn(Dist.CLIENT)
   private static void saveRaw(JsonObject obj) {
      try {
         if (!CONFIG_FILE.getParentFile().exists()) {
            CONFIG_FILE.getParentFile().mkdirs();
         }

         try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
            GSON.toJson(obj, writer);
         }
      } catch (Exception var6) {
         Storytelling.LOG.error(var6);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static void reloadCache() {
      POS_CACHE.clear();
      JsonObject obj = loadRaw();

      for (String key : obj.keySet()) {
         try {
            JsonArray arr = obj.getAsJsonArray(key);
            if (arr.size() == 2) {
               ResourceLocation id = new ResourceLocation(key);
               Cognition.Pos pos = new Cognition.Pos(arr.get(0).getAsFloat(), arr.get(1).getAsFloat());
               POS_CACHE.put(id, pos);
            }
         } catch (Exception var6) {
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static void save(ResourceLocation id, float x, float y) {
      JsonObject obj = loadRaw();
      JsonArray arr = new JsonArray();
      arr.add(x);
      arr.add(y);
      obj.add(id.toString(), arr);
      saveRaw(obj);
      POS_CACHE.put(id, new Cognition.Pos(x, y));
   }

   @OnlyIn(Dist.CLIENT)
   public static Cognition.Pos getPos(ResourceLocation id) {
      return POS_CACHE.getOrDefault(id, new Cognition.Pos(0.0F, 0.0F));
   }

   public static Map<ResourceLocation, Cognition.Pos> getAllPoses() {
      return Collections.unmodifiableMap(POS_CACHE);
   }
}

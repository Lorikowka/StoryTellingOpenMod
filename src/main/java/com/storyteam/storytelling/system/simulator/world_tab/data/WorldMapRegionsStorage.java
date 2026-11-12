package com.storyteam.storytelling.system.simulator.world_tab.data;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldMapRegionsStorage {
   public static final Map<String, WorldMapRegion> WORLD_MAP_REGIONS = new HashMap<>();
   public static final Map<String, WMRButton> WORLD_MAP_BUTTONS = new HashMap<>();
   private static final String WORLD_MAP_REGIONS_DIRECTORY = "world_map_regions";

   public static void addWMRegion(WorldMapRegion region) {
      WORLD_MAP_REGIONS.put(region.getId().getPath(), region);
   }

   @SubscribeEvent
   public static void onSave(ScriptIOEvent.Save event) {
      File directory = event.getSystemDirectory("world_map_regions");
      save(directory);
   }

   @SubscribeEvent
   public static void onLoad(ScriptIOEvent.Load event) {
      File directory = event.getSystemDirectory("world_map_regions");
      load(directory);
   }

   public static void save(File directory) {
      for (WorldMapRegion region : WORLD_MAP_REGIONS.values()) {
         ScriptIO.save(region.serializeNBT(), new File(directory, region.getId().getPath() + ".dat"));
      }
   }

   public static void load(File directory) {
      for (WorldMapRegion region : WORLD_MAP_REGIONS.values()) {
         CompoundTag tag = ScriptIO.read(new File(directory, region.getId().getPath() + ".dat"));
         if (tag != null) {
            region.deserializeNBT(tag);
         }
      }
   }
}

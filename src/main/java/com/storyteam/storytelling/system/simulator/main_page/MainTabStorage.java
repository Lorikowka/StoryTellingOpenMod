package com.storyteam.storytelling.system.simulator.main_page;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.EnumMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MainTabStorage {
   private static final String TAG_NAME = "main_tab_storage";
   private static final EnumMap<MainTabType, Boolean> blocked = new EnumMap<>(MainTabType.class);

   public static void setBlocked(MainTabType type, boolean blocked) {
      MainTabStorage.blocked.put(type, blocked);
   }

   public static boolean isBlocked(MainTabType type) {
      return blocked.getOrDefault(type, Boolean.valueOf(true));
   }

   @SubscribeEvent
   public static void scriptIoSave(ScriptIOEvent.Save event) {
      CompoundTag root = getCurrencyTag();

      for (MainTabType type : MainTabType.values()) {
         root.putBoolean(type.name(), isBlocked(type));
      }
   }

   @SubscribeEvent
   public static void scriptIoLoad(ScriptIOEvent.Load event) {
      CompoundTag root = getCurrencyTag();

      for (MainTabType type : MainTabType.values()) {
         boolean contains = root.contains(type.name());
         boolean val = !contains || root.getBoolean(type.name());
         blocked.put(type, val);
      }
   }

   private static CompoundTag getCurrencyTag() {
      return getOrCreateTag(ScriptIO.WORLD_EXTRA_NBT, "main_tab_storage");
   }

   private static CompoundTag getOrCreateTag(CompoundTag tag, String tagName) {
      return ScriptUtils.getTagFromTag(tag, tagName);
   }
}

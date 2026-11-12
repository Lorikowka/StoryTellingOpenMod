package com.storyteam.storytelling.system.shop.storage;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.shop.data.ShopData;
import com.storyteam.storytelling.system.shop.data.ShopInfo;
import com.storyteam.storytelling.system.shop.init.ShopsST;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShopStorage {
   private static final String SYSTEM_DIR = "shops";
   public static Map<String, ShopData> SHOPS = new LinkedHashMap<>();

   @SubscribeEvent
   public static void save(ScriptIOEvent.Save evt) {
      File dir = evt.getSystemDirectory("shops");
      if (SHOPS.isEmpty() && !ShopsST.SHOPS.isEmpty()) {
         load(dir);
      }

      for (Entry<String, ShopInfo> entry : ShopsST.SHOPS.entrySet()) {
         String id = entry.getKey();
         ShopData dummy = new ShopData(entry.getValue());
         if (SHOPS.containsKey(id)) {
            dummy = SHOPS.get(id);
         }

         dummy.setId(id);
         ScriptIO.save(dummy.serializeNBT(), new File(dir, id + ".dat"));
      }
   }

   @SubscribeEvent
   public static void load(ScriptIOEvent.Load evt) {
      load(evt.getSystemDirectory("shops"));
   }

   private static void load(File dir) {
      ShopsST.init();
      SHOPS.clear();

      for (Entry<String, ShopInfo> entry : ShopsST.SHOPS.entrySet()) {
         String id = entry.getKey();
         File file = new File(dir, id + ".dat");
         CompoundTag nbt = ScriptIO.read(file);
         ShopData data = new ShopData(entry.getValue());
         if (nbt != null) {
            data.deserializeNBT(nbt);
         }

         SHOPS.put(id, data);
      }
   }
}

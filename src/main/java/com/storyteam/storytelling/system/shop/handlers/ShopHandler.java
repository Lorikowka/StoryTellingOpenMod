package com.storyteam.storytelling.system.shop.handlers;

import com.storyteam.storytelling.events.base.NextDayEvent;
import com.storyteam.storytelling.system.shop.data.LotData;
import com.storyteam.storytelling.system.shop.data.ShopData;
import com.storyteam.storytelling.system.shop.storage.ShopStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShopHandler {
   @SubscribeEvent
   public static void onNextDay(NextDayEvent event) {
      try {
         if (ShopStorage.SHOPS == null) {
            return;
         }

         for (ShopData shopData : ShopStorage.SHOPS.values()) {
            if (shopData != null && shopData.getLots() != null) {
               for (LotData lotData : shopData.getLots().values()) {
                  lotData.setTimes_bought(0);
                  if (lotData.getInfo() != null) {
                     lotData.setStock(lotData.getInfo().getStock());
                  }
               }
            }
         }
      } catch (Exception var5) {
      }
   }
}

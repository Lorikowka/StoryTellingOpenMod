package com.storyteam.storytelling.system.rts.handlers;

import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.storyteam.storytelling.system.rts.common.RTSData;
import com.storyteam.storytelling.system.rts.common.RTSTicker;
import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import com.storyteam.storytelling.system.rts.common.ResourceST;
import com.storyteam.storytelling.system.rts.init.RTSAttributesST;
import com.storyteam.storytelling.system.rts.units.api.AnimatedUnit;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class TickersHandler {
   private static final List<RTSTicker> tickers = List.of(new RTSTicker("Food", 12000, reset -> {
      if (reset) {
         RTSData.get().foodTicker = 0;
         return 0;
      } else {
         return ++RTSData.get().foodTicker;
      }
   }, () -> {
      ResourceCostST resources = null;

      for (LivingEntity unit : UnitServerEvents.getAllUnits()) {
         if (unit instanceof AnimatedUnit) {
            AnimatedUnit u = (AnimatedUnit)unit;
            if (!u.getOwnerName().isEmpty()) {
               if (resources == null) {
                  resources = ResourceCostST.getResources(u.getOwnerName());
               }

               int needRice = (int)Math.ceil(u.getAttribute(RTSAttributesST.FOOD_CONSUMPTION).getValue());
               if (needRice > resources.get(ResourceST.RICE)) {
                  float damage = u.getMaxHealth() * ((float)(needRice - resources.get(ResourceST.RICE)) / (float)needRice);
                  u.hurt(u.damageSources().generic(), damage);
                  needRice = resources.get(ResourceST.RICE);
               }

               ResourceCostST.create().add(ResourceST.RICE, needRice).trySubtract(u.getOwnerName());
               resources = resources.add(ResourceST.RICE, -needRice);
            }
         }
      }
   }));

   @SubscribeEvent
   public static void tick(ServerTickEvent event) {
      if (event.side == LogicalSide.SERVER) {
         if (event.phase == Phase.START) {
            tickers.forEach(RTSTicker::tick);
         }
      }
   }
}

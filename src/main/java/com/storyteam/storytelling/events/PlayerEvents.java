package com.storyteam.storytelling.events;

import com.storyteam.storytelling.init.AttributesST;
import java.util.UUID;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PlayerEvents {
   private static final UUID DAMAGE_LISTENER_UUID = UUID.fromString("81b46078-542d-481d-abc6-1d9c5f3877ab");

   @SubscribeEvent
   public static void playerJoinEvent(PlayerTickEvent event) {
      if (!event.side.isClient()) {
         if (event.player.tickCount - event.player.getLastHurtByMobTimestamp() > 300 && event.player.getAttributeBaseValue(AttributesST.POISE) < 0.0) {
            if (event.player.getAttribute(AttributesST.POISE) == null) {
               return;
            }

            event.player.getAttribute(AttributesST.POISE).setBaseValue(0.0);
         }
      }
   }
}

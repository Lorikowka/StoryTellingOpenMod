package com.storyteam.storytelling.system.simulator.cognition_tab.events;

import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PlayerJoinEvent {
   @SubscribeEvent
   public static void onPlayerJoin(PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer serverPlayer) {
         CognitionHelper.syncAllData(serverPlayer);
      }
   }

   @SubscribeEvent
   public static void onPlayerDimensityChanged(PlayerChangedDimensionEvent event) {
      if (event.getEntity() instanceof ServerPlayer serverPlayer) {
         CognitionHelper.syncAllData(serverPlayer);
      }
   }

   @SubscribeEvent
   public static void onPlayerConstuct(EntityConstructing event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         CognitionHelper.syncAllData(player);
      }
   }
}

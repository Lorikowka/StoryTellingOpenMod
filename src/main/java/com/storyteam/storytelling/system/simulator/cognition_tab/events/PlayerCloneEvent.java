package com.storyteam.storytelling.system.simulator.cognition_tab.events;

import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PlayerCloneEvent {
   @SubscribeEvent
   public static void onPlayerClone(Clone event) {
      Player oldPlayer = event.getOriginal();
      Player newPlayer = event.getEntity();
      oldPlayer.reviveCaps();
      CognitionHelper.copy(newPlayer, oldPlayer);
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      if (event.getEntity() instanceof ServerPlayer serverPlayer) {
         CognitionHelper.syncAllData(serverPlayer);
      }
   }
}

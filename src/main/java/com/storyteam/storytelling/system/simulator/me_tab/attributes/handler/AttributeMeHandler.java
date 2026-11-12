package com.storyteam.storytelling.system.simulator.me_tab.attributes.handler;

import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class AttributeMeHandler {
   @SubscribeEvent
   public static void tickServer(PlayerTickEvent event) {
      if (event.phase == Phase.END && event.side != LogicalSide.CLIENT) {
         for (AttributeData data : PlayerStats.getAttributes()) {
            data.tick((ServerPlayer)event.player);
         }
      }
   }
}

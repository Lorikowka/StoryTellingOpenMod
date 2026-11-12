package com.storyteam.storytelling.system.simulator.me_tab.fragment.handler;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class FragmentHandler {
   @SubscribeEvent
   public static void tickServer(PlayerTickEvent event) {
      if (event.phase == Phase.END && event.side != LogicalSide.CLIENT) {
         if (event.player instanceof ServerPlayer serverPlayer) {
            FragmentInstanceManager.tickAll(serverPlayer);
         }
      }
   }
}

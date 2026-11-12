package com.storyteam.storytelling.system.time.handler;

import com.storyteam.storytelling.system.time.core.TimeController;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TimeWorkHandler {
   @SubscribeEvent
   public static void tickTime(ServerTickEvent event) {
      TimeController.get().tick();
   }
}

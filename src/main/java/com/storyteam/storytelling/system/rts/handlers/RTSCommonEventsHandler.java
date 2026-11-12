package com.storyteam.storytelling.system.rts.handlers;

import com.solegendary.reignofnether.event.OrthoviewToggleEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RTSCommonEventsHandler {
   @SubscribeEvent
   public static void orthoviewToggle(OrthoviewToggleEvent event) {
      event.getPlayer().refreshDimensions();
   }
}

package com.storyteam.storytelling.system.rts.handlers;

import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
import com.storyteam.storytelling.events.base.RenderJEIEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class JEIHandler {
   @SubscribeEvent
   public static void onJEIRender(RenderJEIEvent event) {
      if (OrthoviewClientEvents.isEnabled()) {
         event.setCanceled(true);
      }
   }
}

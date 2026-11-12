package com.storyteam.storytelling.system.gating.event;

import com.storyteam.storytelling.Storytelling;
import net.minecraftforge.eventbus.api.Event;

public class RequestLockedItemUpdateEvent extends Event {
   public static void request() {
      Storytelling.BUS.post(new RequestLockedItemUpdateEvent());
   }
}

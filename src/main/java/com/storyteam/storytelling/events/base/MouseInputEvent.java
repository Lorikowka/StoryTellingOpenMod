package com.storyteam.storytelling.events.base;

import lombok.Generated;
import net.minecraftforge.eventbus.api.Event;

public class MouseInputEvent extends Event {
   private final int key;

   public MouseInputEvent(int key) {
      this.key = key;
   }

   @Generated
   public int getKey() {
      return this.key;
   }
}

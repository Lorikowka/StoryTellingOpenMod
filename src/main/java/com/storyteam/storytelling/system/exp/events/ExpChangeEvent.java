package com.storyteam.storytelling.system.exp.events;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ExpChangeEvent extends Event {
   private int value;

   public ExpChangeEvent(int value) {
      this.value = value;
   }

   public int getExp() {
      return this.value;
   }

   public int setExp() {
      return this.value;
   }
}

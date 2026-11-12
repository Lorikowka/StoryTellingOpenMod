package com.storyteam.storytelling.system.rts.common;

import com.storyteam.storytelling.Storytelling;
import java.util.function.Function;

public record RTSTicker(String name, int interval, Function<Boolean, Integer> updateTicker, Runnable runnable) {
   public void tick() {
      if (this.updateTicker.apply(false) >= this.interval) {
         this.updateTicker.apply(true);
         Storytelling.LOG.info("Started RTS ticker '{}' runnable", this.name);
         this.runnable.run();
      }
   }
}

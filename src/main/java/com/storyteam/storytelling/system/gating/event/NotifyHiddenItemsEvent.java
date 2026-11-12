package com.storyteam.storytelling.system.gating.event;

import com.storyteam.storytelling.system.gating.LockedContents;
import net.minecraftforge.eventbus.api.Event;

public class NotifyHiddenItemsEvent extends Event {
   private final LockedContents previous;
   private final LockedContents current;

   public NotifyHiddenItemsEvent(LockedContents previous, LockedContents current) {
      this.previous = previous;
      this.current = current;
   }

   public LockedContents getPrevious() {
      return this.previous;
   }

   public LockedContents getCurrent() {
      return this.current;
   }
}

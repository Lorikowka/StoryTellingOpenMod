package com.storyteam.storytelling.events.base;

import javax.annotation.Nullable;
import net.minecraftforge.eventbus.api.Event;

public class WorldTimeEvent extends Event {
   private final boolean isLocked;
   @Nullable
   private Long lockTimeAt = null;
   private boolean restoreTimeOnUnlock = true;
   private boolean restoreDaylightCycleOnUnlock = true;

   public WorldTimeEvent(boolean isLocked) {
      this.isLocked = isLocked;
   }

   public boolean isLocked() {
      return this.isLocked;
   }

   @Nullable
   public Long getLockTimeAt() {
      return this.lockTimeAt;
   }

   public void lockTimeAt(long dayTime) {
      this.lockTimeAt = dayTime;
   }

   public boolean restoreTimeOnUnlock() {
      return this.restoreTimeOnUnlock;
   }

   public void setRestoreTimeOnUnlock(boolean restoreTimeOnUnlock) {
      this.restoreTimeOnUnlock = restoreTimeOnUnlock;
   }

   public boolean restoreDaylightCycleOnUnlock() {
      return this.restoreDaylightCycleOnUnlock;
   }

   public void setRestoreDaylightCycleOnUnlock(boolean restoreDaylightCycleOnUnlock) {
      this.restoreDaylightCycleOnUnlock = restoreDaylightCycleOnUnlock;
   }
}

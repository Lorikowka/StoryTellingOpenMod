package com.storyteam.storytelling.system.lockpicking.data;

import lombok.Generated;

public enum LockState {
   NOT_FOUND(false, false, true),
   LOCKED(true, true, false),
   UNLOCKED(true, false, true);

   private final boolean exists;
   private final boolean locked;
   private final boolean unlocked;

   @Generated
   public boolean isExists() {
      return this.exists;
   }

   @Generated
   public boolean isLocked() {
      return this.locked;
   }

   @Generated
   public boolean isUnlocked() {
      return this.unlocked;
   }

   @Generated
   private LockState(boolean exists, boolean locked, boolean unlocked) {
      this.exists = exists;
      this.locked = locked;
      this.unlocked = unlocked;
   }
}

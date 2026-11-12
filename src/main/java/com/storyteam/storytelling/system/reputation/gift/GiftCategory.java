package com.storyteam.storytelling.system.reputation.gift;

import lombok.Generated;

public enum GiftCategory {
   LOVE(5),
   LIKE(3),
   NEUTRAL(0),
   DISLIKE(-3),
   HATE(-5),
   IGNORED(0);

   private final int reward;

   @Generated
   public int getReward() {
      return this.reward;
   }

   @Generated
   private GiftCategory(int reward) {
      this.reward = reward;
   }
}

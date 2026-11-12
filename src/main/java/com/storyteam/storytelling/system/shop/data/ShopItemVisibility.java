package com.storyteam.storytelling.system.shop.data;

public enum ShopItemVisibility {
   HIDDEN,
   DISABLED,
   ENABLED;

   public boolean isLocked() {
      return this == DISABLED;
   }
}

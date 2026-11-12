package com.storyteam.storytelling.system.shop.data.utils;

import lombok.Generated;

public enum LotType {
   SELL("Продать"),
   BUY("Купить");

   private final String name;

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   private LotType(String name) {
      this.name = name;
   }
}

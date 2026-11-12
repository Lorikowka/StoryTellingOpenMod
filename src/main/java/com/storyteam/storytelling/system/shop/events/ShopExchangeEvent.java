package com.storyteam.storytelling.system.shop.events;

import com.storyteam.storytelling.system.shop.data.LotInfo;
import com.storyteam.storytelling.system.shop.data.ShopInfo;
import lombok.Generated;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ShopExchangeEvent extends Event {
   private final ShopInfo shop;
   private final LotInfo lot;
   private int count;
   private boolean forfeitItems;

   @Generated
   public ShopInfo getShop() {
      return this.shop;
   }

   @Generated
   public LotInfo getLot() {
      return this.lot;
   }

   @Generated
   public int getCount() {
      return this.count;
   }

   @Generated
   public boolean isForfeitItems() {
      return this.forfeitItems;
   }

   @Generated
   public void setCount(int count) {
      this.count = count;
   }

   @Generated
   public void setForfeitItems(boolean forfeitItems) {
      this.forfeitItems = forfeitItems;
   }

   @Generated
   public String toString() {
      return "ShopExchangeEvent(shop="
         + this.getShop()
         + ", lot="
         + this.getLot()
         + ", count="
         + this.getCount()
         + ", forfeitItems="
         + this.isForfeitItems()
         + ")";
   }

   @Generated
   public ShopExchangeEvent(ShopInfo shop, LotInfo lot, int count, boolean forfeitItems) {
      this.shop = shop;
      this.lot = lot;
      this.count = count;
      this.forfeitItems = forfeitItems;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ShopExchangeEvent other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else if (this.getCount() != other.getCount()) {
         return false;
      } else if (this.isForfeitItems() != other.isForfeitItems()) {
         return false;
      } else {
         Object this$shop = this.getShop();
         Object other$shop = other.getShop();
         if (this$shop == null ? other$shop == null : this$shop.equals(other$shop)) {
            Object this$lot = this.getLot();
            Object other$lot = other.getLot();
            return this$lot == null ? other$lot == null : this$lot.equals(other$lot);
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof ShopExchangeEvent;
   }

   @Generated
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      result = result * 59 + this.getCount();
      result = result * 59 + (this.isForfeitItems() ? 79 : 97);
      Object $shop = this.getShop();
      result = result * 59 + ($shop == null ? 43 : $shop.hashCode());
      Object $lot = this.getLot();
      return result * 59 + ($lot == null ? 43 : $lot.hashCode());
   }
}

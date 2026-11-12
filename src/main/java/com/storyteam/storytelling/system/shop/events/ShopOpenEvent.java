package com.storyteam.storytelling.system.shop.events;

import com.storyteam.storytelling.system.shop.data.ShopInfo;
import lombok.Generated;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ShopOpenEvent extends Event {
   private ShopInfo shop;

   @Generated
   public ShopInfo getShop() {
      return this.shop;
   }

   @Generated
   public void setShop(ShopInfo shop) {
      this.shop = shop;
   }

   @Generated
   public String toString() {
      return "ShopOpenEvent(shop=" + this.getShop() + ")";
   }

   @Generated
   public ShopOpenEvent(ShopInfo shop) {
      this.shop = shop;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ShopOpenEvent other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         Object this$shop = this.getShop();
         Object other$shop = other.getShop();
         return this$shop == null ? other$shop == null : this$shop.equals(other$shop);
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof ShopOpenEvent;
   }

   @Generated
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      Object $shop = this.getShop();
      return result * 59 + ($shop == null ? 43 : $shop.hashCode());
   }
}

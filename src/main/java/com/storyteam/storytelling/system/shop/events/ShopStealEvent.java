package com.storyteam.storytelling.system.shop.events;

import com.storyteam.storytelling.system.shop.data.LotInfo;
import com.storyteam.storytelling.system.shop.data.ShopInfo;
import lombok.Generated;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

@Cancelable
@HasResult
public class ShopStealEvent extends Event {
   private final ShopInfo shop;
   private final LotInfo lot;
   private int count;
   private float chance;

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
   public float getChance() {
      return this.chance;
   }

   @Generated
   public void setCount(int count) {
      this.count = count;
   }

   @Generated
   public void setChance(float chance) {
      this.chance = chance;
   }

   @Generated
   public String toString() {
      return "ShopStealEvent(shop=" + this.getShop() + ", lot=" + this.getLot() + ", count=" + this.getCount() + ", chance=" + this.getChance() + ")";
   }

   @Generated
   public ShopStealEvent(ShopInfo shop, LotInfo lot, int count, float chance) {
      this.shop = shop;
      this.lot = lot;
      this.count = count;
      this.chance = chance;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ShopStealEvent other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else if (this.getCount() != other.getCount()) {
         return false;
      } else if (Float.compare(this.getChance(), other.getChance()) != 0) {
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
      return other instanceof ShopStealEvent;
   }

   @Generated
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      result = result * 59 + this.getCount();
      result = result * 59 + Float.floatToIntBits(this.getChance());
      Object $shop = this.getShop();
      result = result * 59 + ($shop == null ? 43 : $shop.hashCode());
      Object $lot = this.getLot();
      return result * 59 + ($lot == null ? 43 : $lot.hashCode());
   }
}

package com.storyteam.storytelling.system.shop.data;

import com.storyteam.storytelling.system.shop.init.ShopsST;
import lombok.Generated;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class LotData implements IAutoNBTSerializable {
   @NBTSerializable
   private String shopId;
   @NBTSerializable
   private String tabId;
   @NBTSerializable
   private String lotId;
   @NBTSerializable
   private int stock;
   @NBTSerializable
   private int times_bought;

   public LotData(LotInfo info) {
      this.shopId = info.getShopId();
      this.tabId = info.getTabId();
      this.lotId = info.getId();
      this.stock = info.getStock();
   }

   public boolean isOutOfStock() {
      return this.stock == 0;
   }

   public LotInfo getInfo() {
      if (this.tabId == null) {
         return ShopsST.SHOPS.get(this.shopId).getAllLots().get(this.lotId);
      } else {
         return ShopsST.SHOPS.get(this.shopId).getTabs() != null ? ShopsST.SHOPS.get(this.shopId).getTabs().get(this.tabId).getLots().get(this.lotId) : null;
      }
   }

   @Generated
   public String getShopId() {
      return this.shopId;
   }

   @Generated
   public String getTabId() {
      return this.tabId;
   }

   @Generated
   public String getLotId() {
      return this.lotId;
   }

   @Generated
   public int getStock() {
      return this.stock;
   }

   @Generated
   public int getTimes_bought() {
      return this.times_bought;
   }

   @Generated
   public void setShopId(String shopId) {
      this.shopId = shopId;
   }

   @Generated
   public void setTabId(String tabId) {
      this.tabId = tabId;
   }

   @Generated
   public void setLotId(String lotId) {
      this.lotId = lotId;
   }

   @Generated
   public void setStock(int stock) {
      this.stock = stock;
   }

   @Generated
   public void setTimes_bought(int times_bought) {
      this.times_bought = times_bought;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LotData other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.getStock() != other.getStock()) {
         return false;
      } else if (this.getTimes_bought() != other.getTimes_bought()) {
         return false;
      } else {
         Object this$shopId = this.getShopId();
         Object other$shopId = other.getShopId();
         if (this$shopId == null ? other$shopId == null : this$shopId.equals(other$shopId)) {
            Object this$tabId = this.getTabId();
            Object other$tabId = other.getTabId();
            if (this$tabId == null ? other$tabId == null : this$tabId.equals(other$tabId)) {
               Object this$lotId = this.getLotId();
               Object other$lotId = other.getLotId();
               return this$lotId == null ? other$lotId == null : this$lotId.equals(other$lotId);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof LotData;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + this.getStock();
      result = result * 59 + this.getTimes_bought();
      Object $shopId = this.getShopId();
      result = result * 59 + ($shopId == null ? 43 : $shopId.hashCode());
      Object $tabId = this.getTabId();
      result = result * 59 + ($tabId == null ? 43 : $tabId.hashCode());
      Object $lotId = this.getLotId();
      return result * 59 + ($lotId == null ? 43 : $lotId.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "LotData(shopId="
         + this.getShopId()
         + ", tabId="
         + this.getTabId()
         + ", lotId="
         + this.getLotId()
         + ", stock="
         + this.getStock()
         + ", times_bought="
         + this.getTimes_bought()
         + ")";
   }

   @Generated
   public LotData(String shopId, String tabId, String lotId, int stock, int times_bought) {
      this.shopId = shopId;
      this.tabId = tabId;
      this.lotId = lotId;
      this.stock = stock;
      this.times_bought = times_bought;
   }
}

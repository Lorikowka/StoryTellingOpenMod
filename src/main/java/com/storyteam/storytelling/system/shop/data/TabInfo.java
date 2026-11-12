package com.storyteam.storytelling.system.shop.data;

import com.storyteam.storytelling.system.shop.data.utils.LotType;
import com.storyteam.storytelling.util.UtilST;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.world.item.ItemStack;

public class TabInfo {
   private final String id;
   private ItemStack icon;
   private Map<String, LotInfo> lots;
   private String shopId;

   public static TabInfo.TabInfoBuilder builder(String id) {
      TabInfo.TabInfoBuilder builder = new TabInfo.TabInfoBuilder();
      builder.id = id;
      return builder;
   }

   @Generated
   public String getId() {
      return this.id;
   }

   @Generated
   public ItemStack getIcon() {
      return this.icon;
   }

   @Generated
   public Map<String, LotInfo> getLots() {
      return this.lots;
   }

   @Generated
   public String getShopId() {
      return this.shopId;
   }

   @Generated
   public void setLots(Map<String, LotInfo> lots) {
      this.lots = lots;
   }

   @Generated
   public void setShopId(String shopId) {
      this.shopId = shopId;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TabInfo other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$id = this.getId();
         Object other$id = other.getId();
         if (this$id == null ? other$id == null : this$id.equals(other$id)) {
            Object this$icon = this.getIcon();
            Object other$icon = other.getIcon();
            if (this$icon == null ? other$icon == null : this$icon.equals(other$icon)) {
               Object this$lots = this.getLots();
               Object other$lots = other.getLots();
               if (this$lots == null ? other$lots == null : this$lots.equals(other$lots)) {
                  Object this$shopId = this.getShopId();
                  Object other$shopId = other.getShopId();
                  return this$shopId == null ? other$shopId == null : this$shopId.equals(other$shopId);
               } else {
                  return false;
               }
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
      return other instanceof TabInfo;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $icon = this.getIcon();
      result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
      Object $lots = this.getLots();
      result = result * 59 + ($lots == null ? 43 : $lots.hashCode());
      Object $shopId = this.getShopId();
      return result * 59 + ($shopId == null ? 43 : $shopId.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "TabInfo(id=" + this.getId() + ", icon=" + this.getIcon() + ", lots=" + this.getLots() + ", shopId=" + this.getShopId() + ")";
   }

   @Generated
   public TabInfo(String id, ItemStack icon, Map<String, LotInfo> lots, String shopId) {
      this.id = id;
      this.icon = icon;
      this.lots = lots;
      this.shopId = shopId;
   }

   @Generated
   public void setIcon(ItemStack icon) {
      this.icon = icon;
   }

   public static class TabInfoBuilder {
      @Generated
      private String id;
      @Generated
      private ItemStack icon;
      @Generated
      private String shopId;
      private ShopInfo.ShopInfoBuilder builder;
      private Map<String, LotInfo> lots = new LinkedHashMap<>();

      public TabInfoBuilder(ShopInfo.ShopInfoBuilder builder) {
         this.builder = builder;
      }

      public TabInfoBuilder() {
      }

      private TabInfo.TabInfoBuilder shopId(String shopId) {
         this.shopId = shopId;
         return this;
      }

      public TabInfo.TabInfoBuilder append(Consumer<TabInfo.TabInfoBuilder> builder) {
         builder.accept(this);
         return this;
      }

      public TabInfo.TabInfoBuilder lot(LotInfo lot) {
         lot.setShopId(this.builder.getId());
         lot.setTabId(this.id);
         this.lots.put(lot.getId(), lot);
         return this;
      }

      public TabInfo.TabInfoBuilder buyLot(String item, int count, int cost, int stock) {
         return this.lot(LotInfo.builder().type(LotType.BUY).stack(UtilST.parseCraftTweakerItem(item)).stock(stock).count(count).cost(cost).build());
      }

      public TabInfo.TabInfoBuilder buyLot(ItemStack stack, int count, int cost, int stock) {
         return this.lot(LotInfo.builder().type(LotType.BUY).stack(stack).stock(stock).count(count).cost(cost).build());
      }

      public TabInfo.TabInfoBuilder buyLot(String item, int count, int cost) {
         return this.buyLot(item, count, cost, -1);
      }

      public TabInfo.TabInfoBuilder sellLot(String item, int count, int cost, int stock) {
         return this.lot(LotInfo.builder().type(LotType.SELL).stack(UtilST.parseCraftTweakerItem(item)).stock(stock).count(count).cost(cost).build());
      }

      public TabInfo.TabInfoBuilder sellLot(String item, int count, int cost) {
         return this.sellLot(item, count, cost, -1);
      }

      public ShopInfo.ShopInfoBuilder end() {
         ShopInfo.ShopInfoBuilder builder = this.builder;
         builder.getTabs().put(this.id, this.shopId(builder.getId()).build());
         return builder;
      }

      @Generated
      public TabInfo.TabInfoBuilder id(String id) {
         this.id = id;
         return this;
      }

      @Generated
      public TabInfo.TabInfoBuilder icon(ItemStack icon) {
         this.icon = icon;
         return this;
      }

      @Generated
      public TabInfo.TabInfoBuilder lots(Map<String, LotInfo> lots) {
         this.lots = lots;
         return this;
      }

      @Generated
      public TabInfo build() {
         return new TabInfo(this.id, this.icon, this.lots, this.shopId);
      }

      @Generated
      @Override
      public String toString() {
         return "TabInfo.TabInfoBuilder(id=" + this.id + ", icon=" + this.icon + ", lots=" + this.lots + ", shopId=" + this.shopId + ")";
      }

      @Generated
      public Map<String, LotInfo> getLots() {
         return this.lots;
      }
   }
}

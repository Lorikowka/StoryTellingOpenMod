package com.storyteam.storytelling.system.shop.data;

import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.shop.data.utils.LotType;
import com.storyteam.storytelling.util.UtilST;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.world.item.ItemStack;

public class ShopInfo {
   private String id;
   private Map<String, TabInfo> tabs;
   @Nullable
   private Character<?> character;
   private Supplier<ShopItemVisibility> visibilityStatus;
   private Consumer<ShopInfo> applicator;

   public static ShopInfo.ShopInfoBuilder builder() {
      return new ShopInfo.ShopInfoBuilder() {
         @Override
         public ShopInfo build() {
            ShopInfo info = super.build();
            info.getApplicator().accept(info);
            return info;
         }
      };
   }

   public Map<String, LotInfo> getAllLots() {
      Map<String, LotInfo> result = new LinkedHashMap<>();

      for (TabInfo tab : this.tabs.values()) {
         result.putAll(tab.getLots());
      }

      return result;
   }

   @Generated
   private static Supplier<ShopItemVisibility> $default$visibilityStatus() {
      return () -> ShopItemVisibility.ENABLED;
   }

   @Generated
   private static Consumer<ShopInfo> $default$applicator() {
      return builder -> {
      };
   }

   @Generated
   public String getId() {
      return this.id;
   }

   @Generated
   public Map<String, TabInfo> getTabs() {
      return this.tabs;
   }

   @Nullable
   @Generated
   public Character<?> getCharacter() {
      return this.character;
   }

   @Generated
   public Supplier<ShopItemVisibility> getVisibilityStatus() {
      return this.visibilityStatus;
   }

   @Generated
   public Consumer<ShopInfo> getApplicator() {
      return this.applicator;
   }

   @Generated
   public void setId(String id) {
      this.id = id;
   }

   @Generated
   public void setTabs(Map<String, TabInfo> tabs) {
      this.tabs = tabs;
   }

   @Generated
   public void setCharacter(@Nullable Character<?> character) {
      this.character = character;
   }

   @Generated
   public void setVisibilityStatus(Supplier<ShopItemVisibility> visibilityStatus) {
      this.visibilityStatus = visibilityStatus;
   }

   @Generated
   public void setApplicator(Consumer<ShopInfo> applicator) {
      this.applicator = applicator;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ShopInfo other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$id = this.getId();
         Object other$id = other.getId();
         if (this$id == null ? other$id == null : this$id.equals(other$id)) {
            Object this$tabs = this.getTabs();
            Object other$tabs = other.getTabs();
            if (this$tabs == null ? other$tabs == null : this$tabs.equals(other$tabs)) {
               Object this$character = this.getCharacter();
               Object other$character = other.getCharacter();
               if (this$character == null ? other$character == null : this$character.equals(other$character)) {
                  Object this$visibilityStatus = this.getVisibilityStatus();
                  Object other$visibilityStatus = other.getVisibilityStatus();
                  if (this$visibilityStatus == null ? other$visibilityStatus == null : this$visibilityStatus.equals(other$visibilityStatus)) {
                     Object this$applicator = this.getApplicator();
                     Object other$applicator = other.getApplicator();
                     return this$applicator == null ? other$applicator == null : this$applicator.equals(other$applicator);
                  } else {
                     return false;
                  }
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
      return other instanceof ShopInfo;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $tabs = this.getTabs();
      result = result * 59 + ($tabs == null ? 43 : $tabs.hashCode());
      Object $character = this.getCharacter();
      result = result * 59 + ($character == null ? 43 : $character.hashCode());
      Object $visibilityStatus = this.getVisibilityStatus();
      result = result * 59 + ($visibilityStatus == null ? 43 : $visibilityStatus.hashCode());
      Object $applicator = this.getApplicator();
      return result * 59 + ($applicator == null ? 43 : $applicator.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "ShopInfo(id="
         + this.getId()
         + ", tabs="
         + this.getTabs()
         + ", character="
         + this.getCharacter()
         + ", visibilityStatus="
         + this.getVisibilityStatus()
         + ", applicator="
         + this.getApplicator()
         + ")";
   }

   @Generated
   public ShopInfo(
      String id, Map<String, TabInfo> tabs, @Nullable Character<?> character, Supplier<ShopItemVisibility> visibilityStatus, Consumer<ShopInfo> applicator
   ) {
      this.id = id;
      this.tabs = tabs;
      this.character = character;
      this.visibilityStatus = visibilityStatus;
      this.applicator = applicator;
   }

   public static class ShopInfoBuilder {
      @Generated
      private String id;
      @Generated
      private Character<?> character;
      @Generated
      private boolean visibilityStatus$set;
      @Generated
      private Supplier<ShopItemVisibility> visibilityStatus$value;
      @Generated
      private boolean applicator$set;
      @Generated
      private Consumer<ShopInfo> applicator$value;
      private Map<String, TabInfo> tabs = new LinkedHashMap<String, TabInfo>() {
         {
            this.put("main", TabInfo.builder("main").build());
         }
      };

      private ShopInfo.ShopInfoBuilder id(String id) {
         return this;
      }

      protected String getId() {
         return this.id;
      }

      public ShopInfo.ShopInfoBuilder append(Consumer<ShopInfo.ShopInfoBuilder> builder) {
         builder.accept(this);
         return this;
      }

      public ShopInfo.ShopInfoBuilder lot(LotInfo lot) {
         lot.setShopId(this.id);
         lot.setTabId("main");
         this.tabs.get("main").getLots().put(lot.getId(), lot);
         return this;
      }

      public ShopInfo.ShopInfoBuilder buyLot(String item, int count, int cost, int stock) {
         return this.lot(LotInfo.builder().type(LotType.BUY).stack(UtilST.parseCraftTweakerItem(item)).stock(stock).count(count).cost(cost).build());
      }

      public ShopInfo.ShopInfoBuilder buyLot(ItemStack stack, int count, int cost, int stock) {
         return this.lot(LotInfo.builder().type(LotType.BUY).stack(stack).stock(stock).count(count).cost(cost).build());
      }

      public ShopInfo.ShopInfoBuilder buyLot(String item, int count, int cost) {
         return this.buyLot(item, count, cost, -1);
      }

      public ShopInfo.ShopInfoBuilder sellLot(String item, int count, int cost, int stock) {
         return this.lot(LotInfo.builder().type(LotType.SELL).stack(UtilST.parseCraftTweakerItem(item)).stock(stock).count(count).cost(cost).build());
      }

      public ShopInfo.ShopInfoBuilder sellLot(String item, int count, int cost) {
         return this.sellLot(item, count, cost, -1);
      }

      public TabInfo.TabInfoBuilder startTab(String id, ItemStack icon) {
         return new TabInfo.TabInfoBuilder(this).id(id).icon(icon);
      }

      public ShopInfo.ShopInfoBuilder setMainIcon(ItemStack icon) {
         this.tabs.get("main").setIcon(icon);
         return this;
      }

      @Generated
      ShopInfoBuilder() {
      }

      @Generated
      public ShopInfo.ShopInfoBuilder tabs(Map<String, TabInfo> tabs) {
         this.tabs = tabs;
         return this;
      }

      @Generated
      public ShopInfo.ShopInfoBuilder character(@Nullable Character<?> character) {
         this.character = character;
         return this;
      }

      @Generated
      public ShopInfo.ShopInfoBuilder visibilityStatus(Supplier<ShopItemVisibility> visibilityStatus) {
         this.visibilityStatus$value = visibilityStatus;
         this.visibilityStatus$set = true;
         return this;
      }

      @Generated
      public ShopInfo.ShopInfoBuilder applicator(Consumer<ShopInfo> applicator) {
         this.applicator$value = applicator;
         this.applicator$set = true;
         return this;
      }

      @Generated
      public ShopInfo build() {
         Supplier<ShopItemVisibility> visibilityStatus$value = this.visibilityStatus$value;
         if (!this.visibilityStatus$set) {
            visibilityStatus$value = ShopInfo.$default$visibilityStatus();
         }

         Consumer<ShopInfo> applicator$value = this.applicator$value;
         if (!this.applicator$set) {
            applicator$value = ShopInfo.$default$applicator();
         }

         return new ShopInfo(this.id, this.tabs, this.character, visibilityStatus$value, applicator$value);
      }

      @Generated
      @Override
      public String toString() {
         return "ShopInfo.ShopInfoBuilder(id="
            + this.id
            + ", tabs="
            + this.tabs
            + ", character="
            + this.character
            + ", visibilityStatus$value="
            + this.visibilityStatus$value
            + ", applicator$value="
            + this.applicator$value
            + ")";
      }

      @Generated
      public Map<String, TabInfo> getTabs() {
         return this.tabs;
      }
   }
}

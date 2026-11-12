package com.storyteam.storytelling.system.shop.data;

import com.storyteam.storytelling.system.shop.data.utils.LotType;
import com.storyteam.storytelling.system.shop.storage.ShopStorage;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class LotInfo {
   private String shopId;
   private String tabId;
   private Supplier<ItemStack> stack;
   private int count;
   private int stock;
   private int cost;
   private ItemStack[] tradeStack;
   private LotType type;
   private Supplier<Boolean> visibilityCondition;
   private Supplier<Boolean> lockCondition;
   private Predicate<ItemStack> stackValidator;
   private Supplier<Boolean> stealCondition;
   private DoubleSupplier priceMultiplier;
   private double additiveMultiplier;

   public ItemStack getStack() {
      return this.stack.get();
   }

   public String getId() {
      ItemStack stack = this.stack.get();
      if (this.tradeStack.length <= 0) {
         return this.type.getName().toLowerCase(Locale.ROOT) + "_" + stack.save(new CompoundTag()) + "_" + this.count + "_" + this.stock + "_" + this.cost;
      } else {
         StringBuilder sb = new StringBuilder();
         sb.append(this.type.getName().toLowerCase(Locale.ROOT)).append("_");
         sb.append(stack.save(new CompoundTag())).append("_");
         sb.append(this.count).append("_");

         for (int i = 0; i < this.tradeStack.length; i++) {
            sb.append(stack.save(new CompoundTag())).append("_");
         }

         sb.append(this.count).append("_");
         sb.append(this.stock).append("_");
         sb.append(this.cost);
         return sb.toString();
      }
   }

   public LotData getData() {
      return ShopStorage.SHOPS.get(this.shopId).getLots().get(this.getId());
   }

   public int getFinalCost() {
      return this.type == LotType.BUY
         ? Mth.floor(
            (double)this.cost
               * this.priceMultiplier.getAsDouble()
               * Mth.clamp(Math.pow(this.additiveMultiplier, (double)this.getData().getTimes_bought()), 1.0, 999.0)
         )
         : this.cost;
   }

   @Generated
   private static int $default$count() {
      return 1;
   }

   @Generated
   private static int $default$stock() {
      return -1;
   }

   @Generated
   private static int $default$cost() {
      return 0;
   }

   @Generated
   private static ItemStack[] $default$tradeStack() {
      return new ItemStack[0];
   }

   @Generated
   private static LotType $default$type() {
      return LotType.BUY;
   }

   @Generated
   private static Supplier<Boolean> $default$visibilityCondition() {
      return () -> true;
   }

   @Generated
   private static Supplier<Boolean> $default$lockCondition() {
      return () -> false;
   }

   @Generated
   private static Predicate<ItemStack> $default$stackValidator() {
      return stack -> true;
   }

   @Generated
   private static Supplier<Boolean> $default$stealCondition() {
      return () -> false;
   }

   @Generated
   private static DoubleSupplier $default$priceMultiplier() {
      return () -> 1.0;
   }

   @Generated
   private static double $default$additiveMultiplier() {
      return 1.0;
   }

   @Generated
   public static LotInfo.LotInfoBuilder builder() {
      return new LotInfo.LotInfoBuilder();
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
   public int getCount() {
      return this.count;
   }

   @Generated
   public int getStock() {
      return this.stock;
   }

   @Generated
   public int getCost() {
      return this.cost;
   }

   @Generated
   public ItemStack[] getTradeStack() {
      return this.tradeStack;
   }

   @Generated
   public LotType getType() {
      return this.type;
   }

   @Generated
   public Supplier<Boolean> getVisibilityCondition() {
      return this.visibilityCondition;
   }

   @Generated
   public Supplier<Boolean> getLockCondition() {
      return this.lockCondition;
   }

   @Generated
   public Predicate<ItemStack> getStackValidator() {
      return this.stackValidator;
   }

   @Generated
   public Supplier<Boolean> getStealCondition() {
      return this.stealCondition;
   }

   @Generated
   public DoubleSupplier getPriceMultiplier() {
      return this.priceMultiplier;
   }

   @Generated
   public double getAdditiveMultiplier() {
      return this.additiveMultiplier;
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
   public void setStack(Supplier<ItemStack> stack) {
      this.stack = stack;
   }

   @Generated
   public void setCount(int count) {
      this.count = count;
   }

   @Generated
   public void setStock(int stock) {
      this.stock = stock;
   }

   @Generated
   public void setCost(int cost) {
      this.cost = cost;
   }

   @Generated
   public void setTradeStack(ItemStack[] tradeStack) {
      this.tradeStack = tradeStack;
   }

   @Generated
   public void setType(LotType type) {
      this.type = type;
   }

   @Generated
   public void setVisibilityCondition(Supplier<Boolean> visibilityCondition) {
      this.visibilityCondition = visibilityCondition;
   }

   @Generated
   public void setLockCondition(Supplier<Boolean> lockCondition) {
      this.lockCondition = lockCondition;
   }

   @Generated
   public void setStackValidator(Predicate<ItemStack> stackValidator) {
      this.stackValidator = stackValidator;
   }

   @Generated
   public void setStealCondition(Supplier<Boolean> stealCondition) {
      this.stealCondition = stealCondition;
   }

   @Generated
   public void setPriceMultiplier(DoubleSupplier priceMultiplier) {
      this.priceMultiplier = priceMultiplier;
   }

   @Generated
   public void setAdditiveMultiplier(double additiveMultiplier) {
      this.additiveMultiplier = additiveMultiplier;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LotInfo other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.getCount() != other.getCount()) {
         return false;
      } else if (this.getStock() != other.getStock()) {
         return false;
      } else if (this.getCost() != other.getCost()) {
         return false;
      } else if (Double.compare(this.getAdditiveMultiplier(), other.getAdditiveMultiplier()) != 0) {
         return false;
      } else {
         Object this$shopId = this.getShopId();
         Object other$shopId = other.getShopId();
         if (this$shopId == null ? other$shopId == null : this$shopId.equals(other$shopId)) {
            Object this$tabId = this.getTabId();
            Object other$tabId = other.getTabId();
            if (this$tabId == null ? other$tabId == null : this$tabId.equals(other$tabId)) {
               Object this$stack = this.getStack();
               Object other$stack = other.getStack();
               if (this$stack == null ? other$stack == null : this$stack.equals(other$stack)) {
                  if (!Arrays.deepEquals(this.getTradeStack(), other.getTradeStack())) {
                     return false;
                  } else {
                     Object this$type = this.getType();
                     Object other$type = other.getType();
                     if (this$type == null ? other$type == null : this$type.equals(other$type)) {
                        Object this$visibilityCondition = this.getVisibilityCondition();
                        Object other$visibilityCondition = other.getVisibilityCondition();
                        if (this$visibilityCondition == null ? other$visibilityCondition == null : this$visibilityCondition.equals(other$visibilityCondition)) {
                           Object this$lockCondition = this.getLockCondition();
                           Object other$lockCondition = other.getLockCondition();
                           if (this$lockCondition == null ? other$lockCondition == null : this$lockCondition.equals(other$lockCondition)) {
                              Object this$stackValidator = this.getStackValidator();
                              Object other$stackValidator = other.getStackValidator();
                              if (this$stackValidator == null ? other$stackValidator == null : this$stackValidator.equals(other$stackValidator)) {
                                 Object this$stealCondition = this.getStealCondition();
                                 Object other$stealCondition = other.getStealCondition();
                                 if (this$stealCondition == null ? other$stealCondition == null : this$stealCondition.equals(other$stealCondition)) {
                                    Object this$priceMultiplier = this.getPriceMultiplier();
                                    Object other$priceMultiplier = other.getPriceMultiplier();
                                    return this$priceMultiplier == null ? other$priceMultiplier == null : this$priceMultiplier.equals(other$priceMultiplier);
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
                     } else {
                        return false;
                     }
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
      return other instanceof LotInfo;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + this.getCount();
      result = result * 59 + this.getStock();
      result = result * 59 + this.getCost();
      long $additiveMultiplier = Double.doubleToLongBits(this.getAdditiveMultiplier());
      result = result * 59 + (int)($additiveMultiplier >>> 32 ^ $additiveMultiplier);
      Object $shopId = this.getShopId();
      result = result * 59 + ($shopId == null ? 43 : $shopId.hashCode());
      Object $tabId = this.getTabId();
      result = result * 59 + ($tabId == null ? 43 : $tabId.hashCode());
      Object $stack = this.getStack();
      result = result * 59 + ($stack == null ? 43 : $stack.hashCode());
      result = result * 59 + Arrays.deepHashCode(this.getTradeStack());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $visibilityCondition = this.getVisibilityCondition();
      result = result * 59 + ($visibilityCondition == null ? 43 : $visibilityCondition.hashCode());
      Object $lockCondition = this.getLockCondition();
      result = result * 59 + ($lockCondition == null ? 43 : $lockCondition.hashCode());
      Object $stackValidator = this.getStackValidator();
      result = result * 59 + ($stackValidator == null ? 43 : $stackValidator.hashCode());
      Object $stealCondition = this.getStealCondition();
      result = result * 59 + ($stealCondition == null ? 43 : $stealCondition.hashCode());
      Object $priceMultiplier = this.getPriceMultiplier();
      return result * 59 + ($priceMultiplier == null ? 43 : $priceMultiplier.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "LotInfo(shopId="
         + this.getShopId()
         + ", tabId="
         + this.getTabId()
         + ", stack="
         + this.getStack()
         + ", count="
         + this.getCount()
         + ", stock="
         + this.getStock()
         + ", cost="
         + this.getCost()
         + ", tradeStack="
         + Arrays.deepToString(this.getTradeStack())
         + ", type="
         + this.getType()
         + ", visibilityCondition="
         + this.getVisibilityCondition()
         + ", lockCondition="
         + this.getLockCondition()
         + ", stackValidator="
         + this.getStackValidator()
         + ", stealCondition="
         + this.getStealCondition()
         + ", priceMultiplier="
         + this.getPriceMultiplier()
         + ", additiveMultiplier="
         + this.getAdditiveMultiplier()
         + ")";
   }

   @Generated
   public LotInfo(
      String shopId,
      String tabId,
      Supplier<ItemStack> stack,
      int count,
      int stock,
      int cost,
      ItemStack[] tradeStack,
      LotType type,
      Supplier<Boolean> visibilityCondition,
      Supplier<Boolean> lockCondition,
      Predicate<ItemStack> stackValidator,
      Supplier<Boolean> stealCondition,
      DoubleSupplier priceMultiplier,
      double additiveMultiplier
   ) {
      this.shopId = shopId;
      this.tabId = tabId;
      this.stack = stack;
      this.count = count;
      this.stock = stock;
      this.cost = cost;
      this.tradeStack = tradeStack;
      this.type = type;
      this.visibilityCondition = visibilityCondition;
      this.lockCondition = lockCondition;
      this.stackValidator = stackValidator;
      this.stealCondition = stealCondition;
      this.priceMultiplier = priceMultiplier;
      this.additiveMultiplier = additiveMultiplier;
   }

   public static class LotInfoBuilder {
      @Generated
      private String shopId;
      @Generated
      private String tabId;
      @Generated
      private Supplier<ItemStack> stack;
      @Generated
      private boolean count$set;
      @Generated
      private int count$value;
      @Generated
      private boolean stock$set;
      @Generated
      private int stock$value;
      @Generated
      private boolean cost$set;
      @Generated
      private int cost$value;
      @Generated
      private boolean tradeStack$set;
      @Generated
      private ItemStack[] tradeStack$value;
      @Generated
      private boolean type$set;
      @Generated
      private LotType type$value;
      @Generated
      private boolean visibilityCondition$set;
      @Generated
      private Supplier<Boolean> visibilityCondition$value;
      @Generated
      private boolean lockCondition$set;
      @Generated
      private Supplier<Boolean> lockCondition$value;
      @Generated
      private boolean stackValidator$set;
      @Generated
      private Predicate<ItemStack> stackValidator$value;
      @Generated
      private boolean stealCondition$set;
      @Generated
      private Supplier<Boolean> stealCondition$value;
      @Generated
      private boolean priceMultiplier$set;
      @Generated
      private DoubleSupplier priceMultiplier$value;
      @Generated
      private boolean additiveMultiplier$set;
      @Generated
      private double additiveMultiplier$value;

      public LotInfo.LotInfoBuilder stack(ItemStack stack) {
         this.stack = stack::copy;
         return this;
      }

      public LotInfo.LotInfoBuilder stack(Supplier<ItemStack> stackSupplier) {
         this.stack = stackSupplier;
         return this;
      }

      private LotInfo.LotInfoBuilder shopId(String shopId) {
         return this;
      }

      @Generated
      LotInfoBuilder() {
      }

      @Generated
      public LotInfo.LotInfoBuilder tabId(String tabId) {
         this.tabId = tabId;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder count(int count) {
         this.count$value = count;
         this.count$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder stock(int stock) {
         this.stock$value = stock;
         this.stock$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder cost(int cost) {
         this.cost$value = cost;
         this.cost$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder tradeStack(ItemStack[] tradeStack) {
         this.tradeStack$value = tradeStack;
         this.tradeStack$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder type(LotType type) {
         this.type$value = type;
         this.type$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder visibilityCondition(Supplier<Boolean> visibilityCondition) {
         this.visibilityCondition$value = visibilityCondition;
         this.visibilityCondition$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder lockCondition(Supplier<Boolean> lockCondition) {
         this.lockCondition$value = lockCondition;
         this.lockCondition$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder stackValidator(Predicate<ItemStack> stackValidator) {
         this.stackValidator$value = stackValidator;
         this.stackValidator$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder stealCondition(Supplier<Boolean> stealCondition) {
         this.stealCondition$value = stealCondition;
         this.stealCondition$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder priceMultiplier(DoubleSupplier priceMultiplier) {
         this.priceMultiplier$value = priceMultiplier;
         this.priceMultiplier$set = true;
         return this;
      }

      @Generated
      public LotInfo.LotInfoBuilder additiveMultiplier(double additiveMultiplier) {
         this.additiveMultiplier$value = additiveMultiplier;
         this.additiveMultiplier$set = true;
         return this;
      }

      @Generated
      public LotInfo build() {
         int count$value = this.count$value;
         if (!this.count$set) {
            count$value = LotInfo.$default$count();
         }

         int stock$value = this.stock$value;
         if (!this.stock$set) {
            stock$value = LotInfo.$default$stock();
         }

         int cost$value = this.cost$value;
         if (!this.cost$set) {
            cost$value = LotInfo.$default$cost();
         }

         ItemStack[] tradeStack$value = this.tradeStack$value;
         if (!this.tradeStack$set) {
            tradeStack$value = LotInfo.$default$tradeStack();
         }

         LotType type$value = this.type$value;
         if (!this.type$set) {
            type$value = LotInfo.$default$type();
         }

         Supplier<Boolean> visibilityCondition$value = this.visibilityCondition$value;
         if (!this.visibilityCondition$set) {
            visibilityCondition$value = LotInfo.$default$visibilityCondition();
         }

         Supplier<Boolean> lockCondition$value = this.lockCondition$value;
         if (!this.lockCondition$set) {
            lockCondition$value = LotInfo.$default$lockCondition();
         }

         Predicate<ItemStack> stackValidator$value = this.stackValidator$value;
         if (!this.stackValidator$set) {
            stackValidator$value = LotInfo.$default$stackValidator();
         }

         Supplier<Boolean> stealCondition$value = this.stealCondition$value;
         if (!this.stealCondition$set) {
            stealCondition$value = LotInfo.$default$stealCondition();
         }

         DoubleSupplier priceMultiplier$value = this.priceMultiplier$value;
         if (!this.priceMultiplier$set) {
            priceMultiplier$value = LotInfo.$default$priceMultiplier();
         }

         double additiveMultiplier$value = this.additiveMultiplier$value;
         if (!this.additiveMultiplier$set) {
            additiveMultiplier$value = LotInfo.$default$additiveMultiplier();
         }

         return new LotInfo(
            this.shopId,
            this.tabId,
            this.stack,
            count$value,
            stock$value,
            cost$value,
            tradeStack$value,
            type$value,
            visibilityCondition$value,
            lockCondition$value,
            stackValidator$value,
            stealCondition$value,
            priceMultiplier$value,
            additiveMultiplier$value
         );
      }

      @Generated
      @Override
      public String toString() {
         return "LotInfo.LotInfoBuilder(shopId="
            + this.shopId
            + ", tabId="
            + this.tabId
            + ", stack="
            + this.stack
            + ", count$value="
            + this.count$value
            + ", stock$value="
            + this.stock$value
            + ", cost$value="
            + this.cost$value
            + ", tradeStack$value="
            + Arrays.deepToString(this.tradeStack$value)
            + ", type$value="
            + this.type$value
            + ", visibilityCondition$value="
            + this.visibilityCondition$value
            + ", lockCondition$value="
            + this.lockCondition$value
            + ", stackValidator$value="
            + this.stackValidator$value
            + ", stealCondition$value="
            + this.stealCondition$value
            + ", priceMultiplier$value="
            + this.priceMultiplier$value
            + ", additiveMultiplier$value="
            + this.additiveMultiplier$value
            + ")";
      }
   }
}

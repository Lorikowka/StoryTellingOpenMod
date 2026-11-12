package com.storyteam.storytelling.system.containertier.data;

import com.storyteam.storytelling.util.UtilST;
import java.util.Random;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.Resources;

public class ContainerTierElement {
   private Supplier<ItemStack> itemStack;
   private int maxCount;
   private int minCount;
   private int maxDrop;
   private double chance;

   public ContainerTierElement() {
      this(Cast.constant(ItemStack.EMPTY), 1, 1, 1, 1.0);
   }

   public ContainerTierElement(ItemStack item) {
      this(Cast.constant(item), 1, item.getMaxStackSize(), 1, 1.0);
   }

   public ContainerTierElement(Supplier<ItemStack> itemStack, int minCount, int maxCount, int maxDrop, double chance) {
      this.itemStack = itemStack;
      this.maxCount = maxCount;
      this.minCount = minCount;
      this.maxDrop = Math.max(maxDrop, 1);
      this.chance = chance;
      if (chance <= 0.0 || chance > 1.0) {
         throw new IllegalArgumentException("ContainerTierElement(chance) out of range. Expected (0; 1], got " + chance);
      }
   }

   public int[] rollCount(Random rng) {
      int dropCount = 0;

      int rolls;
      for (rolls = 0; rng.nextDouble() <= this.chance; rolls++) {
         dropCount += rng.nextInt(this.getMinCount(), this.getMaxCount() + 1);
      }

      return new int[]{rolls, dropCount};
   }

   public ItemStack getItemStack() {
      return this.itemStack.get();
   }

   @Generated
   public static ContainerTierElement.ContainerTierElementBuilder builder() {
      return new ContainerTierElement.ContainerTierElementBuilder();
   }

   @Generated
   public int getMaxCount() {
      return this.maxCount;
   }

   @Generated
   public int getMinCount() {
      return this.minCount;
   }

   @Generated
   public int getMaxDrop() {
      return this.maxDrop;
   }

   @Generated
   public double getChance() {
      return this.chance;
   }

   @Generated
   public void setItemStack(Supplier<ItemStack> itemStack) {
      this.itemStack = itemStack;
   }

   @Generated
   public void setMaxCount(int maxCount) {
      this.maxCount = maxCount;
   }

   @Generated
   public void setMinCount(int minCount) {
      this.minCount = minCount;
   }

   @Generated
   public void setMaxDrop(int maxDrop) {
      this.maxDrop = maxDrop;
   }

   @Generated
   public void setChance(double chance) {
      this.chance = chance;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ContainerTierElement other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.getMaxCount() != other.getMaxCount()) {
         return false;
      } else if (this.getMinCount() != other.getMinCount()) {
         return false;
      } else if (this.getMaxDrop() != other.getMaxDrop()) {
         return false;
      } else if (Double.compare(this.getChance(), other.getChance()) != 0) {
         return false;
      } else {
         Object this$itemStack = this.getItemStack();
         Object other$itemStack = other.getItemStack();
         return this$itemStack == null ? other$itemStack == null : this$itemStack.equals(other$itemStack);
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof ContainerTierElement;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + this.getMaxCount();
      result = result * 59 + this.getMinCount();
      result = result * 59 + this.getMaxDrop();
      long $chance = Double.doubleToLongBits(this.getChance());
      result = result * 59 + (int)($chance >>> 32 ^ $chance);
      Object $itemStack = this.getItemStack();
      return result * 59 + ($itemStack == null ? 43 : $itemStack.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "ContainerTierElement(itemStack="
         + this.getItemStack()
         + ", maxCount="
         + this.getMaxCount()
         + ", minCount="
         + this.getMinCount()
         + ", maxDrop="
         + this.getMaxDrop()
         + ", chance="
         + this.getChance()
         + ")";
   }

   public static class ContainerTierElementBuilder {
      @Generated
      private Supplier<ItemStack> itemStack;
      @Generated
      private int minCount;
      @Generated
      private int maxCount;
      @Generated
      private int maxDrop;
      @Generated
      private double chance;

      public ContainerTierElement.ContainerTierElementBuilder item(String id) {
         ResourceLocation rl = Resources.location(id);
         return this.itemStack(() -> ((Item)BuiltInRegistries.ITEM.get(rl)).getDefaultInstance());
      }

      public ContainerTierElement.ContainerTierElementBuilder ctItem(String craftTweakerItem) {
         return this.itemStack(() -> UtilST.parseCraftTweakerItem(craftTweakerItem));
      }

      public ContainerTierElement.ContainerTierElementBuilder mcItem(ItemStack stack) {
         return this.itemStack(stack::copy);
      }

      @Generated
      ContainerTierElementBuilder() {
      }

      @Generated
      public ContainerTierElement.ContainerTierElementBuilder itemStack(Supplier<ItemStack> itemStack) {
         this.itemStack = itemStack;
         return this;
      }

      @Generated
      public ContainerTierElement.ContainerTierElementBuilder minCount(int minCount) {
         this.minCount = minCount;
         return this;
      }

      @Generated
      public ContainerTierElement.ContainerTierElementBuilder maxCount(int maxCount) {
         this.maxCount = maxCount;
         return this;
      }

      @Generated
      public ContainerTierElement.ContainerTierElementBuilder maxDrop(int maxDrop) {
         this.maxDrop = maxDrop;
         return this;
      }

      @Generated
      public ContainerTierElement.ContainerTierElementBuilder chance(double chance) {
         this.chance = chance;
         return this;
      }

      @Generated
      public ContainerTierElement build() {
         return new ContainerTierElement(this.itemStack, this.minCount, this.maxCount, this.maxDrop, this.chance);
      }

      @Generated
      @Override
      public String toString() {
         return "ContainerTierElement.ContainerTierElementBuilder(itemStack="
            + this.itemStack
            + ", minCount="
            + this.minCount
            + ", maxCount="
            + this.maxCount
            + ", maxDrop="
            + this.maxDrop
            + ", chance="
            + this.chance
            + ")";
      }
   }
}

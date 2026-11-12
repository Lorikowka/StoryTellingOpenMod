package com.storyteam.storytelling.system.containertier;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

public class ContainerTierPopulator {
   public static NonNullList<ContainerTierPopulator.BoundSlot> distributeItemOnce(
      NonNullList<ContainerTierPopulator.BoundSlot> allSlots, ItemStack sample, AtomicInteger totalCount, IntProvider clumpSize
   ) {
      NonNullList<ContainerTierPopulator.BoundSlot> populatedSlots = NonNullList.create();
      NonNullList<ContainerTierPopulator.BoundSlot> filteredSlots = NonNullList.create();
      filteredSlots.addAll(allSlots);
      filteredSlots.removeIf(bs -> !bs.canPlaceItem(sample));
      RandomSource rng = RandomSource.create();

      while (!filteredSlots.isEmpty() && totalCount.get() > 0) {
         int clump = Math.min(clumpSize.sample(rng), totalCount.get());
         ContainerTierPopulator.BoundSlot remove = (ContainerTierPopulator.BoundSlot)filteredSlots.remove(rng.nextInt(filteredSlots.size()));
         int inserted = remove.insert(sample, clump);
         if (inserted > 0) {
            populatedSlots.add(remove);
            totalCount.addAndGet(-inserted);
         }
      }

      return populatedSlots;
   }

   public static NonNullList<ContainerTierPopulator.BoundSlot> gatherSlots(Player player, Level level, int radius) {
      NonNullList<ContainerTierPopulator.BoundSlot> slots = NonNullList.create();

      for (BlockPos pos : BlockPos.betweenClosed(
         player.blockPosition().offset(-radius, -radius, -radius), player.blockPosition().offset(radius, radius, radius)
      )) {
         if (level.getBlockEntity(pos) instanceof BaseContainerBlockEntity bcbe) {
            gatherSlots(bcbe, slots);
         }
      }

      return slots;
   }

   public static void gatherSlots(BaseContainerBlockEntity container, NonNullList<ContainerTierPopulator.BoundSlot> slots) {
      IntStream.range(0, container.getContainerSize()).mapToObj(i -> new ContainerTierPopulator.BoundSlot(container, i)).forEach(slots::add);
   }

   public static record BoundSlot(BaseContainerBlockEntity container, int slot) {
      public int insert(ItemStack sample, int count) {
         int maxStack = Math.min(this.container.getMaxStackSize(), sample.getMaxStackSize());
         count = Math.min(maxStack, count);
         sample = sample.copyWithCount(count);
         if (!this.canPlaceItem(sample)) {
            return 0;
         } else {
            ItemStack it = this.getItem();
            if (it.isEmpty()) {
               this.setItem(sample);
               return count;
            } else {
               if (ItemStack.isSameItemSameTags(it, sample)) {
                  count = Math.min(maxStack - it.getCount(), count);
                  it.grow(count);
               }

               return count;
            }
         }
      }

      public boolean canPlaceItem(ItemStack stack) {
         return this.container.canPlaceItem(this.slot, stack);
      }

      public ItemStack getItem() {
         return this.container.getItem(this.slot);
      }

      public void setItem(ItemStack item) {
         this.container.setItem(this.slot, item);
      }
   }
}

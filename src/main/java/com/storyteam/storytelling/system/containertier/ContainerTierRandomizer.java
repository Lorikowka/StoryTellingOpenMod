package com.storyteam.storytelling.system.containertier;

import com.storyteam.storytelling.content.tiles.TileLinkableContainer;
import com.storyteam.storytelling.system.containertier.data.ContainerTier;
import com.storyteam.storytelling.system.containertier.data.ContainerTierElement;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.api.inv.SimpleInventory;

public class ContainerTierRandomizer {
   private static final Random RANDOM = new Random();

   public static void fillChest(BaseContainerBlockEntity container, ContainerTier tier, int rolls) {
      List<ContainerTierElement> elements = tier.getTierElements();
      if (!elements.isEmpty()) {
         NonNullList<ItemStack> resultItems = NonNullList.create();
         container.clearContent();
         ArrayList<ContainerTierElement> els = new ArrayList<>(elements);
         int totalRolls = 0;

         label27:
         while (totalRolls < rolls) {
            Collections.shuffle(els);

            for (ContainerTierElement el : els) {
               int[] ints = el.rollCount(RANDOM);
               if (ints[0] > 0) {
                  totalRolls += ints[0];
                  ItemStack resultItem = el.getItemStack();
                  resultItem.setCount(ints[1]);
                  resultItems.addAll(splitStack(resultItem, 255));
                  if (resultItems.size() > container.getContainerSize()) {
                     break label27;
                  }
               }
            }
         }

         populateContainer(container, resultItems);
      }
   }

   public static void fillChest(TileLinkableContainer tile, ContainerTier tier, int rolls) {
      if (tile.getInventory() instanceof SimpleInventory container) {
         List<ContainerTierElement> elements = tier.getTierElements();
         if (!elements.isEmpty()) {
            NonNullList<ItemStack> resultItems = NonNullList.create();
            ArrayList<ContainerTierElement> els = new ArrayList<>(elements);
            int totalRolls = 0;

            label32:
            while (totalRolls < rolls) {
               Collections.shuffle(els);

               for (ContainerTierElement el : els) {
                  int[] ints = el.rollCount(RANDOM);
                  if (ints[0] > 0) {
                     totalRolls += ints[0];
                     ItemStack resultItem = el.getItemStack();
                     resultItem.setCount(ints[1]);
                     resultItems.addAll(splitStack(resultItem, 255));
                     if (resultItems.size() > container.getContainerSize()) {
                        break label32;
                     }
                  }
               }
            }

            populateContainer(container, resultItems);
         }
      }
   }

   public static void populateContainer(Container container, NonNullList<ItemStack> items) {
      IntList availableSlots = new IntArrayList(IntStream.range(0, container.getContainerSize()).toArray());
      Collections.shuffle(availableSlots);

      while (!availableSlots.isEmpty()) {
         int slot = availableSlots.removeInt(0);

         for (int i = 0; i < items.size(); i++) {
            ItemStack stack = (ItemStack)items.get(i);
            if (container.canPlaceItem(slot, stack)) {
               container.setItem(slot, stack.copy());
               items.remove(0);
               break;
            }
         }
      }
   }

   private static NonNullList<ItemStack> splitStack(ItemStack stack, int maxStackSize) {
      NonNullList<ItemStack> stacks = NonNullList.create();
      if (stack.isEmpty()) {
         return stacks;
      } else {
         while (!stack.isEmpty()) {
            int split = Math.min(stack.getCount(), Math.min(stack.getMaxStackSize(), maxStackSize));
            if (split > 0) {
               stacks.add(stack.split(split));
            }
         }

         return stacks;
      }
   }

   private static int rnd(int min, int max) {
      return RANDOM.nextInt(min, max + 1);
   }

   public static void foreachInRadius(Player player, Level level, ContainerTier tier, int radius, int rolls) {
      for (BlockPos pos : BlockPos.betweenClosed(
         player.blockPosition().offset(-radius, -radius, -radius), player.blockPosition().offset(radius, radius, radius)
      )) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof BaseContainerBlockEntity bcbe) {
            fillChest(bcbe, tier, rolls);
         }

         if (be instanceof TileLinkableContainer bcbe) {
            fillChest(bcbe, tier, rolls);
         }
      }
   }
}

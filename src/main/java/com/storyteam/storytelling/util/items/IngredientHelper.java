package com.storyteam.storytelling.util.items;

import com.google.common.base.Suppliers;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class IngredientHelper {
   private static final Map<Class<? extends Block>, Supplier<Ingredient>> INGREDIENTS_BY_BLOCK = new ConcurrentHashMap<>();
   private static final Map<Class<? extends Item>, Supplier<Ingredient>> INGREDIENTS_BY_ITEM = new ConcurrentHashMap<>();
   public static final Supplier<Ingredient> ALL_FOODS = Suppliers.memoize(() -> {
      Builder<ItemStack> its = Stream.builder();

      for (Item item : ForgeRegistries.ITEMS) {
         if (item.isEdible()) {
            its.add(item.getDefaultInstance());
         }
      }

      return Ingredient.of(its.build());
   });

   public static Supplier<Ingredient> itemBlocksOfType(Class<? extends Block> block) {
      return INGREDIENTS_BY_BLOCK.computeIfAbsent(block, key -> () -> {
            Builder<ItemStack> its = Stream.builder();

            for (Item item : ForgeRegistries.ITEMS) {
               if (item instanceof BlockItem) {
                  BlockItem bi = (BlockItem)item;
                  if (key.isInstance(bi.getBlock())) {
                     its.add(item.getDefaultInstance());
                  }
               }
            }

            return Ingredient.of(its.build());
         });
   }

   public static Supplier<Ingredient> itemsOfType(Class<? extends Item> block) {
      return INGREDIENTS_BY_ITEM.computeIfAbsent(block, key -> () -> {
            Builder<ItemStack> its = Stream.builder();

            for (Item item : ForgeRegistries.ITEMS) {
               if (key.isInstance(item)) {
                  its.add(item.getDefaultInstance());
               }
            }

            return Ingredient.of(its.build());
         });
   }
}

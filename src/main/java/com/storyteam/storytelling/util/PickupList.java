package com.storyteam.storytelling.util;

import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.common.util.INBTSerializable;
import org.zeith.hammerlib.core.RecipeHelper;

public class PickupList implements INBTSerializable<ListTag> {
   final Map<PickupList.TaggedItem, Style> overrideStyles = new HashMap<>();
   final Object2IntArrayMap<PickupList.TaggedItem> initial = new Object2IntArrayMap();
   final Object2IntArrayMap<PickupList.TaggedItem> waiting = new Object2IntArrayMap();

   public PickupList add(String item, int amount) {
      return this.add(new PickupList.TaggedItem((Supplier<ItemStack>)(() -> UtilST.createStack(item, 1))), amount);
   }

   public PickupList add(Ingredient item, int amount) {
      return this.add(new PickupList.TaggedItem(item), amount);
   }

   public PickupList add(ItemLike item, int amount) {
      return this.add(new PickupList.TaggedItem(item.asItem()), amount);
   }

   public PickupList add(TagKey<Item> item, int amount) {
      return this.add(Ingredient.of(item), amount);
   }

   public PickupList add(PickupList.TaggedItem item, int amount) {
      this.waiting.put(item, this.waiting.getInt(item) + amount);
      this.initial.put(item, this.initial.getInt(item) + amount);
      return this;
   }

   public PickupList add(ItemLike item, int amount, Style style) {
      return this.add(new PickupList.TaggedItem(item.asItem()), amount, style);
   }

   public PickupList add(ItemStack item, int amount) {
      return this.add(StrictNBTIngredient.of(item), amount);
   }

   public PickupList add(PickupList.TaggedItem item, int amount, Style style) {
      this.waiting.put(item, this.waiting.getInt(item) + amount);
      this.initial.put(item, this.initial.getInt(item) + amount);
      this.overrideStyles.put(item, style);
      return this;
   }

   public boolean hasAll(Container inv) {
      ObjectIterator var2 = this.waiting.object2IntEntrySet().iterator();

      while (var2.hasNext()) {
         Entry<PickupList.TaggedItem> entry = (Entry<PickupList.TaggedItem>)var2.next();
         if (count(inv, (Predicate<ItemStack>)entry.getKey()) < entry.getIntValue()) {
            return false;
         }
      }

      return true;
   }

   public boolean takeAll(Container inv) {
      if (!this.hasAll(inv)) {
         return false;
      } else {
         ObjectIterator var2 = this.waiting.object2IntEntrySet().iterator();

         while (var2.hasNext()) {
            Entry<PickupList.TaggedItem> entry = (Entry<PickupList.TaggedItem>)var2.next();
            PickupList.TaggedItem it = (PickupList.TaggedItem)entry.getKey();
            int amount = entry.getIntValue();

            for (int i = 0; i < inv.getContainerSize(); i++) {
               if (amount <= 0) {
                  return true;
               }

               ItemStack stack = inv.getItem(i);
               if (!stack.isEmpty() && it.test(stack)) {
                  if (stack.getCount() >= amount) {
                     stack.shrink(amount);
                     amount = 0;
                  } else {
                     amount -= stack.getCount();
                     inv.setItem(i, ItemStack.EMPTY);
                  }
               }
            }
         }

         return false;
      }
   }

   public static int count(Container inv, Item it) {
      int c = 0;

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (!stack.isEmpty() && stack.getItem() == it) {
            c += stack.getCount();
         }
      }

      return c;
   }

   public static int count(Container inv, Predicate<ItemStack> it) {
      int c = 0;

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (!stack.isEmpty() && it.test(stack)) {
            c += stack.getCount();
         }
      }

      return c;
   }

   public boolean pickupAs(LivingEntity ent, float expand) {
      boolean pickedAnything = false;
      if (!this.waiting.isEmpty()) {
         ObjectIterator var4 = this.waiting.keySet().iterator();

         while (var4.hasNext()) {
            PickupList.TaggedItem item = (PickupList.TaggedItem)var4.next();
            int left = this.waiting.getInt(item);
            int picked = ScriptUtils.pickUpItemMax(ent, left, expand, item);
            if (picked > 0) {
               pickedAnything = true;
            }

            if (picked > 0) {
               this.waiting.put(item, left - picked);
            }
         }
      }

      if (pickedAnything || ent.tickCount % 20 == 0) {
         this.waiting.values().removeIf(i -> i <= 0);
      }

      return pickedAnything;
   }

   public ItemStack pickupOne(LivingEntity ent, float expand) {
      PickupList.TaggedItem pickup = null;
      if (!this.waiting.isEmpty()) {
         ObjectIterator var4 = this.waiting.keySet().iterator();

         while (var4.hasNext()) {
            PickupList.TaggedItem item = (PickupList.TaggedItem)var4.next();
            int left = this.waiting.getInt(item);
            int picked = ScriptUtils.pickUpItemMax(ent, left, expand, item);
            if (picked > 0) {
               pickup = item;
               this.waiting.put(item, left - picked);
               break;
            }
         }
      }

      if (pickup != null || ent.tickCount % 20 == 0) {
         this.waiting.values().removeIf(i -> i <= 0);
      }

      return pickup != null ? pickup.asStack() : ItemStack.EMPTY;
   }

   public ItemStack createList(String name) {
      ItemStack paper = new ItemStack(Items.PAPER);
      CompoundTag display = paper.getOrCreateTagElement("display");
      ListTag lore = new ListTag();
      ObjectIterator var5 = this.initial.keySet().iterator();

      while (var5.hasNext()) {
         PickupList.TaggedItem s = (PickupList.TaggedItem)var5.next();
         Component itemName = Component.literal(this.initial.getInt(s) + "x ").append(s.asStack().getDisplayName());
         if (this.overrideStyles.containsKey(s)) {
            itemName = itemName.copy().setStyle(this.overrideStyles.get(s));
         }

         lore.add(StringTag.valueOf(Serializer.toJson(itemName)));
      }

      display.put("Lore", lore);
      paper.setHoverName(Component.literal(name));
      return paper;
   }

   public List<Component> getLeftoverItems() {
      List<Component> txts = new ArrayList<>();
      ObjectIterator var2 = this.waiting.keySet().iterator();

      while (var2.hasNext()) {
         PickupList.TaggedItem s = (PickupList.TaggedItem)var2.next();
         ItemStack[] ingr = s.item.getItems();
         MutableComponent c = Component.literal(ingr.length > 1 ? "[" : "");

         for (int i = 0; i < ingr.length; i++) {
            ItemStack sta = ingr[i];
            if (i > 0) {
               c = c.append(" | ");
            }

            c = c.append(sta.getHoverName());
         }

         c = c.append(ingr.length > 1 ? "]" : "");
         Component itemName = Component.literal(this.waiting.getInt(s) + "x ").append(c);
         if (this.overrideStyles.containsKey(s)) {
            itemName = itemName.copy().setStyle(this.overrideStyles.get(s));
         }

         txts.add(itemName);
      }

      return txts;
   }

   public Object2IntArrayMap<PickupList.TaggedItem> getRestItems() {
      return this.waiting;
   }

   public boolean hasAnyMoreItems() {
      return !this.waiting.isEmpty();
   }

   public ListTag serializeNBT() {
      ListTag lst = new ListTag();
      ObjectIterator var2 = this.waiting.keySet().iterator();

      while (var2.hasNext()) {
         PickupList.TaggedItem item = (PickupList.TaggedItem)var2.next();
         CompoundTag tag = item.serializeNBT();
         tag.putInt("Count", this.waiting.getInt(item));
         lst.add(tag);
      }

      return lst;
   }

   public void deserializeNBT(ListTag nbt) {
      this.waiting.clear();

      for (int i = 0; i < nbt.size(); i++) {
         CompoundTag tag = nbt.getCompound(i);
         this.waiting.put(PickupList.TaggedItem.read(tag), tag.getInt("Count"));
      }
   }

   public Object2IntArrayMap<PickupList.TaggedItem> getInitial() {
      return this.initial;
   }

   public static class TaggedItem implements INBTSerializable<CompoundTag>, Predicate<ItemStack> {
      @Nonnull
      Ingredient item = Ingredient.EMPTY;

      TaggedItem() {
      }

      public TaggedItem(Ingredient ingredient) {
         this.item = ingredient;
      }

      public TaggedItem(ItemLike... item) {
         this.item = Ingredient.of(item);
      }

      public TaggedItem(Supplier<ItemStack> item) {
         this.item = StrictNBTIngredient.of(item.get());
      }

      public CompoundTag serializeNBT() {
         CompoundTag nbt = new CompoundTag();
         nbt.putString("json", this.item.toJson().toString());
         return nbt;
      }

      public void deserializeNBT(CompoundTag nbt) {
         this.item = Ingredient.fromJson(JsonParser.parseString(nbt.getString("json")));
      }

      public boolean test(ItemStack itemStack) {
         return this.item.test(itemStack);
      }

      public ItemStack asStack() {
         return RecipeHelper.cycleIngredientStack(this.item, 1000L);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            PickupList.TaggedItem that = (PickupList.TaggedItem)o;
            return Objects.equals(this.item, that.item);
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return Objects.hash(this.item);
      }

      public static PickupList.TaggedItem read(CompoundTag nbt) {
         PickupList.TaggedItem it = new PickupList.TaggedItem();
         it.deserializeNBT(nbt);
         return it;
      }
   }
}

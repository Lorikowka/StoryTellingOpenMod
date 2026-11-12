package com.storyteam.storytelling.system.clothes.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.system.clothes.Outfit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record VanityInventory(Map<VanitySlot, ItemStack> items) implements Iterable<ItemStack> {
   public VanityInventory() {
      this(Map.of());
   }

   public ItemStack getItem(VanitySlot slot) {
      return this.items.getOrDefault(slot, ItemStack.EMPTY).copy();
   }

   public VanityInventory withItem(VanitySlot slot, ItemStack stack) {
      if (stack != null && !stack.isEmpty()) {
         Map<VanitySlot, ItemStack> b = new HashMap<>();
         this.items.forEach((slot0, item) -> {
            if (slot != slot0) {
               b.put(slot0, item);
            }
         });
         b.put(slot, stack.copy());
         return new VanityInventory(Map.copyOf(b));
      } else if (!this.items.containsKey(slot)) {
         return this;
      } else {
         Map<VanitySlot, ItemStack> b = new HashMap<>();
         this.items.forEach((slot0, item) -> {
            if (slot != slot0) {
               b.put(slot0, item);
            }
         });
         return new VanityInventory(Map.copyOf(b));
      }
   }

   public VanityInventory copy() {
      return new VanityInventory(Map.copyOf(this.items));
   }

   @NotNull
   @Override
   public Iterator<ItemStack> iterator() {
      return this.items.values().iterator();
   }

   public ListTag serializeNBT() {
      ListTag tags = new ListTag();

      for (Entry<VanitySlot, ItemStack> e : this.items.entrySet()) {
         CompoundTag tag = new CompoundTag();
         tag.putString("Slot", e.getKey().getSerializedName());
         tag.put("Item", e.getValue().serializeNBT());
         tags.add(tag);
      }

      return tags;
   }

   public static VanityInventory deserializeNBT(ListTag tags) {
      Builder<VanitySlot, ItemStack> b = ImmutableMap.builder();

      for (int i = 0; i < tags.size(); i++) {
         CompoundTag tag = tags.getCompound(i);
         VanitySlot vs = VanitySlot.byName(tag.getString("Slot"));
         if (vs == null) {
            Storytelling.LOG.warn("Skipping unknown slot {} with item {}.", tag.getString("Slot"), tag.getCompound("Item"));
         } else {
            b.put(vs, ItemStack.of(tag.getCompound("Item")));
         }
      }

      return new VanityInventory(b.build());
   }

   public Outfit asOutfit() {
      Outfit.OutfitBuilder ofb = Outfit.builder();

      for (Entry<VanitySlot, ItemStack> e : this.items().entrySet()) {
         ofb.set(e.getKey(), e.getValue().copy());
      }

      return ofb.build();
   }

   public VanityInventory apply(Outfit outfit) {
      VanityInventory res = this;

      for (Entry<VanitySlot, ItemStack> e : outfit.items().entrySet()) {
         res = res.withItem(e.getKey(), e.getValue());
      }

      return res;
   }

   public void equip(LivingEntity entity) {
      if (!(entity instanceof AnimatedEntity he)) {
         for (VanitySlot slot : Set.of(VanitySlot.HEAD, VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET)) {
            ItemStack it = this.getItem(slot).copy();
            if (!it.isEmpty()) {
               entity.setItemSlot(slot.getPlayerSlot().getSlot(), it);
            }
         }
      } else {
         VanityInventory inv = he.getInventory();

         for (VanitySlot e : VanitySlot.values()) {
            inv = inv.withItem(e, this.getItem(e));
         }

         he.setInventory(inv);
      }
   }
}

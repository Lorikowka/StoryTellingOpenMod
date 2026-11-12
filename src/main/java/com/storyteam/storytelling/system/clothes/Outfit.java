package com.storyteam.storytelling.system.clothes;

import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record Outfit(Map<VanitySlot, ItemStack> items) {
   @Nullable
   public ItemStack get(VanitySlot slot) {
      return this.items.get(slot);
   }

   public Outfit clearUnused() {
      Outfit.OutfitBuilder b = this.derive();

      for (VanitySlot value : VanitySlot.values()) {
         if (!this.items.containsKey(value)) {
            b.set(value, ItemStack.EMPTY);
         }
      }

      return b.build();
   }

   public Outfit.OutfitBuilder derive() {
      Outfit.OutfitBuilder b = builder();

      for (Entry<VanitySlot, ItemStack> e : this.items.entrySet()) {
         b = b.set(e.getKey(), e.getValue());
      }

      return b;
   }

   public static Outfit of(VanitySet set) {
      return builder().set(set).build();
   }

   public static Outfit.OutfitBuilder builder() {
      return new Outfit.OutfitBuilder();
   }

   public static Outfit.OutfitBuilder builder(VanitySet set) {
      return builder().set(set);
   }

   public boolean has(VanitySlot slot) {
      ItemStack st = this.get(slot);
      return st != null && !st.isEmpty();
   }

   public static class OutfitBuilder {
      private final Map<VanitySlot, ItemStack> items = new HashMap<>();

      public Outfit.OutfitBuilder hair(ItemStack item) {
         return this.set(VanitySlot.HAIR, item);
      }

      public Outfit.OutfitBuilder hat(ItemStack item) {
         return this.set(VanitySlot.HAT, item);
      }

      public Outfit.OutfitBuilder head(ItemStack item) {
         return this.set(VanitySlot.HEAD, item);
      }

      public Outfit.OutfitBuilder body(ItemStack item) {
         return this.set(VanitySlot.BODY, item);
      }

      public Outfit.OutfitBuilder bracerLeft(ItemStack item) {
         return this.set(VanitySlot.BRACER_LEFT, item);
      }

      public Outfit.OutfitBuilder bracerRight(ItemStack item) {
         return this.set(VanitySlot.BRACER_RIGHT, item);
      }

      public Outfit.OutfitBuilder badge(ItemStack item) {
         return this.set(VanitySlot.BADGE, item);
      }

      public Outfit.OutfitBuilder legs(ItemStack item) {
         return this.set(VanitySlot.LEGS, item);
      }

      public Outfit.OutfitBuilder feet(ItemStack item) {
         return this.set(VanitySlot.FEET, item);
      }

      public Outfit.OutfitBuilder bracelet(ItemStack item) {
         return this.set(VanitySlot.BRACELET, item);
      }

      public Outfit.OutfitBuilder headphones(ItemStack item) {
         return this.set(VanitySlot.HEAD_PHONES, item);
      }

      public Outfit.OutfitBuilder body_accessories(ItemStack item) {
         return this.set(VanitySlot.BODY_ACCESSORIES, item);
      }

      public Outfit.OutfitBuilder noHead() {
         return this.head(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noHair() {
         return this.hair(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noHat() {
         return this.hat(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noBody() {
         return this.body(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noBracerLeft() {
         return this.bracerLeft(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noBracerRight() {
         return this.bracerRight(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noBadge() {
         return this.badge(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noLegs() {
         return this.legs(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noFeet() {
         return this.feet(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noBracelet() {
         return this.bracelet(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder noHeadPhones() {
         return this.headphones(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder no_body_accessories() {
         return this.body_accessories(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder hair(VanitySet set) {
         return this.set(VanitySlot.HAIR, set);
      }

      public Outfit.OutfitBuilder head(VanitySet set) {
         return this.set(VanitySlot.HEAD, set);
      }

      public Outfit.OutfitBuilder hat(VanitySet set) {
         return this.set(VanitySlot.HAT, set);
      }

      public Outfit.OutfitBuilder badge(VanitySet set) {
         return this.set(VanitySlot.BADGE, set);
      }

      public Outfit.OutfitBuilder body(VanitySet set) {
         return this.set(VanitySlot.BODY, set);
      }

      public Outfit.OutfitBuilder bracerLeft(VanitySet set) {
         return this.set(VanitySlot.BRACER_LEFT, set);
      }

      public Outfit.OutfitBuilder bracerRight(VanitySet set) {
         return this.set(VanitySlot.BRACER_RIGHT, set);
      }

      public Outfit.OutfitBuilder legs(VanitySet set) {
         return this.set(VanitySlot.LEGS, set);
      }

      public Outfit.OutfitBuilder feet(VanitySet set) {
         return this.set(VanitySlot.FEET, set);
      }

      public Outfit.OutfitBuilder bracelet(VanitySet set) {
         return this.set(VanitySlot.BRACELET, set);
      }

      public Outfit.OutfitBuilder headphones(VanitySet set) {
         return this.set(VanitySlot.HEAD_PHONES, set);
      }

      public Outfit.OutfitBuilder body_accessories(VanitySet set) {
         return this.set(VanitySlot.BODY_ACCESSORIES, set);
      }

      public Outfit.OutfitBuilder glasses(VanitySet set) {
         return this.set(VanitySlot.GLASSES, set);
      }

      public Outfit.OutfitBuilder glasses(ItemStack item) {
         return this.set(VanitySlot.GLASSES, item);
      }

      public Outfit.OutfitBuilder noGlasses() {
         return this.glasses(ItemStack.EMPTY);
      }

      public Outfit.OutfitBuilder nothing() {
         for (VanitySlot value : VanitySlot.values()) {
            this.items.put(value, ItemStack.EMPTY);
         }

         return this;
      }

      public Outfit.OutfitBuilder set(VanitySlot slot, ItemStack stack) {
         this.items.put(slot, stack);
         return this;
      }

      public Outfit.OutfitBuilder set(VanitySlot slot, VanitySet set) {
         return !set.has(slot) ? this : this.set(slot, set.get(slot).getDefaultInstance());
      }

      public Outfit.OutfitBuilder set(VanitySet set) {
         set.items().forEach(i -> this.set(i.getKey(), i.getValue().getDefaultInstance()));
         return this;
      }

      public Outfit build() {
         return new Outfit(Map.copyOf(this.items));
      }
   }
}

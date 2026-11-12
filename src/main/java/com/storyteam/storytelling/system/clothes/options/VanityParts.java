package com.storyteam.storytelling.system.clothes.options;

import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import lombok.Generated;

public record VanityParts(Set<VanitySlot> slots) {
   public static final VanityParts ARMOR = of(VanitySlot.HEAD, VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET);
   public static final VanityParts ARMOR_NO_FEET = of(VanitySlot.HEAD, VanitySlot.BODY, VanitySlot.LEGS);
   public static final VanityParts BODY_FEET_HAT_LEGS = of(VanitySlot.BODY, VanitySlot.FEET, VanitySlot.HAT, VanitySlot.LEGS);
   public static final VanityParts ARMOR_WITH_HAIR = of(VanitySlot.HAIR, VanitySlot.HEAD, VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET);
   public static final VanityParts ARMOR_NO_HEAD = of(VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET);
   public static final VanityParts ARMOR_NO_HEAD_WITH_HAIR = of(VanitySlot.HAIR, VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET);
   public static final VanityParts ARMOR_NO_HEAD_NO_LEGS_WITH_HAIR = of(VanitySlot.HAIR, VanitySlot.BODY, VanitySlot.FEET);
   public static final VanityParts ARMOR_NO_HEAD_NO_FEET_NO_HAIR = of(VanitySlot.BODY, VanitySlot.LEGS);
   public static final VanityParts ARMOR_NO_HEAD_NO_LEGS_NO_HAIR = of(VanitySlot.BODY, VanitySlot.FEET);
   public static final VanityParts ARMOR_BODY_LEGS_HAIR = of(VanitySlot.HAIR, VanitySlot.BODY, VanitySlot.LEGS);
   public static final VanityParts ARMOR_NO_HEAD_NO_FEET_WITH_HAIR = of(VanitySlot.HAIR, VanitySlot.BODY, VanitySlot.LEGS);
   public static final VanityParts ARMOR_WITH_HAIR_WITH_BRACELET = of(
      VanitySlot.HAIR, VanitySlot.HEAD, VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET, VanitySlot.BRACELET
   );
   public static final VanityParts ONLY_BODY = of(VanitySlot.BODY);
   public static final VanityParts ONLY_LEGS = of(VanitySlot.LEGS);
   public static final VanityParts ONLY_HEAD = of(VanitySlot.HEAD);
   public static final VanityParts ONLY_HAIR = of(VanitySlot.HAIR);
   public static final VanityParts BRACER = of(VanitySlot.BRACER_RIGHT, VanitySlot.BRACER_LEFT);
   public static final VanityParts ONLY_BRACER_LEFT = of(VanitySlot.BRACER_LEFT);
   public static final VanityParts ONLY_BRACER_RIGHT = of(VanitySlot.BRACER_RIGHT);
   public static final VanityParts ALL = of(VanitySlot.values());
   public static final VanityParts NONE = of();
   public static final VanityParts HEAD_HAIR_LEGS = of(VanitySlot.HEAD, VanitySlot.HAIR, VanitySlot.LEGS);
   public static final VanityParts ONLY_GLASSES = of(VanitySlot.GLASSES);

   public boolean has(VanitySlot slot) {
      return this.slots.contains(slot);
   }

   public VanityParts not() {
      HashSet<VanitySlot> all = new HashSet<>(ALL.slots());
      all.removeAll(this.slots);
      return new VanityParts(Set.copyOf(all));
   }

   public VanityParts or(VanityParts b) {
      HashSet<VanitySlot> all = new HashSet<>(this.slots());
      all.addAll(b.slots());
      return new VanityParts(Set.copyOf(all));
   }

   public VanityParts filter(Predicate<VanitySlot> slots) {
      HashSet<VanitySlot> all = new HashSet<>(this.slots());
      all.removeIf(slots.negate());
      return new VanityParts(all);
   }

   public static VanityParts of(VanitySlot... slots) {
      return new VanityParts(Set.of(slots));
   }

   @Generated
   public static VanityParts.VanityPartsBuilder builder() {
      return new VanityParts.VanityPartsBuilder();
   }

   @Generated
   public static class VanityPartsBuilder {
      @Generated
      private Set<VanitySlot> slots;

      @Generated
      VanityPartsBuilder() {
      }

      @Generated
      public VanityParts.VanityPartsBuilder slots(Set<VanitySlot> slots) {
         this.slots = slots;
         return this;
      }

      @Generated
      public VanityParts build() {
         return new VanityParts(this.slots);
      }

      @Generated
      @Override
      public String toString() {
         return "VanityParts.VanityPartsBuilder(slots=" + this.slots + ")";
      }
   }
}

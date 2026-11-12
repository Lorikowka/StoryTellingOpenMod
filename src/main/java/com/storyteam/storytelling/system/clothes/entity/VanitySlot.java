package com.storyteam.storytelling.system.clothes.entity;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.Util;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem.Type;

public enum VanitySlot implements StringRepresentable {
   HEAD("head", Type.HELMET),
   BODY("body", Type.CHESTPLATE),
   LEGS("legs", Type.LEGGINGS),
   FEET("feet", Type.BOOTS),
   HAIR("hair", Type.HELMET),
   HAT("hat", Type.HELMET),
   SCARF("scarf", Type.HELMET),
   BADGE("badge", Type.CHESTPLATE),
   BRACELET("bracelet", Type.CHESTPLATE),
   HEAD_PHONES("headphones", Type.HELMET),
   GLASSES("glasses", Type.HELMET),
   BODY_ACCESSORIES("body_accessories", Type.CHESTPLATE),
   BRACER_RIGHT("bracer_right", Type.CHESTPLATE),
   BRACER_LEFT("bracer_left", Type.CHESTPLATE);

   private final String name;
   private final Type playerSlot;
   private static final Map<String, VanitySlot> SLOT_REGISTRY = (Map<String, VanitySlot>)Util.make(new HashMap(), m -> {
      for (VanitySlot value : values()) {
         m.put(value.getSerializedName(), value);
      }
   });

   private VanitySlot(String name, Type playerSlot) {
      this.name = name;
      this.playerSlot = playerSlot;
   }

   public String getSerializedName() {
      return this.name;
   }

   public static VanitySlot byName(String slot) {
      return SLOT_REGISTRY.get(slot);
   }

   @Generated
   public Type getPlayerSlot() {
      return this.playerSlot;
   }
}

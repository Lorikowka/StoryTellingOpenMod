package com.storyteam.storytelling.system.clothes.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.crafting.Ingredient;

public class VanityArmorMaterial implements ArmorMaterial {
   protected final String name;

   public VanityArmorMaterial(ResourceLocation id) {
      this.name = id.toString();
   }

   public int getDurabilityForType(Type pType) {
      return 0;
   }

   public int getDefenseForType(Type pType) {
      return 0;
   }

   public int getEnchantmentValue() {
      return 0;
   }

   public SoundEvent getEquipSound() {
      return SoundEvents.ARMOR_EQUIP_GENERIC;
   }

   public Ingredient getRepairIngredient() {
      return Ingredient.EMPTY;
   }

   public String getName() {
      return this.name;
   }

   public float getToughness() {
      return 0.0F;
   }

   public float getKnockbackResistance() {
      return 0.0F;
   }
}

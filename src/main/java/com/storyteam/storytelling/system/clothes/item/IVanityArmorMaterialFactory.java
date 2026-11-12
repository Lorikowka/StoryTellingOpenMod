package com.storyteam.storytelling.system.clothes.item;

import com.storyteam.storytelling.system.clothes.VanitySet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;

public interface IVanityArmorMaterialFactory {
   IVanityArmorMaterialFactory DEFAULT = (set, id) -> new VanityArmorMaterial(id);

   ArmorMaterial create(VanitySet var1, ResourceLocation var2);

   static IVanityArmorMaterialFactory withDefense(int headDef, int bodyDef, int leggingsDef, int bootsDef) {
      return (set, id) -> new DefensiveVanityArmorMaterial(id, headDef, bodyDef, leggingsDef, bootsDef);
   }
}

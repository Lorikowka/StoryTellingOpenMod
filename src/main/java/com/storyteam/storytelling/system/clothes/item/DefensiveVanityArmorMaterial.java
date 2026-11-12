package com.storyteam.storytelling.system.clothes.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem.Type;

public class DefensiveVanityArmorMaterial extends VanityArmorMaterial {
   protected int headDef;
   protected int bodyDef;
   protected int leggingsDef;
   protected int bootsDef;

   public DefensiveVanityArmorMaterial(ResourceLocation id, int headDef, int bodyDef, int leggingsDef, int bootsDef) {
      super(id);
      this.headDef = headDef;
      this.bodyDef = bodyDef;
      this.leggingsDef = leggingsDef;
      this.bootsDef = bootsDef;
   }

   @Override
   public int getDefenseForType(Type pType) {
      return switch (pType) {
         case HELMET -> this.headDef;
         case CHESTPLATE -> this.bodyDef;
         case LEGGINGS -> this.leggingsDef;
         case BOOTS -> this.bootsDef;
         default -> throw new IncompatibleClassChangeError();
      };
   }
}

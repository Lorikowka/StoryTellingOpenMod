package com.storyteam.storytelling.init.items;

import com.storyteam.storytelling.init.geo.VanityModelsST;
import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.system.clothes.options.IVanityTextureProvider;
import com.storyteam.storytelling.system.clothes.options.VanitySetOptions;
import java.util.List;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "armor/vanity/zombie_clothes/"
)
public interface VanityArmorZombie {
   @RegistryName("zombie_wearing_1")
   VanitySet ZOMBIE_WEAR_1 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .setParts(VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET)
      .rename(VanitySlot.BODY, "Одежда зомби")
      .rename(VanitySlot.LEGS, "Штаны зомби")
      .rename(VanitySlot.FEET, "Обувь зомби")
      .create();
   @RegistryName("zombie_wearing_2")
   VanitySet ZOMBIE_WEAR_2 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .setParts(VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET)
      .rename(VanitySlot.BODY, "Одежда зомби")
      .rename(VanitySlot.LEGS, "Штаны зомби")
      .rename(VanitySlot.FEET, "Обувь зомби")
      .create();
   @RegistryName("zombie_wearing_3")
   VanitySet ZOMBIE_WEAR_3 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .setParts(VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET)
      .rename(VanitySlot.BODY, "Одежда зомби")
      .rename(VanitySlot.LEGS, "Штаны зомби")
      .rename(VanitySlot.FEET, "Обувь зомби")
      .create();
   @RegistryName("zombie_wearing_4")
   VanitySet ZOMBIE_WEAR_4 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .setParts(VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET)
      .rename(VanitySlot.BODY, "Одежда зомби")
      .rename(VanitySlot.LEGS, "Штаны зомби")
      .rename(VanitySlot.FEET, "Обувь зомби")
      .create();
   List<VanitySet> ZOMBIE_CLOTHES = List.of(ZOMBIE_WEAR_1, ZOMBIE_WEAR_2, ZOMBIE_WEAR_3, ZOMBIE_WEAR_4);
}

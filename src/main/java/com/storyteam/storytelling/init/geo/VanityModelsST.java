package com.storyteam.storytelling.init.geo;

import com.storyteam.storytelling.system.clothes.VanityModels;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.system.clothes.options.VanityModelOptions;
import com.storyteam.storytelling.system.clothes.options.VanityParts;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "armor/vanity/"
)
public interface VanityModelsST {
   @RegistryName("simple_steve")
   VanityModels SIMPLE_STEVE = VanityModelOptions.builder().parts(VanityParts.ARMOR).create();
   @RegistryName("lolowear")
   VanityModels LOLOWEAR = VanityModelOptions.builder().parts(VanityParts.ARMOR).create();
   @RegistryName("hunter_garb")
   VanityModels HUNTER_GARB = VanityModelOptions.builder().parts(VanityParts.ARMOR).create();
   @RegistryName("mensis_cage")
   VanityModels MENSIS_CAGE = VanityModelOptions.builder().parts(VanityParts.ONLY_HEAD).create();
   @RegistryName("civillian_clothes")
   VanityModels CIVILLIAN_CLOTHES = VanityModelOptions.builder()
      .parts(VanityParts.of(VanitySlot.BODY, VanitySlot.HAIR))
      .overrideBaseTex(EntityModelsST.CITY_ALEX, "alex")
      .create();
   @RegistryName("civillian_clothes_ovveride_split")
   VanityModels CIVILLIAN_CLOTHES_OVERRIDE_SPLIT = VanityModelOptions.builder()
      .parts(VanityParts.of(VanitySlot.BODY, VanitySlot.HAIR))
      .overrideSplitTex(EntityModelsST.CITY_ALEX, "alex")
      .create();
}

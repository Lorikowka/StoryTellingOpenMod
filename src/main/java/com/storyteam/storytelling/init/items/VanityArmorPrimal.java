package com.storyteam.storytelling.init.items;

import com.storyteam.storytelling.init.geo.VanityModelsST;
import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.system.clothes.options.IVanityTextureProvider;
import com.storyteam.storytelling.system.clothes.options.VanityParts;
import com.storyteam.storytelling.system.clothes.options.VanitySetOptions;
import java.util.List;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "armor/vanity/primal_clothes/"
)
public interface VanityArmorPrimal {
   @RegistryName("primal_wearing_1")
   VanitySet PRIMAL_WEAR_1 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Первобытная одежда")
      .create();
   @RegistryName("primal_wearing_2")
   VanitySet PRIMAL_WEAR_2 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Первобытная одежда")
      .create();
   @RegistryName("primal_wearing_3")
   VanitySet PRIMAL_WEAR_3 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Первобытная одежда")
      .create();
   @RegistryName("primal_wearing_4")
   VanitySet PRIMAL_WEAR_4 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Первобытная одежда")
      .create();
   @RegistryName("primal_wearing_deluxe")
   VanitySet PRIMAL_WEAR_DELUXE = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Первобытная одежда люкс")
      .create();
   @RegistryName("primal_wearing_deluxe_scint")
   VanitySet PRIMAL_WEAR_DELUXE_SCINT = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Первобытная одежда люкс скинт")
      .create();
   @RegistryName("primal_deluxe_hair")
   VanitySet PRIMAL_DELUXE_HAIR = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_HAIR)
      .rename(VanitySlot.HAIR, "Первобытные волосы люкс")
      .create();
   @RegistryName("primal_deluxe_hair_scint")
   VanitySet PRIMAL_DELUXE_HAIR_SCINT = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_HAIR)
      .rename(VanitySlot.HAIR, "Первобытные волосы люкс скинт")
      .create();
   List<VanitySet> PRIMAL_CLOTHES = List.of(PRIMAL_WEAR_1, PRIMAL_WEAR_2, PRIMAL_WEAR_3, PRIMAL_WEAR_4);
}

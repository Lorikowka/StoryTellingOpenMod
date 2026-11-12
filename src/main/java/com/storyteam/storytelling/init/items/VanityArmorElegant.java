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
   prefix = "armor/vanity/elegant_clothes/"
)
public interface VanityArmorElegant {
   @RegistryName("steve_elegant_1")
   VanitySet STEVE_ELEGANT_1 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Красивая мужская одежда")
      .create();
   @RegistryName("steve_elegant_2")
   VanitySet STEVE_ELEGANT_2 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Красивая мужская одежда")
      .create();
   @RegistryName("steve_elegant_3")
   VanitySet STEVE_ELEGANT_3 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Красивая мужская одежда")
      .create();
   @RegistryName("steve_elegant_4")
   VanitySet STEVE_ELEGANT_4 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Красивая мужская одежда")
      .create();
   @RegistryName("alex_elegant_1")
   VanitySet ALEX_ELEGANT_1 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Красивая женская одежда")
      .create();
   @RegistryName("alex_elegant_2")
   VanitySet ALEX_ELEGANT_2 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Красивая женская одежда")
      .create();
   @RegistryName("alex_elegant_3")
   VanitySet ALEX_ELEGANT_3 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Красивая женская одежда")
      .create();
   @RegistryName("alex_elegant_4")
   VanitySet ALEX_ELEGANT_4 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES)
      .textures(IVanityTextureProvider.UNIFIED_TEXTURE)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Красивая женская одежда")
      .create();
   List<VanitySet> STEVE_ELEGANT_CLOTHES = List.of(STEVE_ELEGANT_1, STEVE_ELEGANT_2, STEVE_ELEGANT_3, STEVE_ELEGANT_4);
   List<VanitySet> ALEX_ELEGANT_CLOTHES = List.of(ALEX_ELEGANT_1, ALEX_ELEGANT_2, ALEX_ELEGANT_3, ALEX_ELEGANT_4);
}

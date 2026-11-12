package com.storyteam.storytelling.init.items;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.init.geo.VanityModelsST;
import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.system.clothes.options.IVanityTextureProvider;
import com.storyteam.storytelling.system.clothes.options.VanityParts;
import com.storyteam.storytelling.system.clothes.options.VanitySetOptions;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "armor/vanity/"
)
public interface VanityArmorST {
   TagKey<Item> VANITY_TAG = ItemTags.create(St.id("vanity"));
   @RegistryName("lolowear")
   VanitySet LOLOWEAR = VanitySetOptions.builder()
      .geometry(VanityModelsST.LOLOWEAR)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ARMOR)
      .rename(VanitySlot.HEAD, "Очки Ло")
      .rename(VanitySlot.BODY, "Осенняя куртка \"Мироходец\"")
      .rename(VanitySlot.LEGS, "Осенние штаны \"Мироходец\"")
      .rename(VanitySlot.FEET, "Осенняя обувь \"Мироходец\"")
      .rename(VanitySlot.SCARF, "Шарф")
      .create();
   @RegistryName("lolowear_summer")
   VanitySet LOLOWEAR_SUMMER = VanitySetOptions.builder()
      .geometry(VanityModelsST.LOLOWEAR)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ARMOR)
      .rename(VanitySlot.HEAD, "Очки Ло")
      .rename(VanitySlot.BODY, "Куртка \"Мироходец\"")
      .rename(VanitySlot.LEGS, "Штаны \"Мироходец\"")
      .rename(VanitySlot.FEET, "Обувь \"Мироходец\"")
      .rename(VanitySlot.SCARF, "Шарф")
      .create();
   @RegistryName("loloarmor")
   VanitySet LOLOARMOR = VanitySetOptions.builder()
      .geometry(VanityModelsST.SIMPLE_STEVE)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ARMOR_NO_HEAD)
      .rename(VanitySlot.BODY, "Нагрудник Мироходца")
      .rename(VanitySlot.LEGS, "Поножи Мироходца")
      .rename(VanitySlot.FEET, "Сапоги Мироходца")
      .useBuiltinItemModels(false)
      .create();
   @RegistryName("civillian_hair_1")
   VanitySet CIVILLIAN_HAIR_1 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_HAIR)
      .rename(VanitySlot.HAIR, "Волосы")
      .create();
   @RegistryName("civillian_hair_2")
   VanitySet CIVILLIAN_HAIR_2 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_HAIR)
      .rename(VanitySlot.HAIR, "Волосы")
      .create();
   @RegistryName("civillian_hair_3")
   VanitySet CIVILLIAN_HAIR_3 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_HAIR)
      .rename(VanitySlot.HAIR, "Волосы")
      .create();
   @RegistryName("civillian_hair_4")
   VanitySet CIVILLIAN_HAIR_4 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_HAIR)
      .rename(VanitySlot.HAIR, "Волосы")
      .create();
   @RegistryName("civillian_hair_5")
   VanitySet CIVILLIAN_HAIR_5 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_HAIR)
      .rename(VanitySlot.HAIR, "Волосы")
      .create();
   @RegistryName("civillian_clothes_1")
   VanitySet CIVILLIAN_CLOTHES_1 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .nameOverrides(new EnumMap<>(Map.of(VanitySlot.BODY, "zhezh")))
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_2")
   VanitySet CIVILLIAN_CLOTHES_2 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_3")
   VanitySet CIVILLIAN_CLOTHES_3 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_4")
   VanitySet CIVILLIAN_CLOTHES_4 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_5")
   VanitySet CIVILLIAN_CLOTHES_5 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_6")
   VanitySet CIVILLIAN_CLOTHES_6 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_7")
   VanitySet CIVILLIAN_CLOTHES_7 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_8")
   VanitySet CIVILLIAN_CLOTHES_8 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_9")
   VanitySet CIVILLIAN_CLOTHES_9 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_10")
   VanitySet CIVILLIAN_CLOTHES_10 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_11")
   VanitySet CIVILLIAN_CLOTHES_11 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   @RegistryName("civillian_clothes_12")
   VanitySet CIVILLIAN_CLOTHES_12 = VanitySetOptions.builder()
      .geometry(VanityModelsST.CIVILLIAN_CLOTHES_OVERRIDE_SPLIT)
      .textures(IVanityTextureProvider.SPLIT_TEXTURES)
      .parts(VanityParts.ONLY_BODY)
      .rename(VanitySlot.BODY, "Городская одежда")
      .create();
   List<VanitySet> HAIR = List.of(CIVILLIAN_HAIR_1, CIVILLIAN_HAIR_2, CIVILLIAN_HAIR_3, CIVILLIAN_HAIR_4, CIVILLIAN_HAIR_5);
   List<VanitySet> CIVILLIAN_OUTFITS = List.of(
      CIVILLIAN_CLOTHES_1,
      CIVILLIAN_CLOTHES_2,
      CIVILLIAN_CLOTHES_3,
      CIVILLIAN_CLOTHES_4,
      CIVILLIAN_CLOTHES_5,
      CIVILLIAN_CLOTHES_6,
      CIVILLIAN_CLOTHES_7,
      CIVILLIAN_CLOTHES_8,
      CIVILLIAN_CLOTHES_9,
      CIVILLIAN_CLOTHES_10,
      CIVILLIAN_CLOTHES_11,
      CIVILLIAN_CLOTHES_12
   );
}

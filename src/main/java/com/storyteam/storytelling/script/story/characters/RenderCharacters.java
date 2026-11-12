package com.storyteam.storytelling.script.story.characters;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import com.storyteam.storytelling.init.items.VanityArmorST;
import com.storyteam.storytelling.system.clothes.Outfit;

public interface RenderCharacters {
   RenderCharacter DEFAULT_CHARACTER = RenderCharacter.builder()
      .name("Хуманоид (стив)")
      .model(EntityModelsST.CITY_STEVE)
      .texture(St.tx("entity/city_steve/city_steve_base1"))
      .outfit(Outfit.of(VanityArmorST.LOLOWEAR).derive().noHead().set(VanityArmorST.CIVILLIAN_HAIR_1).build())
      .build();
   RenderCharacter ZEITHERON = RenderCharacter.builder()
      .name("Зейтерон")
      .model(EntityModelsST.ZEITHERON_SERGAL)
      .texture(St.tx("entity/st_team/zeitheron_sergal"))
      .withEmission()
      .build();
   RenderCharacter ARCHMAGE = RenderCharacter.builder().name("Архимаг").model(EntityModelsST.CITY_STEVE).texture(St.tx("entity/st_team/archmage")).build();
   RenderCharacter ANT = RenderCharacter.builder().name("Формик").model(EntityModelsST.ANT).texture(St.tx("entity/ant")).build();
   RenderCharacter SPIDER_HEAD = RenderCharacter.builder()
      .name("Лошка на ножках")
      .model(EntityModelsST.SPIDER_HEAD_LOLOLOSHKA)
      .texture(St.tx("entity/spider_head_lololoshka"))
      .build();
   RenderCharacter HEAD_LOLOLOSHKA = RenderCharacter.builder()
      .name("Lololowka")
      .model(EntityModelsST.HEAD_LOLOLOSHKA)
      .texture(St.tx("entity/head_lololoshka"))
      .build();
   RenderCharacter HEAD_LOLOLOSHKA_ANT = RenderCharacter.builder()
      .name("Lololowka")
      .model(EntityModelsST.HEAD_LOLOLOSHKA_ANT)
      .texture(St.tx("entity/spider_head_lololoshka"))
      .build();
   RenderCharacter CUTSCENE_MERGE_ANT = RenderCharacter.builder().name("Формики").model(EntityModelsST.CUTSCENE_MERGE_ANT).texture(St.tx("entity/ant")).build();
   RenderCharacter BLUE_MAN = RenderCharacter.builder().name("Синий Мужчина").model(EntityModelsST.BLUE_MAN).texture(St.tx("entity/blue_man")).build();
}

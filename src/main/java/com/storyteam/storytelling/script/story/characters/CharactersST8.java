package com.storyteam.storytelling.script.story.characters;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.content.entities.AntGroupEntity;
import com.storyteam.storytelling.content.entities.GlitchCloudEntity;
import com.storyteam.storytelling.content.entities.HeadLololoshkaAntEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.content.entities.RotateStatueEntity;
import com.storyteam.storytelling.content.entities.SkyEyeEntity;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.init.EntitiesST;
import org.zeith.hammerlib.util.java.Cast;

public interface CharactersST8 {
   Character<SkyEyeEntity> SKY_EYE = Character.builder("sky_eye", Cast.constant(EntitiesST.SKY_EYE)).name("???").build();
   Character<AntEntity> ANT = Character.builder("ant", Cast.constant(EntitiesST.ANT)).name("Формик").build();
   Characters.CharactersFactory<AntEntity> ANT_FAC = Characters.CharactersFactory.<AntEntity>builder()
      .setTexturesForType(EntitiesST.ANT, St.tx("entity/ant"))
      .setDefaultName("Формик")
      .build();
   Characters.CharactersFactory<HumanoidEntity> ZOMBIE_FAC = Characters.CharactersFactory.<HumanoidEntity>builder()
      .setTexturesForType(EntitiesST.ZEITHERON, St.tx("entity/st_team/zeitheron_sergal"))
      .setDefaultName("Зомби")
      .build();
   Character<HumanoidEntity> HEAD_LOLOLOSHKA = Character.builder("head_lololoshka", Cast.constant(EntitiesST.HEAD_LOLOLOSHKA)).name("Lololowka").build();
   Character<HeadLololoshkaAntEntity> HEAD_LOLOLOSHKA_ANT = Character.builder("head_lololoshka_ant", Cast.constant(EntitiesST.HEAD_LOLOLOSHKA_ANT))
      .name("Lololowka")
      .build();
   Character<AnimatedEntity> SPIDER_HEAD_LOLOLOSHKA = Character.builder("spider_head_lololoshka", Cast.constant(EntitiesST.SPIDER_HEAD_LOLOLOSHKA))
      .name("Lololowka")
      .build();
   Character<AnimatedEntity> ANU = Character.builder("anu", Cast.constant(EntitiesST.ANU)).name("Ану").build();
   Character<BaseMob> MUD_BOSS = Character.builder("mud_boss", Cast.constant(EntitiesST.MUD_BOSS)).name("Земляной Босс").build();
   Character<HumanoidEntity> UNIT_BASE_BIGGER = Character.builder("unit_base_bigger", Cast.constant(EntitiesST.UNIT_BASE_BIGGER))
      .name("Большой Формик")
      .build();
   Character<AntGroupEntity> ANT_PILLAR = Character.builder("ant_pillar", Cast.constant(EntitiesST.ANT_PILLAR)).name("Формики").build();
   Character<HumanoidEntity> CUTSCENE_MERGE_ANT = Character.builder("cutscene_merge_ant", Cast.constant(EntitiesST.CUTSCENE_MERGE_ANT)).name("Формики").build();
   Character<HumanoidEntity> BLUE_MAN = Character.builder("blue_man", Cast.constant(EntitiesST.BLUE_MAN)).name("Синий Мужчина").build();
   Character<BaseMob> FLYTRAP = Character.builder("flytrap", Cast.constant(EntitiesST.FLYTRAP)).name("Мухоловка").build();
   Character<BaseMob> FLYTRAP_GRAY = Character.builder("flytrap_gray", Cast.constant(EntitiesST.FLYTRAP))
      .name("Мухоловка (но серая)")
      .textured(St.tx("entity/flytrap_gray"))
      .build();
   Character<AnimatedEntity> CLOWN_BOX = Character.builder("clown_box", Cast.constant(EntitiesST.CLOWN_BOX)).name("Лолоклоун").build();
   Character<AnimatedEntity> CLOWN_BOX_GRAY = Character.builder("clown_box_gray", Cast.constant(EntitiesST.CLOWN_BOX))
      .name("Лолоклоун (но серый)")
      .textured(St.tx("entity/clown_box_gray"))
      .build();
   Character<AnimatedEntity> BUTTERFLY_1 = Character.builder("butterfly_1", Cast.constant(EntitiesST.BUTTERFLY))
      .name("Бабочька")
      .textured(St.tx("entity/butterfly/butterfly_1"))
      .build();
   Character<AnimatedEntity> BUTTERFLY_2 = Character.builder("butterfly_2", Cast.constant(EntitiesST.BUTTERFLY))
      .name("Бабочька")
      .textured(St.tx("entity/butterfly/butterfly_2"))
      .build();
   Character<AnimatedEntity> BUTTERFLY_3 = Character.builder("butterfly_3", Cast.constant(EntitiesST.BUTTERFLY))
      .name("Бабочька")
      .textured(St.tx("entity/butterfly/butterfly_3"))
      .build();
   Character<AnimatedEntity> BUTTERFLY_GRAY = Character.builder("butterfly_gray", Cast.constant(EntitiesST.BUTTERFLY))
      .name("Бабочька (но серая)")
      .textured(St.tx("entity/butterfly/butterfly_gray"))
      .build();
   Character<AnimatedEntity> JDH_CLOUD = Character.builder("jdh_cloud", Cast.constant(EntitiesST.JDH_CLOUD)).name("JDH Cloud").build();
   Character<AnimatedEntity> PINK_AIR_BALOON = Character.builder("pink_air_baloon", Cast.constant(EntitiesST.PINK_AIR_BALOON)).name("Воздушный шар").build();
   Character<RotateStatueEntity> STATUE_1 = Character.builder("statue_1", Cast.constant(EntitiesST.STATUE_1)).name("Статуя 1").build();
   Character<RotateStatueEntity> STATUE_2 = Character.builder("statue_2", Cast.constant(EntitiesST.STATUE_2)).name("Статуя 2").build();
   Character<RotateStatueEntity> STATUE_3 = Character.builder("statue_3", Cast.constant(EntitiesST.STATUE_3)).name("Статуя 3").build();
   Character<RotateStatueEntity> STATUE_4 = Character.builder("statue_4", Cast.constant(EntitiesST.STATUE_4)).name("Статуя 4").build();
   Character<RotateStatueEntity> STATUE_5 = Character.builder("statue_5", Cast.constant(EntitiesST.STATUE_5)).name("Статуя 5").build();
   Character<RotateStatueEntity> STATUE_6 = Character.builder("statue_6", Cast.constant(EntitiesST.STATUE_6)).name("Статуя 6").build();
   Character<RotateStatueEntity> STATUE_7 = Character.builder("statue_7", Cast.constant(EntitiesST.STATUE_7)).name("Статуя 7").build();
   Character<RotateStatueEntity> STATUE_8 = Character.builder("statue_8", Cast.constant(EntitiesST.STATUE_8)).name("Статуя 8").build();
   Character<GlitchCloudEntity> GLITCH_CLOUD = Character.builder("glitch_cloud", Cast.constant(EntitiesST.GLITCH_CLOUD)).name("Глитчевое блачко").build();
   Character<HumanoidEntity> GIRL_HEAD = Character.builder("girl_head", Cast.constant(EntitiesST.GIRL_HEAD)).name("Женская голова").build();
}

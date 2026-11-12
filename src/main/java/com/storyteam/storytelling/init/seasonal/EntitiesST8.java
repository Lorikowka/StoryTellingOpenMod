package com.storyteam.storytelling.init.seasonal;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.animations.AnimationsST6;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.client.render.entity.def.RenderContext;
import com.storyteam.storytelling.client.render.entity.def.RenderKey;
import com.storyteam.storytelling.client.render.entity.def.impl.CommonRenderDefinition;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.content.entities.AntGroupEntity;
import com.storyteam.storytelling.content.entities.GlitchCloudEntity;
import com.storyteam.storytelling.content.entities.HeadLololoshkaAntEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.content.entities.RiddleEntity;
import com.storyteam.storytelling.content.entities.RotateStatueEntity;
import com.storyteam.storytelling.content.entities.SkyEyeEntity;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.content.entities.configurations.Sex;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import com.storyteam.storytelling.system.currency.content.entities.EidosEntity;
import com.storyteam.storytelling.system.currency.content.entities.EidosGuiEntity;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import org.zeith.hammeranims.api.animation.LoopMode;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface EntitiesST8 {
   @RegistryName("sky_eye")
   EntityType<SkyEyeEntity> SKY_EYE = EntitiesST.createAnimated(SkyEyeEntity::new, IDefaultAnimConfiguration.noAnims())
      .texture(St.tx("entity/sky_eye"))
      .model(EntityModelsST.SKY_EYE)
      .clientTrackingRange(134217727)
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.SkyEyeRenderer)
      .build();
   @RegistryName("ant")
   EntityType<AntEntity> ANT = EntitiesST.createAnimated(
         AntEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.ANT_IDLE)
            .with(MovementActionType.WALKING, AnimationsST8.ANT_WALK)
            .with(MovementActionType.FAST_WALKING, () -> List.of(AnimationsST8.ANT_WALK.configure().speed(1.5F)))
            .with(MovementActionType.RUNNING, AnimationsST8.ANT_RUN)
      )
      .model(EntityModelsST.ANT)
      .texture(St.tx("entity/ant"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.AnimatedEntityLikeHumanoidRenderer)
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .sized(0.35F, 0.4F)
      .build();
   @RegistryName("spider_head_lololoshka")
   EntityType<AnimatedEntity> SPIDER_HEAD_LOLOLOSHKA = EntitiesST.createAnimated(
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.SPIDER_HEAD_IDLE)
            .with(MovementActionType.WALKING, AnimationsST8.SPIDER_HEAD_WALK)
            .with(MovementActionType.FAST_WALKING, () -> List.of(AnimationsST8.SPIDER_HEAD_WALK.configure().speed(1.5F)))
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.SPIDER_HEAD_LOLOLOSHKA)
      .texture(St.tx("entity/spider_head_lololoshka"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .sized(0.8F, 0.8F)
      .build();
   @RegistryName("spider_head_lololoshka_big")
   EntityType<AnimatedEntity> SPIDER_HEAD_LOLOLOSHKA_BIG = EntitiesST.createAnimated(
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.SPIDER_HEAD_IDLE)
            .with(MovementActionType.WALKING, AnimationsST8.SPIDER_HEAD_WALK)
            .with(MovementActionType.FAST_WALKING, () -> List.of(AnimationsST8.SPIDER_HEAD_WALK.configure().speed(1.5F)))
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.SPIDER_HEAD_LOLOLOSHKA_BIG)
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonWithEmissiveRenderer)
      .texture(St.tx("entity/spider_head_lololoshka"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .sized(2.2F, 2.5F)
      .build();
   @RegistryName("mud_boss")
   EntityType<BaseMob> MUD_BOSS = EntitiesST.createBaseMob(
         BaseMob::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST6.ZOMBIE3_IDLE)
            .with(MovementActionType.RUNNING, () -> List.of(AnimationsST6.ZOMBIE3_WALK.configure().speed(1.2F).loopMode(LoopMode.LOOP)))
            .with(MovementActionType.FAST_WALKING, () -> List.of(AnimationsST6.ZOMBIE3_WALK.configure().speed(1.2F).loopMode(LoopMode.LOOP)))
            .with(MovementActionType.WALKING, AnimationsST6.ZOMBIE3_WALK)
      )
      .sized(0.6F, 1.6F)
      .model(EntityModelsST.MUD_BOSS)
      .texture(St.tx("entity/mud_boss"))
      .build();
   @RegistryName("unit_base_bigger")
   EntityType<HumanoidEntity> UNIT_BASE_BIGGER = EntitiesST.createHumanoid(Sex.STEVE)
      .model(EntityModelsST.UNIT_BASE_BIGGER)
      .texture(St.tx("entity/unit_base"))
      .build();
   @RegistryName("blue_man")
   EntityType<HumanoidEntity> BLUE_MAN = EntitiesST.createHumanoid(Sex.STEVE).model(EntityModelsST.BLUE_MAN).texture(St.tx("entity/blue_man")).build();
   @RegistryName("riddle")
   EntityType<RiddleEntity> RIDDLE = EntitiesST.createAnimated(
         RiddleEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.WALKING, Animations.NULL)
      )
      .texture(St.tx("entity/riddle"))
      .sized(0.1F, 2.0F)
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.RiddleEntityRenderer)
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderer(CommonRenderDefinition::new)
      .build();
   @RegistryName("eidos")
   EntityType<EidosGuiEntity> EIDOS = EntitiesST.createAnimated(
         EidosGuiEntity::new, IDefaultAnimConfiguration.empty().with(MovementActionType.IDLE, AnimationsST8.EIDOS_IDLE)
      )
      .model(EntityModelsST.EIDOS)
      .texture(St.tx("entity/eidos"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.EidosGuiRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .sized(0.5F, 0.5F)
      .build();
   @RegistryName("head_lololoshka")
   EntityType<HumanoidEntity> HEAD_LOLOLOSHKA = EntitiesST.createHumanoid(Sex.STEVE)
      .model(EntityModelsST.HEAD_LOLOLOSHKA)
      .texture(St.tx("entity/head_lololoshka"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .sized(0.45F, 0.6F)
      .build();
   @RegistryName("head_lololoshka_ant")
   EntityType<HeadLololoshkaAntEntity> HEAD_LOLOLOSHKA_ANT = EntitiesST.createAnimated(
         HeadLololoshkaAntEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.HEAD_LOLOLOSHKA_ANT_IDLE)
            .with(MovementActionType.WALKING, AnimationsST8.HEAD_LOLOLOSHKA_ANT_WALK)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.HEAD_LOLOLOSHKA_ANT)
      .texture(St.tx("entity/spider_head_lololoshka"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .sized(0.8F, 1.0F)
      .build();
   @RegistryName("head_lololoshka_ant_tall")
   EntityType<HeadLololoshkaAntEntity> HEAD_LOLOLOSHKA_ANT_TALL = EntitiesST.createAnimated(
         HeadLololoshkaAntEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.HEAD_LOLOLOSHKA_ANT_IDLE)
            .with(MovementActionType.WALKING, AnimationsST8.HEAD_LOLOLOSHKA_ANT_WALK)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.HEAD_LOLOLOSHKA_ANT)
      .texture(St.tx("entity/spider_head_lololoshka"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .sized(0.8F, 2.3F)
      .build();
   @RegistryName("block_green")
   EntityType<AnimatedEntity> BLOCK_GREEN = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.BLOCK)
      .texture(St.tx("block/platform/block_green"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(0.7F, 0.6F)
      .build();
   @RegistryName("block_orange")
   EntityType<AnimatedEntity> BLOCK_ORANGE = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.BLOCK)
      .texture(St.tx("block/platform/block_orange"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(0.7F, 0.6F)
      .build();
   @RegistryName("block_white")
   EntityType<AnimatedEntity> BLOCK_WHITE = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.BLOCK)
      .texture(St.tx("block/platform/block_white"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(0.7F, 0.6F)
      .build();
   @RegistryName("block_yellow")
   EntityType<AnimatedEntity> BLOCK_YELLOW = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.BLOCK)
      .texture(St.tx("block/platform/block_yellow"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(0.7F, 0.6F)
      .build();
   @RegistryName("neon/neon_flower")
   EntityType<AnimatedEntity> NEON_FLOWER = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.NEON_FLOWER)
      .texture(St.tx("entity/neon/neon_flower"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonWithEmissiveRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(1.0F, 1.9F)
      .build();
   @RegistryName("neon/neon_flower_black")
   EntityType<AnimatedEntity> NEON_FLOWER_BLACK = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.NEON_FLOWER)
      .texture(St.tx("entity/neon/neon_flower_black"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonWithEmissiveRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(1.0F, 1.9F)
      .build();
   @RegistryName("neon/neon_flower_other")
   EntityType<AnimatedEntity> NEON_FLOWER_OTHER = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.NEON_FLOWER)
      .texture(St.tx("entity/neon/neon_flower_other"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonWithEmissiveRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(1.0F, 1.9F)
      .build();
   @RegistryName("neon/neon_grass")
   EntityType<AnimatedEntity> NEON_GRASS = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.NEON_GRASS)
      .texture(St.tx("entity/neon/neon_grass"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonWithEmissiveRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(1.5F, 1.9F)
      .build();
   @RegistryName("neon/neon_mushroom")
   EntityType<AnimatedEntity> NEON_MUSHROOM = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.NEON_MUSHROOM)
      .texture(St.tx("entity/neon/neon_mushroom"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonWithEmissiveRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("neon/neon_stone")
   EntityType<AnimatedEntity> NEON_STONE = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.NEON_STONE)
      .texture(St.tx("entity/neon/neon_stone"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonWithEmissiveRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(2.0F, 1.4F)
      .build();
   @RegistryName("ant_pillar")
   EntityType<AntGroupEntity> ANT_PILLAR = EntitiesST.createAnimated(
         AntGroupEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.ANT_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.ANT_PILLAR)
      .texture(St.tx("entity/ant"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .sized(0.5F, 1.6F)
      .build();
   @RegistryName("ant_triangle")
   EntityType<AntGroupEntity> ANT_TRIANGLE = EntitiesST.createAnimated(
         AntGroupEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.ANT_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.ANT_TRIANGLE)
      .texture(St.tx("entity/ant"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .sized(1.1F, 1.7F)
      .build();
   @RegistryName("ant_heart")
   EntityType<AntGroupEntity> ANT_HEART = EntitiesST.createAnimated(
         AntGroupEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.ANT_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.ANT_HEART)
      .texture(St.tx("entity/ant"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .sized(1.0F, 1.4F)
      .build();
   @RegistryName("ant_head")
   EntityType<AntGroupEntity> ANT_HEAD = EntitiesST.createAnimated(
         AntGroupEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.ANT_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.ANT_HEAD)
      .texture(St.tx("entity/ant"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .sized(0.7F, 0.7F)
      .build();
   @RegistryName("ant_flower")
   EntityType<AntGroupEntity> ANT_FLOWER = EntitiesST.createAnimated(
         AntGroupEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.ANT_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.ANT_FLOWER)
      .texture(St.tx("entity/ant"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .sized(0.6F, 1.9F)
      .build();
   @RegistryName("anu")
   EntityType<AnimatedEntity> ANU = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.ANU)
      .texture(St.tx("entity/anu"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .build();
   @RegistryName("eidos_pickable")
   EntityType<EidosEntity> EIDOS_PICKABLE = EntitiesST.createAnimated(
         EidosEntity::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.EIDOS_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.EIDOS)
      .texture(St.tx("entity/eidos"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.EidosRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .sized(0.25F, 0.25F)
      .build();
   @RegistryName("eidos_collectible")
   EntityType<AnimatedEntity> EIDOS_COLLECTIBLE = EntitiesST.createAnimated(
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.EIDOS_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.EIDOS_COLLECTIBLE)
      .texture(St.tx("entity/eidos"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .sized(0.25F, 0.25F)
      .build();
   @RegistryName("flytrap")
   EntityType<BaseMob> FLYTRAP = EntitiesST.createBaseMob(
         BaseMob::new,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.FLYTRAP_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.FLYTRAP_WITHOUT_LEAVES)
      .texture(St.tx("entity/flytrap"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.FlytrapRenderer)
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .build();
   @RegistryName("clown_box")
   EntityType<AnimatedEntity> CLOWN_BOX = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.CLOWN_BOX)
      .texture(St.tx("entity/clown_box"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonRenderer)
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .build();
   @RegistryName("butterfly")
   EntityType<AnimatedEntity> BUTTERFLY = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.BUTTERFLY)
      .texture(St.tx("entity/butterfly/butterfly_1"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonWithEmissiveRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .build();
   @RegistryName("jdh_cloud")
   EntityType<AnimatedEntity> JDH_CLOUD = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.JDH_CLOUD)
      .texture(St.tx("entity/jdh_cloud"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("pink_air_baloon")
   EntityType<AnimatedEntity> PINK_AIR_BALOON = EntitiesST.createAnimated(IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.PINK_AIR_BALOON)
      .texture(St.tx("entity/pink_air_baloon"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("statue_1")
   EntityType<RotateStatueEntity> STATUE_1 = EntitiesST.createAnimated(RotateStatueEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.STATUE_1)
      .texture(St.tx("entity/ant_statue"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("statue_2")
   EntityType<RotateStatueEntity> STATUE_2 = EntitiesST.createAnimated(RotateStatueEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.STATUE_2)
      .texture(St.tx("entity/ant_statue"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("statue_3")
   EntityType<RotateStatueEntity> STATUE_3 = EntitiesST.createAnimated(RotateStatueEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.STATUE_3)
      .texture(St.tx("entity/ant_statue"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("statue_4")
   EntityType<RotateStatueEntity> STATUE_4 = EntitiesST.createAnimated(RotateStatueEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.STATUE_4)
      .texture(St.tx("entity/ant_statue"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("statue_5")
   EntityType<RotateStatueEntity> STATUE_5 = EntitiesST.createAnimated(RotateStatueEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.STATUE_5)
      .texture(St.tx("entity/ant_statue"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("statue_6")
   EntityType<RotateStatueEntity> STATUE_6 = EntitiesST.createAnimated(RotateStatueEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.STATUE_6)
      .texture(St.tx("entity/ant_statue"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("statue_7")
   EntityType<RotateStatueEntity> STATUE_7 = EntitiesST.createAnimated(RotateStatueEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.STATUE_7)
      .texture(St.tx("entity/ant_statue"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("statue_8")
   EntityType<RotateStatueEntity> STATUE_8 = EntitiesST.createAnimated(RotateStatueEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.STATUE_8)
      .texture(St.tx("entity/ant_statue"))
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("cutscene_merge_ant")
   EntityType<HumanoidEntity> CUTSCENE_MERGE_ANT = EntitiesST.createHumanoid(Sex.STEVE, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.CUTSCENE_MERGE_ANT)
      .texture(St.tx("entity/ant"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.HumanoidRenderer)
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.3F)
      .build();
   @RegistryName("glitch_cloud")
   EntityType<GlitchCloudEntity> GLITCH_CLOUD = EntitiesST.createAnimated(GlitchCloudEntity::new, IDefaultAnimConfiguration.noAnims())
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.NullEntityRenderer)
      .renderProp(RenderContext.ENTITY_SHADOW_SCALE, 0.0F)
      .build();
   @RegistryName("girl_head")
   EntityType<HumanoidEntity> GIRL_HEAD = EntitiesST.createHumanoid(
         Sex.ALEX,
         IDefaultAnimConfiguration.empty()
            .with(MovementActionType.IDLE, AnimationsST8.HEAD_LOLOLOSHKA_ANT_IDLE)
            .with(MovementActionType.WALKING, Animations.NULL)
            .with(MovementActionType.FAST_WALKING, Animations.NULL)
            .with(MovementActionType.RUNNING, Animations.NULL)
      )
      .model(EntityModelsST.GIRL_HEAD)
      .texture(St.tx("entity/girl_head"))
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .sized(0.7F, 0.7F)
      .build();
}

package com.storyteam.storytelling.init;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.impl.CommonRenderDefinition;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import com.storyteam.storytelling.content.entities.AnimatedNoGazeHumanoid;
import com.storyteam.storytelling.content.entities.ChairEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.content.entities.STCameraEntity;
import com.storyteam.storytelling.content.entities.base.EntityRegistrar;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.content.entities.configurations.Sex;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import com.storyteam.storytelling.init.seasonal.EntitiesST8;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface EntitiesST extends EntitiesST8 {
   @RegistryName("chair")
   EntityType<ChairEntity> CHAIR = createGeneric(ChairEntity::new, MobCategory.MISC).sized(0.0F, 0.0F).fireImmune().noSummon().build();
   @RegistryName("zeitheron")
   EntityType<HumanoidEntity> ZEITHERON = createHumanoid(Sex.STEVE)
      .model(EntityModelsST.ZEITHERON_SERGAL)
      .texture(St.tx("entity/st_team/zeitheron_sergal"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.HumanoidWithEmissiveRenderer)
      .renderer(CommonRenderDefinition::new)
      .build();
   @RegistryName("archmage_sword")
   EntityType<HumanoidEntity> ARCHMAGE = createHumanoid(Sex.STEVE)
      .model(EntityModelsST.CITY_STEVE)
      .texture(St.tx("entity/st_team/archmage"))
      .renderer(CommonRenderDefinition::new)
      .build();
   @RegistryName("edit")
   EntityType<HumanoidEntity> EDIT = createHumanoid(Sex.STEVE)
      .model(EntityModelsST.EDIT)
      .texture(St.tx("entity/st_team/st_edit"))
      .renderer(CommonRenderDefinition::new)
      .build();
   @RegistryName("st_camera_entity")
   EntityType<STCameraEntity> ST_CAMERA_ENTITY = createGeneric(STCameraEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).fireImmune().build();

   static <T extends AnimatedEntity> EntityRegistrar<T> create(EntityFactory<T> pFactory, MobCategory pCategory, IDefaultAnimConfiguration animConfiguration) {
      return EntityRegistrar.createAnimated(EntitiesST.Events.$, pFactory, pCategory, animConfiguration);
   }

   static <T extends Entity> EntityRegistrar<T> createGeneric(EntityFactory<T> pFactory, MobCategory pCategory) {
      return EntityRegistrar.createGeneric(EntitiesST.Events.$, pFactory, pCategory);
   }

   static EntityRegistrar<AnimatedEntity> createAnimated(@Nullable IDefaultAnimConfiguration animConfiguration) {
      return create((type, level) -> AnimatedEntity.create(type, level, animConfiguration), MobCategory.CREATURE, animConfiguration)
         .sized(0.5F, 1.8F)
         .renderer(CommonRenderDefinition::new)
         .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonRenderer);
   }

   static <T extends AnimatedEntity> EntityRegistrar<T> createAnimated(EntityFactory<T> animatedFactory, @Nullable IDefaultAnimConfiguration animConfiguration) {
      return create(animatedFactory, MobCategory.CREATURE, animConfiguration)
         .sized(0.5F, 1.8F)
         .renderer(CommonRenderDefinition::new)
         .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.CommonRenderer);
   }

   static <T extends BaseMob> EntityRegistrar<T> createBaseMob(EntityFactory<T> animatedFactory, @Nullable IDefaultAnimConfiguration animConfiguration) {
      return create(animatedFactory, MobCategory.CREATURE, animConfiguration)
         .sized(0.5F, 1.8F)
         .renderer(CommonRenderDefinition::new)
         .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.AnimatedEntityLikeHumanoidRenderer);
   }

   static <T extends AnimatedEntityLikeHumanoid> EntityRegistrar<T> createAnimatedLikeHumanoid(
      EntityFactory<T> animatedFactory, @Nullable IDefaultAnimConfiguration animConfiguration
   ) {
      return create(animatedFactory, MobCategory.CREATURE, animConfiguration)
         .sized(0.5F, 1.8F)
         .renderer(CommonRenderDefinition::new)
         .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.AnimatedEntityLikeHumanoidRenderer);
   }

   static <T extends AnimatedNoGazeHumanoid> EntityRegistrar<T> createAnimatedNoGazeHumanoid(
      EntityFactory<T> animatedFactory, @Nullable IDefaultAnimConfiguration animConfiguration
   ) {
      return create(animatedFactory, MobCategory.CREATURE, animConfiguration)
         .sized(0.5F, 1.8F)
         .renderer(CommonRenderDefinition::new)
         .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.AnimatedNoGazeHumanoidRenderer);
   }

   static EntityRegistrar<HumanoidEntity> createHumanoid(Sex sex) {
      return EntitiesST.<HumanoidEntity>create((type, level) -> HumanoidEntity.createHumanoid(type, level, sex), MobCategory.CREATURE, sex)
         .sized(0.5F, 1.8F)
         .model(sex.getModel())
         .renderer(CommonRenderDefinition::new)
         .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.HumanoidRenderer);
   }

   static EntityRegistrar<HumanoidEntity> createHumanoid(Sex sex, IDefaultAnimConfiguration animConfiguration) {
      return EntitiesST.<HumanoidEntity>create((type, level) -> HumanoidEntity.createHumanoid(type, level, animConfiguration), MobCategory.CREATURE, sex)
         .sized(0.5F, 1.8F)
         .model(sex.getModel())
         .renderer(CommonRenderDefinition::new)
         .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.HumanoidRenderer);
   }

   @EventBusSubscriber(
      bus = Bus.MOD,
      value = {Dist.CLIENT}
   )
   public static class ClientEvents {
      @SubscribeEvent
      public static void entityRenderers(RegisterRenderers event) {
         EntitiesST.Events.$.register(event);
      }
   }

   @EventBusSubscriber(
      bus = Bus.MOD
   )
   public static class Events {
      public static final EntityRegistrar.EntityRegisrarHolder $ = new EntityRegistrar.EntityRegisrarHolder();

      @SubscribeEvent
      public static void createAttributes(EntityAttributeCreationEvent event) {
         $.attributes(event);
      }
   }
}

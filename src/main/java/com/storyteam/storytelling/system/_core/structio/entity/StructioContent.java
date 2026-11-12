package com.storyteam.storytelling.system._core.structio.entity;

import com.storyteam.storytelling.content.entities.data.EntityDataSerializerHL;
import com.storyteam.storytelling.system._core.structio.entity.client.RenderStructureEntity;
import com.storyteam.storytelling.system._core.structio.entity.data.StructureRiderInfo;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/structio/"
)
public class StructioContent {
   @RegistryName("structure_rider_info")
   public static final EntityDataSerializerHL<StructureRiderInfo> STRUCTURE_RIDER_INFO = EntityDataSerializerHL.simpleCopyable(
      StructureRiderInfo::write, StructureRiderInfo::read, StructureRiderInfo::copy
   );
   @RegistryName("structure")
   public static final EntityType<StructureEntity> STRUCTURE_ENTITY_ENTITY_TYPE = Builder.of(StructureEntity::new, MobCategory.MISC).build("structure");

   public static class ClientEvents {
      @SubscribeEvent
      public static void entityRenderers(RegisterRenderers event) {
         event.registerEntityRenderer(StructioContent.STRUCTURE_ENTITY_ENTITY_TYPE, RenderStructureEntity::new);
      }
   }

   public static class Events {
      @SubscribeEvent
      public static void createAttributes(EntityAttributeCreationEvent event) {
         event.put(StructioContent.STRUCTURE_ENTITY_ENTITY_TYPE, Mob.createMobAttributes().build());
      }
   }
}

package com.storyteam.storytelling.script.story.npc;

import com.storyteam.storytelling.Storytelling;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Storytelling.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ScriptNpcs {
   private ScriptNpcs() { }

   public static final DeferredRegister<EntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Storytelling.MODID);
   public static final RegistryObject<EntityType<ScriptNpcEntity>> SCRIPT_NPC = TYPES.register("script_npc",
      () -> EntityType.Builder.of(ScriptNpcEntity::new, MobCategory.CREATURE)
         .sized(0.6f, 1.8f).clientTrackingRange(8).build("storytelling:script_npc"));

   @SubscribeEvent
   public static void attributes(EntityAttributeCreationEvent event) {
      event.put(SCRIPT_NPC.get(), Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).build());
   }

   @Mod.EventBusSubscriber(modid = Storytelling.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
   public static final class ClientEvents {
      @SubscribeEvent
      public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
         event.registerEntityRenderer(SCRIPT_NPC.get(), ScriptNpcRenderer::new);
      }
   }
}

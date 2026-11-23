package com.storyteam.storytelling.events.listeners;

import com.storyteam.storytelling.Storytelling;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;

/** Remaps existing world entries only when the mod adopts a new registry namespace. */
@EventBusSubscriber
public final class BlockRemapper {
   private static final String LEGACY_NAMESPACE = "storytelling";

   private BlockRemapper() { }

   @SubscribeEvent
   public static void handleEvent(MissingMappingsEvent event) {
      if (LEGACY_NAMESPACE.equals(Storytelling.MODID)) return;

      remap(event, ForgeRegistries.BLOCKS);
      remap(event, ForgeRegistries.ITEMS);
      remap(event, ForgeRegistries.ENTITY_TYPES);
      remap(event, ForgeRegistries.BLOCK_ENTITY_TYPES);
      remap(event, ForgeRegistries.FLUIDS);
      remap(event, ForgeRegistries.SOUND_EVENTS);
      remap(event, ForgeRegistries.PARTICLE_TYPES);
   }

   private static <T> void remap(MissingMappingsEvent event, IForgeRegistry<T> registry) {
      for (MissingMappingsEvent.Mapping<T> mapping : event.getMappings(registry.getRegistryKey(), LEGACY_NAMESPACE)) {
         ResourceLocation targetId = new ResourceLocation(Storytelling.MODID, mapping.getKey().getPath());
         if (!registry.containsKey(targetId)) {
            Storytelling.LOG.warn("No replacement registered for old entry {} in {}", mapping.getKey(), registry.getRegistryName());
            continue;
         }
         T target = registry.getValue(targetId);
         if (target != null) {
            mapping.remap(target);
            Storytelling.LOG.info("Remapped {} to {}", mapping.getKey(), targetId);
         }
      }
   }
}

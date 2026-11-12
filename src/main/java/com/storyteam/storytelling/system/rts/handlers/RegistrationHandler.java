package com.storyteam.storytelling.system.rts.handlers;

import com.solegendary.reignofnether.api.ReignOfNetherRegistries;
import com.solegendary.reignofnether.faction.FactionConfiguration;
import com.solegendary.reignofnether.faction.FactionRegistrar;
import com.solegendary.reignofnether.registrars.EntityRegistrar;
import com.solegendary.reignofnether.resources.ResourceName;
import com.solegendary.reignofnether.resources.ResourceSource;
import com.solegendary.reignofnether.resources.ResourceSources;
import com.solegendary.reignofnether.resources.ResourcesServerEvents;
import com.solegendary.reignofnether.util.Faction;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.init.BlocksST;
import com.storyteam.storytelling.system.rts.buildings.api.RTSBuildingConfig;
import com.storyteam.storytelling.system.rts.init.RTSUnitsST;
import com.storyteam.storytelling.system.rts.storage.Storages;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class RegistrationHandler {
   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void onEntityRegistration(RegisterEvent event) {
      if (event.getRegistryKey().equals(ForgeRegistries.ENTITY_TYPES.getRegistryKey())) {
         EntityRegistrar.registerDefaultEntity(Faction.STORYTELLING, RTSUnitsST.ANT::getEntity);
      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void onFactionRegistration(RegisterEvent event) {
      if (event.getRegistryKey().equals(ForgeRegistries.ITEMS.getRegistryKey())) {
         ResourceSources.RICE_BLOCKS
            .add(new ResourceSource("Rice", List.of(BlocksST.WILD_RICE), List.of(BlocksST.WILD_RICE.asItem()), 40, 4, ResourceName.RICE));
         ResourceSources.ETHER_BLOCKS
            .addAll(
               List.of(
                  new ResourceSource(
                     "Ether",
                     List.of(BlocksST.ETHER_LOG, BlocksST.ETHER_LOG_BLUE, BlocksST.FALLING_ETHER_LOG, BlocksST.FALLING_ETHER_LOG_BLUE),
                     List.of(
                        BlocksST.ETHER_LOG.asItem(),
                        BlocksST.ETHER_LOG_BLUE.asItem(),
                        BlocksST.FALLING_ETHER_LOG.asItem(),
                        BlocksST.FALLING_ETHER_LOG_BLUE.asItem()
                     ),
                     240,
                     3,
                     ResourceName.ETHER
                  ),
                  new ResourceSource(
                     "EtherLeaves",
                     List.of(BlocksST.ETHER_LEAVES, BlocksST.ETHER_LEAVES_BLUE),
                     List.of(BlocksST.ETHER_LEAVES.asItem(), BlocksST.ETHER_LEAVES_BLUE.asItem()),
                     10,
                     1,
                     ResourceName.ETHER
                  )
               )
            );
         ResourceSources.VERSIFORM_BLOCKS
            .add(
               new ResourceSource(
                  "Versiform",
                  List.of(BlocksST.VERSIFORM_ORE, BlocksST.STONE_VERSIFORM_ORE),
                  List.of(BlocksST.VERSIFORM_ORE.asItem(), BlocksST.STONE_VERSIFORM_ORE.asItem()),
                  900,
                  10,
                  ResourceName.VERSIFORM
               )
            );
         ResourcesServerEvents.FALLING_LOGS.put(BlocksST.ETHER_LOG, BlocksST.FALLING_ETHER_LOG);
         ResourcesServerEvents.FALLING_LOGS.put(BlocksST.ETHER_LOG_BLUE, BlocksST.FALLING_ETHER_LOG_BLUE);
         Storages.<RTSBuildingConfig, ResourceLocation>require(RTSBuildingConfig.class)
            .all()
            .forEach(b -> Registry.register(ReignOfNetherRegistries.BUILDING, b.getId(), b.asBuilding()));
         Storages.<RTSBuildingConfig, ResourceLocation>require(RTSBuildingConfig.class)
            .all()
            .stream()
            .flatMap(b -> b.getLevelConfigs().stream())
            .forEach(l -> Registry.register(ReignOfNetherRegistries.PRODUCTION_ITEM, l.getStructure(), l));
         FactionRegistrar.register(
            Faction.STORYTELLING,
            (FactionConfiguration)Registry.register(
               ReignOfNetherRegistries.FACTION,
               St.id(Faction.STORYTELLING.toString().toLowerCase()),
               new FactionConfiguration(
                  () -> Storages.<RTSBuildingConfig, ResourceLocation>require(RTSBuildingConfig.class)
                        .all()
                        .stream()
                        .map(c -> c.asBuilding().getBuildButton())
                        .toList(),
                  Collections::emptyList
               )
            )
         );
      }
   }
}

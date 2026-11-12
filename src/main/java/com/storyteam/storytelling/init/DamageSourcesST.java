package com.storyteam.storytelling.init;

import com.storyteam.storytelling.St;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "storytelling",
   bus = Bus.FORGE
)
public class DamageSourcesST {
   public static final ResourceKey<DamageType> UNSCALED_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, St.id("unscaled"));
   public static DamageSource UNSCALED;

   @SubscribeEvent
   public static void initiateDamageSources(ServerStartedEvent event) {
      initializeDamageSources(event.getServer().registryAccess());
   }

   public static void initializeDamageSources(RegistryAccess access) {
      Registry<DamageType> types = access.registryOrThrow(Registries.DAMAGE_TYPE);
      UNSCALED = new DamageSource(types.getHolderOrThrow(UNSCALED_TYPE));
   }
}

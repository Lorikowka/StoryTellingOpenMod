package com.storyteam.storytelling.system.health;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class HealthEvents {
   @SubscribeEvent
   public static void healthUpdate(PlayerTickEvent event) {
      AttributeInstance instance = event.player.getAttribute(Attributes.MAX_HEALTH);
      if (instance != null) {
         int maxHealth = HealthSystem.INSTANCE.getMaxHealth(event.player);
         if (instance.getBaseValue() != (double)maxHealth) {
            instance.setBaseValue((double)maxHealth);
         }
      }
   }
}

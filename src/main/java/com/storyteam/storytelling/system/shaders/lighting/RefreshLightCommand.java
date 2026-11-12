package com.storyteam.storytelling.system.shaders.lighting;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RefreshLightCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create() {
      return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("refresh_light").executes(cs -> {
         LightSystem.refreshLighting();
         return 1;
      });
   }
}

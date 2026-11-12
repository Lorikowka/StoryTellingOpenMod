package com.storyteam.storytelling.system.particle;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ParticlesCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create() {
      return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("particles").then(Commands.literal("refresh").executes(cs -> {
         OptimizedParticleRenderer.refreshParticleRenderer = true;
         return 1;
      }));
   }
}

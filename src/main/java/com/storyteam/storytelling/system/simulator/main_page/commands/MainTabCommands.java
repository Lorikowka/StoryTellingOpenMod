package com.storyteam.storytelling.system.simulator.main_page.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.simulator.main_page.MainTabStorage;
import com.storyteam.storytelling.system.simulator.main_page.arguments.MainTabTypeArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MainTabCommands {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return Commands.literal("main_tab")
         .then(
            Commands.literal("setBlocking")
               .then(Commands.argument("type", MainTabTypeArgument.type()).then(Commands.argument("state", BoolArgumentType.bool()).executes(ctx -> {
                  MainTabStorage.setBlocked(MainTabTypeArgument.getType(ctx, "type"), BoolArgumentType.getBool(ctx, "state"));
                  return 1;
               })))
         );
   }
}

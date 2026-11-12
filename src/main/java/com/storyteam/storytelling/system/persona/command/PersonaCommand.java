package com.storyteam.storytelling.system.persona.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.content.commands.ScriptCommand;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import java.util.Arrays;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PersonaCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return ((LiteralArgumentBuilder)Commands.literal("persona")
            .then(Commands.literal("set").then(Commands.argument("id", StringArgumentType.string()).suggests((context, builder) -> {
               for (String fursona : Arrays.stream(Persona.values()).map(Enum::name).toList()) {
                  builder.suggest(fursona);
               }

               return builder.buildFuture();
            }).executes(cs -> {
               ServerPlayer sp = ((CommandSourceStack)cs.getSource()).getPlayer();
               if (sp == null) {
                  return 1;
               } else {
                  String fursona = (String)cs.getArgument("id", String.class);
                  PersonaHandler.setPersona(sp, Persona.valueOf(fursona));
                  return 1;
               }
            }))))
         .then(Commands.literal("current").executes(cs -> {
            ScriptCommand.sendSuccess(cs, Component.literal("Текущий персонаж: " + PersonaHandler.getCurrentPersona().name()), true);
            return 1;
         }));
   }
}

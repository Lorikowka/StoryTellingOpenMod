package com.storyteam.storytelling.content.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.events.listeners.PathRecorderHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.util.java.Cast;

public class STPathCommand {
   public static void register(ICommand.ICommandRegistry dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("stpath")
               .then(
                  Commands.literal("rec")
                     .then(
                        Commands.argument("id", StringArgumentType.string())
                           .executes(
                              cs -> {
                                 PathRecorderHandler.record(StringArgumentType.getString(cs, "id"));
                                 ((CommandSourceStack)cs.getSource())
                                    .sendSuccess(Cast.constant(Component.literal("Запись пути " + StringArgumentType.getString(cs, "id") + " начата")), true);
                                 return 1;
                              }
                           )
                     )
               ))
            .then(Commands.literal("stop").executes(cs -> {
               PathRecorderHandler.finish();
               ((CommandSourceStack)cs.getSource()).sendSuccess(Cast.constant(Component.literal("Запись пути остановлена")), true);
               return 1;
            }))
      );
   }
}

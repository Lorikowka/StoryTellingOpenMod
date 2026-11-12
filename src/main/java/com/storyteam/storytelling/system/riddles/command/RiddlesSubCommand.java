package com.storyteam.storytelling.system.riddles.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.riddles.data.RiddlesDef;
import com.storyteam.storytelling.system.riddles.storage.RiddlesStorage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.util.java.Cast;

public class RiddlesSubCommand {
   private static final SuggestionProvider<CommandSourceStack> PERSONA_SUGGESTIONS = (context, builder) -> {
      for (Persona persona : Persona.values()) {
         builder.suggest(persona.name());
      }

      return builder.buildFuture();
   };
   private static final SuggestionProvider<CommandSourceStack> RIDDLE_ID_SUGGESTIONS = (context, builder) -> {
      for (String id : RiddlesStorage.getAllIds()) {
         builder.suggest(id);
      }

      return builder.buildFuture();
   };
   private static final SuggestionProvider<CommandSourceStack> BOOL_SUGGESTIONS = (context, builder) -> {
      builder.suggest("true");
      builder.suggest("false");
      return builder.buildFuture();
   };

   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return ((LiteralArgumentBuilder)Commands.literal("riddle")
            .then(
               ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("set").then(Commands.literal("solved").then(setSolved())))
                     .then(Commands.literal("canRead").then(setCanRead())))
                  .then(Commands.literal("visible").then(setVisible()))
            ))
         .then(
            ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("get").then(Commands.literal("solved").then(getSolved())))
                  .then(Commands.literal("canRead").then(getCanRead())))
               .then(Commands.literal("visible").then(getVisible()))
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setSolved() {
      return Commands.argument("persona", StringArgumentType.string())
         .suggests(PERSONA_SUGGESTIONS)
         .then(
            Commands.argument("riddle_id", ResourceLocationArgument.id())
               .suggests(RIDDLE_ID_SUGGESTIONS)
               .then(Commands.argument("value", BoolArgumentType.bool()).suggests(BOOL_SUGGESTIONS).executes(context -> {
                  CommandSourceStack source = (CommandSourceStack)context.getSource();
                  String personaName = StringArgumentType.getString(context, "persona");
                  String riddleId = ResourceLocationArgument.getId(context, "riddle_id").toString();
                  boolean value = BoolArgumentType.getBool(context, "value");

                  try {
                     Persona persona = Persona.valueOf(personaName.toUpperCase());
                     RiddlesDef riddle = RiddlesStorage.get(riddleId);
                     if (riddle == null) {
                        source.sendFailure(Component.literal("Загадка с ID '%s' не найдена".formatted(riddleId)));
                        return 0;
                     } else {
                        RiddlesStorage.setSolvedForPersona(persona, riddleId, value);
                        String status = value ? "разгадана" : "сброшена";
                        source.sendSuccess(
                           Cast.constant(Component.literal("Загадка '%s' для персоны %s теперь %s".formatted(riddleId, personaName, status))), true
                        );
                        return 1;
                     }
                  } catch (IllegalArgumentException var8) {
                     source.sendFailure(Component.literal("Персона %s не найдена".formatted(personaName)));
                     return 0;
                  }
               }))
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> getSolved() {
      return Commands.argument("persona", StringArgumentType.string())
         .suggests(PERSONA_SUGGESTIONS)
         .then(Commands.argument("riddle_id", ResourceLocationArgument.id()).suggests(RIDDLE_ID_SUGGESTIONS).executes(context -> {
            CommandSourceStack source = (CommandSourceStack)context.getSource();
            String personaName = StringArgumentType.getString(context, "persona");
            String riddleId = ResourceLocationArgument.getId(context, "riddle_id").toString();

            try {
               Persona persona = Persona.valueOf(personaName.toUpperCase());
               RiddlesDef riddle = RiddlesStorage.get(riddleId);
               if (riddle == null) {
                  source.sendFailure(Component.literal("Загадка с ID '%s' не найдена".formatted(riddleId)));
                  return 0;
               } else {
                  boolean solved = RiddlesStorage.isSolvedByPersona(persona, riddleId);
                  String status = solved ? "разгадана" : "не разгадана";
                  source.sendSuccess(Cast.constant(Component.literal("Загадка '%s' для персоны %s - %s".formatted(riddleId, personaName, status))), false);
                  return 1;
               }
            } catch (IllegalArgumentException var8) {
               source.sendFailure(Component.literal("Персона '%s' не найдена".formatted(personaName)));
               return 0;
            }
         }));
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setCanRead() {
      return Commands.argument("persona", StringArgumentType.string())
         .suggests(PERSONA_SUGGESTIONS)
         .then(Commands.argument("value", BoolArgumentType.bool()).suggests(BOOL_SUGGESTIONS).executes(context -> {
            CommandSourceStack source = (CommandSourceStack)context.getSource();
            String personaName = StringArgumentType.getString(context, "persona");
            boolean value = BoolArgumentType.getBool(context, "value");

            try {
               Persona persona = Persona.valueOf(personaName.toUpperCase());
               RiddlesStorage.setCanReadForPersona(persona, value);
               String status = value ? "может читать" : "не может читать";
               source.sendSuccess(Cast.constant(Component.literal("Персона %s теперь %s".formatted(personaName, status))), true);
               return 1;
            } catch (IllegalArgumentException var6) {
               source.sendFailure(Component.literal("Персона %s не найдена".formatted(personaName)));
               return 0;
            }
         }));
   }

   private static ArgumentBuilder<CommandSourceStack, ?> getCanRead() {
      return Commands.argument("persona", StringArgumentType.string()).suggests(PERSONA_SUGGESTIONS).executes(context -> {
         CommandSourceStack source = (CommandSourceStack)context.getSource();
         String personaName = StringArgumentType.getString(context, "persona");

         try {
            Persona persona = Persona.valueOf(personaName.toUpperCase());
            boolean canRead = RiddlesStorage.canReadByPersona(persona);
            String status = canRead ? "может читать" : "не может читать";
            source.sendSuccess(Cast.constant(Component.literal("Персона %s %s".formatted(personaName, status))), false);
            return 1;
         } catch (IllegalArgumentException var6) {
            source.sendFailure(Component.literal("Персона %s не найдена".formatted(personaName)));
            return 0;
         }
      });
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setVisible() {
      return Commands.argument("riddle_id", ResourceLocationArgument.id())
         .suggests(RIDDLE_ID_SUGGESTIONS)
         .then(Commands.argument("value", BoolArgumentType.bool()).suggests(BOOL_SUGGESTIONS).executes(context -> {
            CommandSourceStack source = (CommandSourceStack)context.getSource();
            String riddleId = ResourceLocationArgument.getId(context, "riddle_id").toString();
            boolean value = BoolArgumentType.getBool(context, "value");

            try {
               RiddlesDef riddle = RiddlesStorage.get(riddleId);
               if (riddle == null) {
                  source.sendFailure(Component.literal("Загадка с ID '%s' не найдена".formatted(riddleId)));
                  return 0;
               } else {
                  riddle.setVisible(value);
                  String visible = value ? "видима" : "не видима";
                  source.sendSuccess(Cast.constant(Component.literal("Загадка '%s' теперь %s".formatted(riddleId, visible))), true);
                  return 1;
               }
            } catch (IllegalArgumentException var6) {
               source.sendFailure(Component.literal("Ошибка команды"));
               return 0;
            }
         }));
   }

   private static ArgumentBuilder<CommandSourceStack, ?> getVisible() {
      return Commands.argument("riddle_id", ResourceLocationArgument.id()).suggests(RIDDLE_ID_SUGGESTIONS).executes(context -> {
         CommandSourceStack source = (CommandSourceStack)context.getSource();
         String riddleId = ResourceLocationArgument.getId(context, "riddle_id").toString();

         try {
            RiddlesDef riddle = RiddlesStorage.get(riddleId);
            if (riddle == null) {
               source.sendFailure(Component.literal("Загадка с ID '%s' не найдена".formatted(riddleId)));
               return 0;
            } else {
               boolean value = riddle.isVisible();
               String visible = value ? "видима" : "не видима";
               source.sendSuccess(Cast.constant(Component.literal("Загадка '%s' - %s".formatted(riddleId, visible))), false);
               return 1;
            }
         } catch (IllegalArgumentException var6) {
            source.sendFailure(Component.literal("Ошибка команды"));
            return 0;
         }
      });
   }
}

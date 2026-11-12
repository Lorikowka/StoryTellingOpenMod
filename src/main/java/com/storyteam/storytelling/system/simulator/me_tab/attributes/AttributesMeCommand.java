package com.storyteam.storytelling.system.simulator.me_tab.attributes;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeData;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.util.java.Cast;

public class AttributesMeCommand {
   private static final SuggestionProvider<CommandSourceStack> ATTRIBUTE_SUGGESTIONS = (context, builder) -> {
      for (AttributeType type : AttributeType.values()) {
         builder.suggest(type.name());
      }

      return builder.buildFuture();
   };

   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return ((LiteralArgumentBuilder)Commands.literal("me").then(level())).then(attribute());
   }

   private static ArgumentBuilder<CommandSourceStack, ?> level() {
      return ((LiteralArgumentBuilder)Commands.literal("level")
            .then(Commands.literal("set").then(Commands.argument("level", IntegerArgumentType.integer()).executes(context -> {
               int level = IntegerArgumentType.getInteger(context, "level");
               PlayerStats.setLevel(level);
               return 1;
            }))))
         .then(Commands.literal("get").executes(context -> {
            CommandSourceStack source = (CommandSourceStack)context.getSource();
            int level = PlayerStats.getLevel();
            source.sendSuccess(Cast.constant(Component.literal("§eТекущий уровень игрока: " + level)), false);
            return 1;
         }));
   }

   public static ArgumentBuilder<CommandSourceStack, ?> attribute() {
      return ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("attribute")
                  .then(
                     Commands.literal("get")
                        .then(
                           Commands.argument("type", StringArgumentType.word())
                              .suggests(ATTRIBUTE_SUGGESTIONS)
                              .executes(
                                 context -> {
                                    String typeName = StringArgumentType.getString(context, "type");
                                    CommandSourceStack source = (CommandSourceStack)context.getSource();

                                    try {
                                       AttributeType type = AttributeType.valueOf(typeName.toUpperCase());
                                       AttributeData data = PlayerStats.getAttribute(type);
                                       source.sendSuccess(
                                          Cast.constant(
                                             Component.literal(type.getDisplayName() + ": value = " + data.getValue() + ", parts = " + data.getParts())
                                          ),
                                          false
                                       );
                                    } catch (IllegalArgumentException var5) {
                                       source.sendFailure(Component.literal("Такого атрибута нет: " + typeName));
                                    }

                                    return 1;
                                 }
                              )
                        )
                  ))
               .then(
                  Commands.literal("set")
                     .then(
                        Commands.argument("type", StringArgumentType.word())
                           .suggests(ATTRIBUTE_SUGGESTIONS)
                           .then(
                              Commands.argument("value", IntegerArgumentType.integer(0))
                                 .then(
                                    Commands.argument("parts", IntegerArgumentType.integer(0, 3))
                                       .executes(
                                          context -> {
                                             String typeName = StringArgumentType.getString(context, "type");
                                             int value = IntegerArgumentType.getInteger(context, "value");
                                             int parts = IntegerArgumentType.getInteger(context, "parts");
                                             CommandSourceStack source = (CommandSourceStack)context.getSource();

                                             try {
                                                AttributeType type = AttributeType.valueOf(typeName.toUpperCase());
                                                PlayerStats.getAttribute(type).set(value, parts);
                                                source.sendSuccess(
                                                   Cast.constant(
                                                      Component.literal(
                                                         "Атрибут " + type.getDisplayName() + " установлен: value = " + value + ", parts = " + parts
                                                      )
                                                   ),
                                                   true
                                                );
                                             } catch (IllegalArgumentException var6) {
                                                source.sendFailure(Component.literal("Такого атрибута нет: " + typeName));
                                             }

                                             return 1;
                                          }
                                       )
                                 )
                           )
                     )
               ))
            .then(
               Commands.literal("addvalue")
                  .then(
                     Commands.argument("type", StringArgumentType.word())
                        .suggests(ATTRIBUTE_SUGGESTIONS)
                        .then(
                           Commands.argument("amount", IntegerArgumentType.integer())
                              .executes(
                                 context -> {
                                    String typeName = StringArgumentType.getString(context, "type");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    CommandSourceStack source = (CommandSourceStack)context.getSource();

                                    try {
                                       AttributeType type = AttributeType.valueOf(typeName.toUpperCase());
                                       PlayerStats.getAttribute(type).addValue(amount);
                                       source.sendSuccess(
                                          Cast.constant(
                                             Component.literal(
                                                "Атрибут "
                                                   + type.getDisplayName()
                                                   + " изменён на "
                                                   + amount
                                                   + ", новое значение: "
                                                   + PlayerStats.getAttribute(type).getValue()
                                             )
                                          ),
                                          true
                                       );
                                    } catch (IllegalArgumentException var5) {
                                       source.sendFailure(Component.literal("Такого атрибута нет: " + typeName));
                                    }

                                    return 1;
                                 }
                              )
                        )
                  )
            ))
         .then(
            Commands.literal("addparts")
               .then(
                  Commands.argument("type", StringArgumentType.word())
                     .suggests(ATTRIBUTE_SUGGESTIONS)
                     .then(
                        Commands.argument("amount", IntegerArgumentType.integer())
                           .executes(
                              context -> {
                                 String typeName = StringArgumentType.getString(context, "type");
                                 int amount = IntegerArgumentType.getInteger(context, "amount");
                                 CommandSourceStack source = (CommandSourceStack)context.getSource();

                                 try {
                                    AttributeType type = AttributeType.valueOf(typeName.toUpperCase());
                                    PlayerStats.getAttribute(type).addParts(amount);
                                    AttributeData data = PlayerStats.getAttribute(type);
                                    source.sendSuccess(
                                       Cast.constant(
                                          Component.literal(
                                             "Атрибут "
                                                + type.getDisplayName()
                                                + " добавлено частей: "
                                                + amount
                                                + ", value = "
                                                + data.getValue()
                                                + ", parts = "
                                                + data.getParts()
                                          )
                                       ),
                                       true
                                    );
                                 } catch (IllegalArgumentException var6) {
                                    source.sendFailure(Component.literal("Такого атрибута нет: " + typeName));
                                 }

                                 return 1;
                              }
                           )
                     )
               )
         );
   }
}

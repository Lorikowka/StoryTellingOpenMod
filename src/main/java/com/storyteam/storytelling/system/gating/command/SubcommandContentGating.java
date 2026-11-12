package com.storyteam.storytelling.system.gating.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.gating.LockedEntry;
import java.util.Objects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

public class SubcommandContentGating {
   @SubscribeEvent
   public static void subcmd(GatherSTSubcommandsEvent e) {
      e.register(
         ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("content-gating")
                  .then(
                     Commands.literal("status")
                        .then(
                           entryArg()
                              .executes(
                                 cs -> {
                                    ResourceLocation rl = ResourceLocationArgument.getId(cs, "entry");
                                    LockedEntry entry = LockedEntry.BY_ID.get(rl);
                                    if (entry == null) {
                                       ((CommandSourceStack)cs.getSource())
                                          .sendFailure(Component.literal("Запись " + JSONObject.quote(Objects.toString(rl)) + " не найдена."));
                                       return 0;
                                    } else {
                                       ((CommandSourceStack)cs.getSource())
                                          .sendSuccess(
                                             Cast.constant(
                                                Component.literal(
                                                   "Статус записи "
                                                      + JSONObject.quote(Objects.toString(rl))
                                                      + ": "
                                                      + (entry.isUnlocked() ? "раз" : "за")
                                                      + "блокирована."
                                                )
                                             ),
                                             true
                                          );
                                       return 1;
                                    }
                                 }
                              )
                        )
                  ))
               .then(
                  Commands.literal("unlock")
                     .then(
                        entryArg()
                           .executes(
                              cs -> {
                                 ResourceLocation rl = ResourceLocationArgument.getId(cs, "entry");
                                 LockedEntry entry = LockedEntry.BY_ID.get(rl);
                                 if (entry == null) {
                                    ((CommandSourceStack)cs.getSource())
                                       .sendFailure(Component.literal("Запись " + JSONObject.quote(Objects.toString(rl)) + " не найдена."));
                                    return 0;
                                 } else if (!entry.setUnlocked(true)) {
                                    ((CommandSourceStack)cs.getSource())
                                       .sendFailure(Component.literal("Запись " + JSONObject.quote(Objects.toString(rl)) + " уже разблокирована."));
                                    return 0;
                                 } else {
                                    ((CommandSourceStack)cs.getSource())
                                       .sendSuccess(
                                          Cast.constant(Component.literal("Запись " + JSONObject.quote(Objects.toString(rl)) + " разблокирована.")), true
                                       );
                                    return 1;
                                 }
                              }
                           )
                     )
               ))
            .then(
               Commands.literal("lock")
                  .then(
                     entryArg()
                        .executes(
                           cs -> {
                              ResourceLocation rl = ResourceLocationArgument.getId(cs, "entry");
                              LockedEntry entry = LockedEntry.BY_ID.get(rl);
                              if (entry == null) {
                                 ((CommandSourceStack)cs.getSource())
                                    .sendFailure(Component.literal("Запись " + JSONObject.quote(Objects.toString(rl)) + " не найдена"));
                                 return 0;
                              } else if (!entry.setUnlocked(false)) {
                                 ((CommandSourceStack)cs.getSource())
                                    .sendFailure(Component.literal("Запись " + JSONObject.quote(Objects.toString(rl)) + " уже заблокирована."));
                                 return 0;
                              } else {
                                 ((CommandSourceStack)cs.getSource())
                                    .sendSuccess(Cast.constant(Component.literal("Запись " + JSONObject.quote(Objects.toString(rl)) + " заблокирована.")), true);
                                 return 1;
                              }
                           }
                        )
                  )
            )
      );
   }

   private static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> entryArg() {
      return Commands.argument("entry", ResourceLocationArgument.id())
         .suggests((cs, b) -> SharedSuggestionProvider.suggestResource(LockedEntry.stream().map(LockedEntry::getId), b));
   }
}

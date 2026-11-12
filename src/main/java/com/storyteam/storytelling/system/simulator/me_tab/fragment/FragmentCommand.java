package com.storyteam.storytelling.system.simulator.me_tab.fragment;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentState;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentStorage;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.util.java.Cast;

public class FragmentCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return Commands.literal("me").then(((LiteralArgumentBuilder)Commands.literal("fragment").then(fragmentBlock(true))).then(fragmentBlock(false)));
   }

   private static ArgumentBuilder<CommandSourceStack, ?> fragmentBlock(boolean active) {
      LiteralArgumentBuilder<CommandSourceStack> literal = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal(active ? "active" : "history")
         .then(Commands.literal("list").executes(ctx -> {
            CommandSourceStack source = (CommandSourceStack)ctx.getSource();
            Collection<FragmentInstance> instances = active ? FragmentInstanceManager.allActive() : FragmentInstanceManager.allHistory();
            instances.forEach(inst -> {
               if (active) {
                  source.sendSuccess(Cast.constant(Component.literal(inst.getData().getId() + " -> " + inst.getState())), false);
               } else {
                  source.sendSuccess(Cast.constant(Component.literal(inst.getData().getId() + " -> " + inst.getResult())), false);
               }
            });
            return 1;
         }));
      if (active) {
         literal.then(
            Commands.literal("ready")
               .then(
                  Commands.argument("id", StringArgumentType.word())
                     .suggests((ctx, builder) -> suggestIds(true, inst -> inst.getState() != FragmentState.READY, builder))
                     .executes(
                        ctx -> {
                           String id = StringArgumentType.getString(ctx, "id");
                           FragmentData data = FragmentStorage.FRAGMENTS_MAP.get(id);
                           if (data == null) {
                              ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("FragmentData с таким ID не найден"));
                              return 0;
                           } else {
                              FragmentInstance inst = FragmentInstanceManager.getActive(data);
                              if (inst == null) {
                                 ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("FragmentInstance не найден в активных"));
                                 return 0;
                              } else {
                                 inst.setEnd(inst.getNow());
                                 inst.setState(FragmentState.READY);
                                 ((CommandSourceStack)ctx.getSource())
                                    .sendSuccess(Cast.constant(Component.literal("FragmentInstance " + id + " переведен в READY")), false);
                                 return 1;
                              }
                           }
                        }
                     )
               )
         );
      }

      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)literal.then(
               Commands.literal("remove")
                  .then(
                     Commands.argument("id", StringArgumentType.word())
                        .suggests((ctx, builder) -> suggestIds(active, inst -> true, builder))
                        .executes(
                           ctx -> {
                              String id = StringArgumentType.getString(ctx, "id");
                              FragmentData data = FragmentStorage.FRAGMENTS_MAP.get(id);
                              if (data == null) {
                                 ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("FragmentData с таким ID не найден"));
                                 return 0;
                              } else {
                                 FragmentInstance inst = active ? FragmentInstanceManager.getActive(data) : FragmentInstanceManager.getHistory(data);
                                 if (inst == null) {
                                    ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("FragmentInstance не найден"));
                                    return 0;
                                 } else {
                                    inst.setPendingAction(FragmentInstance.MoveAction.REMOVE);
                                    ((CommandSourceStack)ctx.getSource())
                                       .sendSuccess(
                                          Cast.constant(Component.literal("FragmentInstance " + id + " удалён из " + (active ? "активных" : "истории"))), false
                                       );
                                    return 1;
                                 }
                              }
                           }
                        )
                  )
            ))
            .then(Commands.literal("clear").executes(ctx -> {
               CommandSourceStack source = (CommandSourceStack)ctx.getSource();
               Collection<FragmentInstance> instances = active ? FragmentInstanceManager.allActive() : FragmentInstanceManager.allHistory();
               if (instances.isEmpty()) {
                  source.sendSuccess(Cast.constant(Component.literal("Нет " + (active ? "активных" : "исторических") + " фрагментов для удаления.")), false);
                  return 1;
               } else {
                  int count = 0;

                  for (FragmentInstance inst : instances) {
                     inst.setPendingAction(FragmentInstance.MoveAction.REMOVE);
                     count++;
                  }

                  source.sendSuccess(
                     Cast.constant(Component.literal("Удалено " + count + " " + (active ? "активных" : "исторических") + " фрагментов.")), false
                  );
                  return 1;
               }
            })))
         .then(
            Commands.literal("add")
               .then(
                  Commands.argument("id", StringArgumentType.word())
                     .suggests((ctx, builder) -> suggestAddIds(builder))
                     .executes(
                        ctx -> {
                           String id = StringArgumentType.getString(ctx, "id");
                           FragmentData data = FragmentStorage.FRAGMENTS_MAP.get(id);
                           if (data == null) {
                              ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("FragmentData с таким ID не найден"));
                              return 0;
                           } else {
                              boolean exists = FragmentInstanceManager.allActive().stream().anyMatch(inst -> inst.getData().getId().equals(id))
                                 || FragmentInstanceManager.allHistory().stream().anyMatch(inst -> inst.getData().getId().equals(id));
                              if (exists) {
                                 ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("FragmentInstance с таким ID уже существует"));
                                 return 0;
                              } else {
                                 FragmentInstanceManager.addActive(data);
                                 ((CommandSourceStack)ctx.getSource())
                                    .sendSuccess(Cast.constant(Component.literal("FragmentInstance " + id + " добавлен в активные")), false);
                                 return 1;
                              }
                           }
                        }
                     )
               )
         );
      return literal;
   }

   private static CompletableFuture<Suggestions> suggestIds(boolean active, Predicate<FragmentInstance> filter, SuggestionsBuilder builder) {
      Collection<FragmentInstance> instances = active ? FragmentInstanceManager.allActive() : FragmentInstanceManager.allHistory();
      instances.stream().filter(filter).forEach(inst -> builder.suggest(inst.getData().getId()));
      return builder.buildFuture();
   }

   private static CompletableFuture<Suggestions> suggestAddIds(SuggestionsBuilder builder) {
      List<String> activeIds = FragmentInstanceManager.allActive().stream().map(inst -> inst.getData().getId()).toList();
      List<String> historyIds = FragmentInstanceManager.allHistory().stream().map(inst -> inst.getData().getId()).toList();

      for (String fragId : FragmentStorage.FRAGMENTS_MAP.keySet()) {
         if (!activeIds.contains(fragId) && !historyIds.contains(fragId)) {
            builder.suggest(fragId);
         }
      }

      return builder.buildFuture();
   }
}

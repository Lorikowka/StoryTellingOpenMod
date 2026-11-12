package com.storyteam.storytelling.system.lockpicking;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.mixins.minecraft.GiveCommandAccessor;
import com.storyteam.storytelling.system.lockpicking.data.LockComplexity;
import com.storyteam.storytelling.system.lockpicking.data.LockEntry;
import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import com.storyteam.storytelling.system.lockpicking.data.LockStorage;
import com.storyteam.storytelling.system.lockpicking.items.ItemKey;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.command.EnumArgument;

public class CommandLock {
   public static int giveKey(CommandContext<CommandSourceStack> cs, boolean consume, ResourceLocation sound, BlockPos pos) throws CommandSyntaxException {
      ServerPlayer player = ((CommandSourceStack)cs.getSource()).getPlayerOrException();
      ItemStack stack = ItemKey.createKey(consume, sound, GlobalPos.of(player.level().dimension(), pos));

      try {
         return GiveCommandAccessor.giveItem((CommandSourceStack)cs.getSource(), new ItemInput(stack.getItemHolder(), stack.getTag()), List.of(player), 1);
      } catch (Throwable var7) {
         var7.printStackTrace();
         return 0;
      }
   }

   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(
         ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                                 "lock"
                              )
                              .then(
                                 ((LiteralArgumentBuilder)Commands.literal("give-key")
                                       .then(
                                          ((RequiredArgumentBuilder)Commands.argument("consume", BoolArgumentType.bool())
                                                .then(
                                                   Commands.argument("sound", ResourceLocationArgument.id())
                                                      .suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                                                      .then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(cs -> {
                                                         boolean consume = BoolArgumentType.getBool(cs, "consume");
                                                         ResourceLocation sound = ResourceLocationArgument.getId(cs, "sound");
                                                         BlockPos pos = BlockPosArgument.getBlockPos(cs, "pos");
                                                         return giveKey(cs, consume, sound, pos);
                                                      }))
                                                ))
                                             .then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(cs -> {
                                                boolean consume = BoolArgumentType.getBool(cs, "consume");
                                                ResourceLocation sound = null;
                                                BlockPos pos = BlockPosArgument.getBlockPos(cs, "pos");
                                                return giveKey(cs, consume, sound, pos);
                                             }))
                                       ))
                                    .then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(cs -> {
                                       boolean consume = true;
                                       ResourceLocation sound = null;
                                       BlockPos pos = BlockPosArgument.getBlockPos(cs, "pos");
                                       return giveKey(cs, consume, sound, pos);
                                    }))
                              ))
                           .then(
                              Commands.literal("create")
                                 .then(
                                    Commands.argument("pos", BlockPosArgument.blockPos())
                                       .then(
                                          Commands.argument("speed", FloatArgumentType.floatArg(0.5F, 3.0F))
                                             .then(
                                                Commands.argument("notches", IntegerArgumentType.integer(1, 12))
                                                   .then(
                                                      Commands.argument("complexity", EnumArgument.enumArgument(LockComplexity.class))
                                                         .executes(
                                                            context -> {
                                                               GlobalPos globalPos = GlobalPos.of(
                                                                  ((CommandSourceStack)context.getSource()).getLevel().dimension(),
                                                                  BlockPosArgument.getLoadedBlockPos(context, "pos")
                                                               );
                                                               float speed = FloatArgumentType.getFloat(context, "speed");
                                                               int notches = IntegerArgumentType.getInteger(context, "notches");
                                                               LockComplexity difficulty = (LockComplexity)context.getArgument(
                                                                  "complexity", LockComplexity.class
                                                               );
                                                               LockRegistryEntry lock = LockStorage.getLock(globalPos);
                                                               if (lock != null && !lock.getPos().equals(globalPos)) {
                                                                  ((CommandSourceStack)context.getSource())
                                                                     .sendSystemMessage(
                                                                        Component.literal("Замок с такой позицией уже существует!")
                                                                           .withStyle(ChatFormatting.RED)
                                                                     );
                                                                  return 0;
                                                               } else {
                                                                  LockStorage.registerLock(
                                                                     new LockEntry(globalPos, speed, notches, difficulty)
                                                                        .setupExtras(((CommandSourceStack)context.getSource()).getLevel())
                                                                  );
                                                                  ((CommandSourceStack)context.getSource())
                                                                     .sendSystemMessage(
                                                                        Component.literal(
                                                                              "Блок на позиции [" + globalPos.pos().toShortString() + "] был заблокирован"
                                                                           )
                                                                           .withStyle(ChatFormatting.DARK_GREEN)
                                                                     );
                                                                  ((CommandSourceStack)context.getSource())
                                                                     .sendSystemMessage(
                                                                        Component.literal("    Скорость: " + speed).withStyle(ChatFormatting.GOLD)
                                                                     );
                                                                  ((CommandSourceStack)context.getSource())
                                                                     .sendSystemMessage(
                                                                        Component.literal("    Засечек: " + notches).withStyle(ChatFormatting.GOLD)
                                                                     );
                                                                  ((CommandSourceStack)context.getSource())
                                                                     .sendSystemMessage(
                                                                        Component.literal("    Сложность: " + difficulty.name()).withStyle(ChatFormatting.GOLD)
                                                                     );
                                                                  return 1;
                                                               }
                                                            }
                                                         )
                                                   )
                                             )
                                       )
                                 )
                           ))
                        .then(
                           Commands.literal("remove")
                              .then(
                                 Commands.argument("pos", BlockPosArgument.blockPos())
                                    .executes(
                                       context -> {
                                          GlobalPos globalPos = GlobalPos.of(
                                             ((CommandSourceStack)context.getSource()).getLevel().dimension(),
                                             BlockPosArgument.getLoadedBlockPos(context, "pos")
                                          );
                                          LockStorage.removeLock(globalPos);
                                          ((CommandSourceStack)context.getSource())
                                             .sendSystemMessage(
                                                Component.literal("Блок на позиции [" + globalPos.pos().toShortString() + "] был разблокирован")
                                                   .withStyle(ChatFormatting.DARK_GREEN)
                                             );
                                          return 1;
                                       }
                                    )
                              )
                        ))
                     .then(Commands.literal("save").executes(context -> {
                        boolean saved = LockStorage.saveRegistryLocally();
                        ((CommandSourceStack)context.getSource()).sendSystemMessage(Component.literal(
                           saved ? "Реестр замков сохранён локально." : "Не удалось сохранить реестр замков."
                        ));
                        return saved ? 1 : 0;
                     })))
                  .then(
                     Commands.literal("append")
                        .then(
                           Commands.argument("root_pos", BlockPosArgument.blockPos())
                              .then(
                                 Commands.argument("append_pos", BlockPosArgument.blockPos())
                                    .executes(
                                       context -> {
                                          ResourceKey<Level> dim = ((CommandSourceStack)context.getSource()).getLevel().dimension();
                                          GlobalPos rootPos = GlobalPos.of(dim, BlockPosArgument.getLoadedBlockPos(context, "root_pos"));
                                          GlobalPos appendPos = GlobalPos.of(dim, BlockPosArgument.getLoadedBlockPos(context, "append_pos"));
                                          LockRegistryEntry data = LockStorage.getLock(rootPos);
                                          if (data == null) {
                                             ((CommandSourceStack)context.getSource())
                                                .sendSystemMessage(Component.literal("Замка на root_pos не существует!").withStyle(ChatFormatting.RED));
                                             return 0;
                                          } else if (LockStorage.getLock(appendPos) != null) {
                                             ((CommandSourceStack)context.getSource())
                                                .sendSystemMessage(Component.literal("Замок на append_pos уже существует!").withStyle(ChatFormatting.RED));
                                             return 0;
                                          } else {
                                             data.entry.addPos(appendPos.pos());
                                             LockStorage.recomputePos();
                                             ((CommandSourceStack)context.getSource())
                                                .sendSystemMessage(
                                                   Component.literal(
                                                         "Блок на позиции ["
                                                            + appendPos.pos().toShortString()
                                                            + "] был успешно добавлен к замку на позиции ["
                                                            + rootPos.pos().toShortString()
                                                            + "]!"
                                                      )
                                                      .withStyle(ChatFormatting.DARK_GREEN)
                                                );
                                             return 1;
                                          }
                                       }
                                    )
                              )
                        )
                  ))
               .then(
                  Commands.literal("list")
                     .executes(
                        context -> {
                           ((CommandSourceStack)context.getSource()).sendSystemMessage(Component.literal("Замки: ").withStyle(ChatFormatting.DARK_GREEN));

                           for (Entry<GlobalPos, LockRegistryEntry> entry : LockStorage.REGISTRY.entrySet()) {
                              GlobalPos pos = entry.getKey();
                              LockRegistryEntry data = entry.getValue();
                              LockEntry en = data.entry;
                              ResourceKey<Level> dim = pos.dimension();
                              ClickEvent ce = new ClickEvent(
                                 Action.SUGGEST_COMMAND,
                                 "/execute in " + dim.location() + " run tp " + pos.pos().getX() + " " + pos.pos().getY() + " " + pos.pos().getZ()
                              );
                              HoverEvent he = new HoverEvent(
                                 net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT,
                                 Component.literal("Сложность: " + en.getComplexity().name() + "\nСкорость: " + en.getSpeed() + "\nЗасечек: " + en.getNotches())
                                    .withStyle(ChatFormatting.GRAY)
                              );
                              ((CommandSourceStack)context.getSource())
                                 .sendSystemMessage(
                                    Component.literal("    ")
                                       .append(
                                          Component.literal("[" + pos.pos().toShortString() + "]")
                                             .withStyle(Style.EMPTY.withClickEvent(ce).withHoverEvent(he).withColor(ChatFormatting.GOLD))
                                       )
                                 );
                           }

                           return 1;
                        }
                     )
               ))
            .then(
               Commands.literal("reset")
                  .then(
                     Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(
                           context -> {
                              GlobalPos globalPos = GlobalPos.of(
                                 ((CommandSourceStack)context.getSource()).getLevel().dimension(), BlockPosArgument.getLoadedBlockPos(context, "pos")
                              );
                              LockRegistryEntry lock = LockStorage.getLock(globalPos);
                              if (lock == null) {
                                 ((CommandSourceStack)context.getSource())
                                    .sendSystemMessage(Component.literal("Замка с такой позицией не существует!").withStyle(ChatFormatting.RED));
                                 return 1;
                              } else {
                                 lock.reset();
                                 ((CommandSourceStack)context.getSource())
                                    .sendSystemMessage(
                                       Component.literal("Прогресс разблокирования блока на позиции [" + globalPos.pos().toShortString() + "] был сброшен!")
                                          .withStyle(ChatFormatting.DARK_GREEN)
                                    );
                                 return 1;
                              }
                           }
                        )
                  )
            )
      );
   }
}

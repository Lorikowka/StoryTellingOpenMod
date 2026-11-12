package com.storyteam.storytelling.system.containertier.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.storyteam.storytelling.content.tiles.TileLinkableContainer;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.containertier.ContainerTierPopulator;
import com.storyteam.storytelling.system.containertier.ContainerTierRandomizer;
import com.storyteam.storytelling.system.containertier.storage.ContainerTierStorage;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import lombok.Generated;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ContainerTierCommand {
   private static final DynamicCommandExceptionType ERROR_TIER_DOES_NOT_EXIST = new DynamicCommandExceptionType(
      id -> Component.literal("Тир " + id + " не найден.").withStyle(ChatFormatting.RED)
   );

   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create(e.getContext()));
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create(CommandBuildContext ctx) {
      return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("chesttier")
               .then(
                  Commands.literal("target")
                     .then(
                        Commands.argument("tier", StringArgumentType.string())
                           .suggests((c, b) -> SharedSuggestionProvider.suggest(ContainerTierStorage.getTiersKeys(), b))
                           .then(
                              Commands.argument("rolls", IntegerArgumentType.integer(1))
                                 .executes(
                                    cs -> {
                                       if (((CommandSourceStack)cs.getSource()).getPlayer() == null) {
                                          return 0;
                                       } else {
                                          int rolls = IntegerArgumentType.getInteger(cs, "rolls");
                                          String tier = StringArgumentType.getString(cs, "tier");
                                          BlockEntity be = ((CommandSourceStack)cs.getSource())
                                             .getLevel()
                                             .getBlockEntity(
                                                clipWithDistance(
                                                   ((CommandSourceStack)cs.getSource()).getPlayer(), ((CommandSourceStack)cs.getSource()).getLevel(), 12.0
                                                )
                                             );
                                          if (be instanceof BaseContainerBlockEntity bcbe) {
                                             ContainerTierRandomizer.fillChest(bcbe, ContainerTierStorage.getTier(tier), rolls);
                                             return 1;
                                          } else if (be instanceof TileLinkableContainer bcbe) {
                                             ContainerTierRandomizer.fillChest(bcbe, ContainerTierStorage.getTier(tier), rolls);
                                             return 1;
                                          } else {
                                             return 0;
                                          }
                                       }
                                    }
                                 )
                           )
                     )
               ))
            .then(
               Commands.literal("radius")
                  .then(
                     Commands.argument("radius", IntegerArgumentType.integer(1))
                        .then(
                           Commands.argument("tier", StringArgumentType.string())
                              .suggests((c, b) -> SharedSuggestionProvider.suggest(ContainerTierStorage.getTiersKeys(), b))
                              .then(Commands.argument("rolls", IntegerArgumentType.integer(1)).executes(cs -> {
                                 if (((CommandSourceStack)cs.getSource()).getPlayer() == null) {
                                    return 0;
                                 } else {
                                    Player player = ((CommandSourceStack)cs.getSource()).getPlayer();
                                    Level level = ((CommandSourceStack)cs.getSource()).getLevel();
                                    int radius = (Integer)cs.getArgument("radius", Integer.class);
                                    int rolls = (Integer)cs.getArgument("rolls", Integer.class);
                                    String tier = (String)cs.getArgument("tier", String.class);
                                    ContainerTierRandomizer.foreachInRadius(player, level, ContainerTierStorage.getTier(tier), radius, rolls);
                                    return 1;
                                 }
                              }))
                        )
                  )
            ))
         .then(
            Commands.literal("fill")
               .then(
                  Commands.argument("radius", IntegerArgumentType.integer(1))
                     .then(
                        Commands.argument("item", ItemArgument.item(ctx))
                           .then(Commands.argument("total", IntegerArgumentType.integer(1)).then((ArgumentBuilder)Util.make(Commands.literal("split"), c -> {
                              for (ContainerTierCommand.FillMode value : ContainerTierCommand.FillMode.values()) {
                                 c.then(value.builder.apply(ContainerTierCommand::distributeItems));
                              }
                           })))
                     )
               )
         );
   }

   private static int distributeItems(CommandContext<CommandSourceStack> cs, IntProvider clumps) throws CommandSyntaxException {
      int radius = IntegerArgumentType.getInteger(cs, "radius");
      int total = IntegerArgumentType.getInteger(cs, "total");
      ItemInput item = ItemArgument.getItem(cs, "item");
      AtomicInteger leftover = new AtomicInteger(total);
      ServerPlayer player = ((CommandSourceStack)cs.getSource()).getPlayerOrException();
      ServerLevel level = player.serverLevel();
      NonNullList<ContainerTierPopulator.BoundSlot> slots = ContainerTierPopulator.gatherSlots(player, level, radius);
      Set<BaseContainerBlockEntity> all = new HashSet<>(slots.stream().map(ContainerTierPopulator.BoundSlot::container).toList());

      while (!slots.isEmpty() && leftover.get() > 0) {
         NonNullList<ContainerTierPopulator.BoundSlot> populated = ContainerTierPopulator.distributeItemOnce(
            slots, item.createItemStack(1, false), leftover, clumps
         );
         slots.removeAll(populated);

         for (ContainerTierPopulator.BoundSlot sl : populated) {
            all.remove(sl.container());
         }

         if (populated.isEmpty()) {
            break;
         }
      }

      if (leftover.get() > 0) {
         ((CommandSourceStack)cs.getSource()).sendFailure(Component.literal("Не все предметы были распределены. Осталось " + leftover.get() + " предметов."));
      }

      if (!all.isEmpty()) {
         ((CommandSourceStack)cs.getSource()).sendFailure(Component.literal("Не все контейнеры были заполнены:"));

         for (BaseContainerBlockEntity ctr : all) {
            BlockPos pos = ctr.getBlockPos();
            ((CommandSourceStack)cs.getSource())
               .sendFailure(
                  Component.literal(pos.toShortString())
                     .setStyle(
                        Style.EMPTY
                           .withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tp %s %s %s".formatted(pos.getX(), pos.getY(), pos.getZ())))
                           .withUnderlined(true)
                     )
               );
         }
      }

      return total - leftover.get();
   }

   @NotNull
   private static BlockPos clipWithDistance(@NotNull Player player, @NotNull Level level, double clipDistance) {
      double vecX = Math.sin((double)(-player.getYRot()) * (Math.PI / 180.0) - Math.PI) * -Math.cos((double)(-player.getXRot()) * (Math.PI / 180.0));
      double vecY = Math.sin((double)(-player.getXRot()) * (Math.PI / 180.0));
      double vecZ = Math.cos((double)(-player.getYRot()) * (Math.PI / 180.0) - Math.PI) * -Math.cos((double)(-player.getXRot()) * (Math.PI / 180.0));
      return level.clip(
            new ClipContext(
               player.getEyePosition(),
               player.getEyePosition().add(vecX * clipDistance, vecY * clipDistance, vecZ * clipDistance),
               Block.OUTLINE,
               Fluid.NONE,
               player
            )
         )
         .getBlockPos();
   }

   private interface DistributionCommand {
      int execute(CommandContext<CommandSourceStack> var1, IntProvider var2) throws CommandSyntaxException;
   }

   public static enum FillMode {
      UNIFORM(
         cmd -> Commands.literal("uniform")
               .then(
                  Commands.argument("min-inclusive", IntegerArgumentType.integer())
                     .then(
                        Commands.argument("max-inclusive", IntegerArgumentType.integer())
                           .executes(
                              cs -> cmd.execute(
                                    cs, UniformInt.of(IntegerArgumentType.getInteger(cs, "min-inclusive"), IntegerArgumentType.getInteger(cs, "max-inclusive"))
                                 )
                           )
                     )
               )
      ),
      FIXED(
         cmd -> Commands.literal("fixed")
               .then(
                  Commands.argument("amount", IntegerArgumentType.integer())
                     .executes(cs -> cmd.execute(cs, ConstantInt.of(IntegerArgumentType.getInteger(cs, "amount"))))
               )
      );

      final Function<ContainerTierCommand.DistributionCommand, ArgumentBuilder<CommandSourceStack, ?>> builder;

      @Generated
      private FillMode(Function<ContainerTierCommand.DistributionCommand, ArgumentBuilder<CommandSourceStack, ?>> builder) {
         this.builder = builder;
      }
   }
}

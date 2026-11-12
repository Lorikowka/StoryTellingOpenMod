package com.storyteam.storytelling.system.currency.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.currency.Currency;
import com.storyteam.storytelling.system.currency.storage.CurrencyRegistry;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.util.java.Cast;

public class CurrencySubCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return Commands.literal("currency").then(Commands.literal("set").then(setCurrency()));
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setCurrency() {
      return Commands.argument("currency_id", StringArgumentType.word()).suggests((ctx, builder) -> {
         CurrencyRegistry.getAll().forEach(currency -> builder.suggest(currency.getId()));
         return builder.buildFuture();
      }).then(Commands.argument("count", IntegerArgumentType.integer(0)).executes(context -> {
         CommandSourceStack source = (CommandSourceStack)context.getSource();
         String currencyId = (String)context.getArgument("currency_id", String.class);
         int count = (Integer)context.getArgument("count", Integer.class);
         Currency currency = CurrencyRegistry.get(currencyId);
         if (currency == null) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
         } else {
            CurrencyStorage.setCurrency(currency, count);
            source.sendSuccess(Cast.constant(Component.literal("Текущий баланс валюты %s изменён на %s".formatted(currencyId, count))), true);
            return 1;
         }
      }));
   }
}

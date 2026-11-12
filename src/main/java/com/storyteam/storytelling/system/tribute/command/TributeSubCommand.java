package com.storyteam.storytelling.system.tribute.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.tribute.data.offer.TributeOffer;
import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import com.storyteam.storytelling.system.tribute.storage.registry.TributeOfferRegistry;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.util.java.Cast;

public class TributeSubCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return ((LiteralArgumentBuilder)Commands.literal("tribute").then(Commands.literal("set").then(setOffer()))).then(setNextOffer());
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setOffer() {
      return Commands.literal("offer").then(Commands.argument("offer_id", StringArgumentType.string()).suggests((ctx, builder) -> {
         TributeOfferRegistry.getAll().forEach(offer -> builder.suggest(offer.getIdPath()));
         builder.suggest("empty");
         return builder.buildFuture();
      }).executes(context -> {
         CommandSourceStack source = (CommandSourceStack)context.getSource();
         String offerId = (String)context.getArgument("offer_id", String.class);
         Optional<TributeOffer> optionalOffer = TributeOfferRegistry.get(offerId);
         if (offerId.equals("empty")) {
            optionalOffer = Optional.of(TributeOffer.empty());
         }

         if (optionalOffer.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
         } else {
            TributeOfferStorage.setOffer(optionalOffer.get(), TimeController.get().getCurrentDay());
            source.sendSuccess(Cast.constant(Component.literal("Текущий оффер изменён на %s".formatted(offerId))), true);
            return 1;
         }
      }));
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setNextOffer() {
      return Commands.literal("set_next").executes(context -> {
         CommandSourceStack source = (CommandSourceStack)context.getSource();
         TributeOffer currentOffer = TributeOfferStorage.getOffer();
         TributeOfferStorage.setNextOffer();
         TributeOffer newOffer = TributeOfferStorage.getOffer();
         String currentOfferId = currentOffer.isEmpty() ? "empty" : currentOffer.getIdPath();
         String newOfferId = newOffer.isEmpty() ? "empty" : newOffer.getIdPath();
         source.sendSuccess(Cast.constant(Component.literal("Текущий оффер %s изменён на %s".formatted(currentOfferId, newOfferId))), true);
         return 1;
      });
   }
}

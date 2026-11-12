package com.storyteam.storytelling.system.weather;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WeatherCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create() {
      return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("weather")
            .then(Commands.argument("weather", WeatherCommand.WeatherArgument.weather()).executes(cs -> {
               WeatherSystem.setWeather((Weather)cs.getArgument("weather", Weather.class));
               return 1;
            })))
         .then(Commands.literal("get").executes(cs -> {
            ((CommandSourceStack)cs.getSource()).sendSuccess(() -> Component.literal(WeatherSystem.currentWeather().name()), true);
            return 1;
         }));
   }

   public static class WeatherArgument implements ArgumentType<Weather> {
      public static WeatherCommand.WeatherArgument weather() {
         return new WeatherCommand.WeatherArgument();
      }

      public Weather parse(StringReader reader) throws CommandSyntaxException {
         return WeatherSystem.REGISTRY.get(reader.readString());
      }

      public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
         return SharedSuggestionProvider.suggest(WeatherSystem.REGISTRY.keySet(), builder);
      }
   }
}

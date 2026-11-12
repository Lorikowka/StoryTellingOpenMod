package com.storyteam.storytelling.content.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.storyteam.storytelling.system._core.structio.repo.StructureRepository;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class CloudStructureFileArgument implements ArgumentType<String> {
   private static final DynamicCommandExceptionType STRUCTURE_NOT_FOUND = new DynamicCommandExceptionType(
      name -> Component.literal("Структура с именем " + name + " не обнаружена.")
   );

   public static CloudStructureFileArgument cloudStructure() {
      return new CloudStructureFileArgument();
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      String name = reader.readString();

      try {
         String var4;
         try (Stream<String> list = getAvailableStructures()) {
            var4 = list.filter(s -> s.equals(name)).findAny().orElseThrow(() -> STRUCTURE_NOT_FOUND.createWithContext(reader, name));
         }

         return var4;
      } catch (IOException var8) {
         var8.printStackTrace();
         return name;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      try {
         CompletableFuture var4;
         try (Stream<String> list = getAvailableStructures()) {
            var4 = SharedSuggestionProvider.suggest(list, builder);
         }

         return var4;
      } catch (IOException var8) {
         var8.printStackTrace();
         return Suggestions.empty();
      }
   }

   private static Stream<String> getAvailableStructures() throws IOException {
      StructureRepository.refresh();
      return StructureRepository.getRemoteStructureNames().stream();
   }
}

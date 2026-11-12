package com.storyteam.storytelling.content.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.concurrent.CompletableFuture;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class CharacterArgument implements ArgumentType<Character<?>> {
   private static final DynamicCommandExceptionType ERROR_ENTITY_NOT_FOUND = new DynamicCommandExceptionType(
      id -> Component.literal("Сюжетный персонаж " + id + " не найден.").withStyle(ChatFormatting.RED)
   );

   public static CharacterArgument character() {
      return new CharacterArgument();
   }

   public Character<?> parse(StringReader reader) throws CommandSyntaxException {
      String name = reader.readString();
      Character<?> character = Character.getById(name);
      if (character == null) {
         throw ERROR_ENTITY_NOT_FOUND.create(name);
      } else {
         return character;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return SharedSuggestionProvider.suggest(Character.getCharacters().keySet(), builder);
   }
}

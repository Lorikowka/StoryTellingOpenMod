package com.storyteam.storytelling.content.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.data.EntityController;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class EntityReferenceArgument implements ArgumentType<EntityController<?>> {
   private static final DynamicCommandExceptionType ERROR_ENTITY_NOT_FOUND = new DynamicCommandExceptionType(
      id -> Component.literal("Сюжетный персонаж " + id + " не найден.").withStyle(ChatFormatting.RED)
   );

   public static EntityReferenceArgument reference() {
      return new EntityReferenceArgument();
   }

   public EntityController<?> parse(StringReader reader) throws CommandSyntaxException {
      String name = reader.readString();
      EntityController<?> entity = IStoryServer.get().getEntityRepository().get(name);
      if (entity == null) {
         throw ERROR_ENTITY_NOT_FOUND.create(name);
      } else {
         return entity;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return SharedSuggestionProvider.suggest(
         IStoryServer.get().getEntityRepository().streamReferences().map(EntityController::getStoryKey).collect(Collectors.toList()), builder
      );
   }
}

package com.storyteam.storytelling.content.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;

public class ScriptTriggerArgument implements ArgumentType<String> {
   public static ScriptTriggerArgument trigger() {
      return new ScriptTriggerArgument();
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      return reader.readString();
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      int id = (Integer)context.getArgument("script", Integer.class);
      ScriptEnvironment env = IStoryServer.get().getScriptEnv();
      List<ScriptInstance.ScriptSubcommand> subcommands = ScriptInstance.gatherSubcommands(env.getScript(id), env);
      return SharedSuggestionProvider.suggest(subcommands.stream().map(ScriptInstance.ScriptSubcommand::getName), builder);
   }
}

package com.storyteam.storytelling.content.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.commands.OrderedSuggestions;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import java.lang.reflect.Constructor;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ScriptIdArgument implements ArgumentType<Integer> {
   protected static final DynamicCommandExceptionType ERROR_SCRIPT_DOES_NOT_EXIST = new DynamicCommandExceptionType(
      id -> Component.literal("Скрипт " + id + " не найден.").withStyle(ChatFormatting.RED)
   );
   protected static final DynamicCommandExceptionType ERROR_SCRIPT_NOT_RUNNING = new DynamicCommandExceptionType(
      id -> Component.literal("Скрипт " + id + " не запущен.").withStyle(ChatFormatting.RED)
   );
   protected final boolean requireActive;

   public static ScriptIdArgument script() {
      return new ScriptIdArgument(false);
   }

   public static ScriptIdArgument activeScript() {
      return new ScriptIdArgument(true);
   }

   public ScriptIdArgument(boolean requireActive) {
      this.requireActive = requireActive;
   }

   public Integer parse(StringReader reader) throws CommandSyntaxException {
      int id = reader.readInt();
      ScriptEnvironment env = IStoryServer.get().getScriptEnv();
      Constructor<? extends ScriptInstance> constructor = (Constructor<? extends ScriptInstance>)env.constructors.get(id);
      if (constructor == null) {
         throw ERROR_SCRIPT_DOES_NOT_EXIST.create(id);
      } else if (this.requireActive && env.getScript(id) == null) {
         throw ERROR_SCRIPT_NOT_RUNNING.create(id);
      } else {
         return id;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      ScriptEnvironment env = IStoryServer.get().getScriptEnv();
      IntStream stream = env.constructors.keySet().intStream();
      if (this.requireActive) {
         stream = stream.filter(env.scriptInstances::containsKey);
      }

      return suggest(stream.sorted().mapToObj(String::valueOf), builder);
   }

   static CompletableFuture<Suggestions> suggest(Stream<String> strings_, SuggestionsBuilder builder_) {
      return CompletableFuture.completedFuture(
         new OrderedSuggestions(
            new StringRange(builder_.getStart(), builder_.getInput().length()),
            strings_.<Suggestion>map(s -> new Suggestion(StringRange.between(builder_.getStart(), builder_.getInput().length()), s))
               .collect(Collectors.toList())
         )
      );
   }
}

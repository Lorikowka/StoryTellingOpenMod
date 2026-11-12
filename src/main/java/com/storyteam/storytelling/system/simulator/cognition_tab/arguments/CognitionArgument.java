package com.storyteam.storytelling.system.simulator.cognition_tab.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHolder;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionSystem;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CognitionArgument implements ArgumentType<Cognition> {
   private static final Collection<Cognition> EXAMPLES = CognitionHolder.INSTANCE.getCognitions();
   private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(
      object -> Component.literal("Не найдено познание " + object)
   );

   public static CognitionArgument cognition() {
      return new CognitionArgument();
   }

   public Cognition parse(StringReader reader) throws CommandSyntaxException {
      String arg = reader.readUnquotedString();

      try {
         ResourceLocation id = CognitionSystem.INSTANCE.id(arg);
         Cognition cognition = CognitionHolder.INSTANCE.getCognition(id);
         if (cognition.isEmpty()) {
            throw ERROR_INVALID.createWithContext(reader, arg);
         } else {
            return cognition;
         }
      } catch (NumberFormatException var5) {
         throw ERROR_INVALID.createWithContext(reader, arg);
      }
   }

   public Collection<String> getExamples() {
      return EXAMPLES.stream().map(c -> c.id().getPath()).collect(Collectors.toList());
   }

   public static Cognition getCognition(CommandContext<CommandSourceStack> context, String string) throws CommandSyntaxException {
      return (Cognition)context.getArgument(string, Cognition.class);
   }
}

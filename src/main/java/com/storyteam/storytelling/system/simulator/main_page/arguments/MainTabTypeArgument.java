package com.storyteam.storytelling.system.simulator.main_page.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.storyteam.storytelling.system.simulator.main_page.MainTabType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class MainTabTypeArgument implements ArgumentType<MainTabType> {
   private static final MainTabType[] EXAMPLES = MainTabType.values();
   private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(object -> Component.literal("Не найден тип " + object));

   public static MainTabTypeArgument type() {
      return new MainTabTypeArgument();
   }

   public MainTabType parse(StringReader reader) throws CommandSyntaxException {
      String arg = reader.readUnquotedString();

      try {
         return MainTabType.valueOf(arg.toUpperCase());
      } catch (IllegalArgumentException var4) {
         throw ERROR_INVALID.create(arg);
      }
   }

   public Collection<String> getExamples() {
      return new ArrayList<>(Arrays.asList(EXAMPLES)).stream().map(type -> type.name().toLowerCase()).collect(Collectors.toList());
   }

   public static MainTabType getType(CommandContext<CommandSourceStack> context, String string) throws CommandSyntaxException {
      return (MainTabType)context.getArgument(string, MainTabType.class);
   }
}

package com.storyteam.storytelling.system._core.morph;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class CommandMorph {
   private static final DynamicCommandExceptionType ERROR_UNKNOWN_ATTRIBUTE = new DynamicCommandExceptionType(p_212392_ -> Component.literal("Unknown morph"));

   @SubscribeEvent
   static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create() {
      return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("morph")
         .then(Commands.argument("morph", ResourceKeyArgument.key(MorphSystem.playerMorphs().getRegistryKey())).suggests((context, builder) -> {
            IForgeRegistry<PlayerMorph> key = MorphSystem.playerMorphs();
            return SharedSuggestionProvider.suggestResource(key.getKeys(), builder);
         }).executes(cs -> {
            PlayerMorph m = getTransformation(cs, "morph");
            ((IAnimatedPlayer)((CommandSourceStack)cs.getSource()).getPlayerOrException()).morphInto(m);
            return 1;
         }));
   }

   public static PlayerMorph getTransformation(CommandContext<CommandSourceStack> pContext, String pName) throws CommandSyntaxException {
      ResourceKey<Registry<PlayerMorph>> key = MorphSystem.playerMorphs().getRegistryKey();
      ResourceKey<PlayerMorph> rk = getRegistryType(pContext, pName, key, ERROR_UNKNOWN_ATTRIBUTE);
      return (PlayerMorph)Optional.ofNullable((PlayerMorph)MorphSystem.playerMorphs().getValue(rk.location()))
         .orElseThrow(() -> ERROR_UNKNOWN_ATTRIBUTE.create(rk.location()));
   }

   private static <T> ResourceKey<T> getRegistryType(
      CommandContext<CommandSourceStack> pContext, String pName, ResourceKey<Registry<T>> pRegistryKey, DynamicCommandExceptionType errType
   ) throws CommandSyntaxException {
      ResourceKey<?> resourcekey = (ResourceKey<?>)pContext.getArgument(pName, ResourceKey.class);
      Optional<ResourceKey<T>> optional = resourcekey.cast(pRegistryKey);
      return optional.orElseThrow(() -> errType.create(resourcekey));
   }
}

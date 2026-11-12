package com.storyteam.storytelling.system.sounds;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundSubcommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return Commands.literal("sound")
         .then(
            ((RequiredArgumentBuilder)Commands.argument("sound", ResourceLocationArgument.id())
                  .suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                  .executes(
                     context -> {
                        ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
                        SoundEvent sound = (SoundEvent)((CommandSourceStack)context.getSource())
                           .registryAccess()
                           .lookupOrThrow(Registries.SOUND_EVENT)
                           .getOrThrow(ResourceKey.create(Registries.SOUND_EVENT, ResourceLocationArgument.getId(context, "sound")))
                           .get();
                        player.displayClientMessage(
                           Component.literal("Громкость " + sound.getLocation() + ": " + SoundUtils.getSoundVolume(sound))
                              .withStyle(ChatFormatting.YELLOW)
                              .withStyle(ChatFormatting.BOLD),
                           true
                        );
                        SoundUtils.playVolumedSound(player, sound);
                        return 1;
                     }
                  ))
               .then(
                  Commands.literal("volume")
                     .then(
                        Commands.argument("volume", FloatArgumentType.floatArg())
                           .executes(
                              context -> {
                                 SoundEvent sound = (SoundEvent)((CommandSourceStack)context.getSource())
                                    .registryAccess()
                                    .lookupOrThrow(Registries.SOUND_EVENT)
                                    .getOrThrow(ResourceKey.create(Registries.SOUND_EVENT, ResourceLocationArgument.getId(context, "sound")))
                                    .get();
                                 SoundUtils.setSoundVolume(sound, (Float)context.getArgument("volume", Float.class));
                                 SoundUtils.playVolumedSound(((CommandSourceStack)context.getSource()).getPlayerOrException(), sound);
                                 return 1;
                              }
                           )
                     )
               )
         );
   }
}

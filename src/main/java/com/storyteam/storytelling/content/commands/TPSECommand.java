package com.storyteam.storytelling.content.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.storyteam.storytelling.content.commands.arguments.EntityReferenceArgument;
import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class TPSECommand {
   private static final DynamicCommandExceptionType ERROR_POSITION_NOT_FOUND = new DynamicCommandExceptionType(
      name -> Component.literal("Не удалось определить позицию для персонажа \"" + name + "\".").withStyle(ChatFormatting.RED)
   );

   public static void register(ICommand.ICommandRegistry dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("tpse")
            .then(((RequiredArgumentBuilder)Commands.argument("character", EntityReferenceArgument.reference()).executes(cs -> {
               ServerPlayer player = ((CommandSourceStack)cs.getSource()).getPlayerOrException();
               EntityController<?> ref = (EntityController<?>)cs.getArgument("character", EntityController.class);
               Location pos = ref.getLastRegisteredPos();
               if (pos == null) {
                  throw ERROR_POSITION_NOT_FOUND.create(ref.getStoryKey());
               } else {
                  pos.teleport(player);
                  return 1;
               }
            })).then(Commands.literal("player").executes(cs -> {
               ServerPlayer player = ((CommandSourceStack)cs.getSource()).getPlayerOrException();
               EntityController<?> ref = (EntityController<?>)cs.getArgument("character", EntityController.class);
               Entity entity = ref.getEntity();
               if (entity == null) {
                  ref.createFreshEntity(player.level(), new Location(player));
               } else {
                  entity.changeDimension((ServerLevel)player.level());
                  entity.teleportTo(player.getX(), player.getY(), player.getZ());
               }

               return 1;
            })))
      );
   }
}

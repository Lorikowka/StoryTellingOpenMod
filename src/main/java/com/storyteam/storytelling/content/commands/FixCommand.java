package com.storyteam.storytelling.content.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system._core.character_data.CharacterDataManager;
import com.storyteam.storytelling.util.ScriptUtils;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.util.java.Cast;

public class FixCommand {
   public static void register(ICommand.ICommandRegistry dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                              "fixst"
                           )
                           .then(Commands.literal("renderDebugPaths").then(Commands.argument("state", BoolArgumentType.bool()).executes(cs -> {
                              Storytelling.renderDebugPaths = BoolArgumentType.getBool(cs, "state");
                              return 1;
                           }))))
                        .then(Commands.literal("test").executes(cs -> {
                           Inventory i = ScriptUtils.getPlayer().getInventory();
                           ItemStack stack = i.getItem(i.selected);
                           ((CommandSourceStack)cs.getSource())
                              .sendSuccess(Cast.constant(Component.literal(stack.getDamageValue() + "/" + stack.getMaxDamage())), true);
                           i = Minecraft.getInstance().player.getInventory();
                           stack = i.getItem(i.selected);
                           ((CommandSourceStack)cs.getSource())
                              .sendSuccess(Cast.constant(Component.literal(stack.getDamageValue() + "/" + stack.getMaxDamage())), true);
                           return 1;
                        })))
                     .then(Commands.literal("teleportOnFreeCamera").then(Commands.argument("state", BoolArgumentType.bool()).executes(cs -> {
                        Storytelling.teleportOnFreeCamera = BoolArgumentType.getBool(cs, "state");
                        return 1;
                     }))))
                  .then(Commands.literal("purge_character_extras").executes(cs -> {
                     try {
                        ((CommandSourceStack)cs.getSource()).sendSuccess(Cast.constant(Component.literal("Started purging.")), true);
                        CharacterDataManager.purge();
                        ((CommandSourceStack)cs.getSource()).sendSuccess(Cast.constant(Component.literal("Ok.")), true);
                        return 1;
                     } catch (IOException var2) {
                        ((CommandSourceStack)cs.getSource()).sendFailure(Component.literal(var2.getMessage()));
                        return 0;
                     }
                  })))
               )
      );
   }
}

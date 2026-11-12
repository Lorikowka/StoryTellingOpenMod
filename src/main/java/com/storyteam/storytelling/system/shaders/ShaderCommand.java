package com.storyteam.storytelling.system.shaders;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.shaders.net.EnableShaderSystemPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.net.Network;

public class ShaderCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create() {
      return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("shaders").then(Commands.literal("enable").executes(cs -> {
         Network.sendTo(((CommandSourceStack)cs.getSource()).getPlayerOrException(), new EnableShaderSystemPacket(true));
         return 1;
      }))).then(Commands.literal("disable").executes(cs -> {
         Network.sendTo(((CommandSourceStack)cs.getSource()).getPlayerOrException(), new EnableShaderSystemPacket(false));
         return 1;
      }));
   }
}

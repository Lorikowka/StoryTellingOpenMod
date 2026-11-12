package com.storyteam.storytelling.system.simulator.cognition_tab.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.S2CToggleEditorMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber
public class CognitionCommands {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return Commands.literal("cognition").then(Commands.literal("editor_mod").executes(ctx -> {
         ServerPlayer player = ((CommandSourceStack)ctx.getSource()).getPlayerOrException();
         Network.sendTo(player, new S2CToggleEditorMod());
         return 1;
      }));
   }
}

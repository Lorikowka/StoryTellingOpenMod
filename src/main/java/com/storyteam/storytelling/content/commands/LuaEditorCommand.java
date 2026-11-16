package com.storyteam.storytelling.content.commands;

import com.storyteam.storytelling.net.PacketOpenLuaEditor;
import com.storyteam.storytelling.net.PacketOpenLuaBlocks;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.net.Network;

public final class LuaEditorCommand {
   private LuaEditorCommand() { }

   public static void register(ICommand.ICommandRegistry registry) {
      registry.register(Commands.literal("storyedit").executes(ctx -> {
         if (!ctx.getSource().getServer().isSingleplayer()) {
            ctx.getSource().sendFailure(Component.literal("Встроенный редактор доступен только в одиночном мире"));
            return 0;
         }
         Network.sendTo(ctx.getSource().getPlayerOrException(), new PacketOpenLuaEditor());
         return 1;
      }));
      registry.register(Commands.literal("storyblocks").executes(ctx -> {
         if (!ctx.getSource().getServer().isSingleplayer()) {
            ctx.getSource().sendFailure(Component.literal("Редактор блоков доступен только в одиночном мире"));
            return 0;
         }
         Network.sendTo(ctx.getSource().getPlayerOrException(), new PacketOpenLuaBlocks());
         return 1;
      }));
   }
}

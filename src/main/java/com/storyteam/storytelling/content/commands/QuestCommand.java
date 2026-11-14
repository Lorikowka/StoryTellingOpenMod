package com.storyteam.storytelling.content.commands;

import com.storyteam.storytelling.net.PacketQuestJournal;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import net.minecraft.commands.Commands;
import org.zeith.hammerlib.net.Network;

public final class QuestCommand {
   private QuestCommand() { }

   public static void register(ICommand.ICommandRegistry registry) {
      ScriptEnvironment env = registry.getStoryServer().getScriptEnv();
      registry.register(Commands.literal("quest").executes(ctx -> {
         Network.sendTo(ctx.getSource().getPlayerOrException(), new PacketQuestJournal(env.questJournal.displayLines()));
         return 1;
      }));
   }
}

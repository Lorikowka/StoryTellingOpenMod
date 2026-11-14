package com.storyteam.storytelling.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.commands.FixCommand;
import com.storyteam.storytelling.content.commands.ICommand;
import com.storyteam.storytelling.content.commands.STCommand;
import com.storyteam.storytelling.content.commands.STPathCommand;
import com.storyteam.storytelling.content.commands.ScriptCommand;
import com.storyteam.storytelling.content.commands.QuestCommand;
import com.storyteam.storytelling.content.commands.TPSECommand;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CommandsST {
   private static final List<Consumer<ICommand.ICommandRegistry>> COMMANDS = new ArrayList<>();

   @SubscribeEvent
   public static void registerCommands(RegisterCommandsEvent event) {
      MinecraftServer server = (MinecraftServer)IStoryServer.get();
      if (server != null) {
         register(server, event.getDispatcher(), event.getBuildContext());
      }
   }

   public static void register(final MinecraftServer mcs, final CommandDispatcher<CommandSourceStack> dispatcher, final CommandBuildContext buildContext) {
      ICommand.ICommandRegistry reg = new ICommand.ICommandRegistry() {
         @Override
         public MinecraftServer getServer() {
            return mcs;
         }

         @Override
         public CommandBuildContext buildContext() {
            return buildContext;
         }

         @Override
         public LiteralCommandNode<CommandSourceStack> register(LiteralArgumentBuilder<CommandSourceStack> cmd) {
            return dispatcher.register(cmd);
         }
      };
      reg.init();
      COMMANDS.forEach(h -> h.accept(reg));
   }

   static {
      COMMANDS.add(FixCommand::register);
      COMMANDS.add(ScriptCommand::register);
      COMMANDS.add(QuestCommand::register);
      COMMANDS.add(STCommand::register);
      COMMANDS.add(TPSECommand::register);
      COMMANDS.add(STPathCommand::register);
   }
}

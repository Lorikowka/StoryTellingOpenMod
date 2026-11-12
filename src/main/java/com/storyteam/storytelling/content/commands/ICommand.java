package com.storyteam.storytelling.content.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.storyteam.storytelling.api.itf.IStoryServer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

public interface ICommand {
   void registerCommand(ICommand.ICommandRegistry var1);

   public interface ICommandRegistry {
      MinecraftServer getServer();

      default IStoryServer getStoryServer() {
         return (IStoryServer)this.getServer();
      }

      default void init() {
         this.getStoryServer().getScriptEnv().loadScripts();
      }

      CommandBuildContext buildContext();

      LiteralCommandNode<CommandSourceStack> register(LiteralArgumentBuilder<CommandSourceStack> var1);
   }
}

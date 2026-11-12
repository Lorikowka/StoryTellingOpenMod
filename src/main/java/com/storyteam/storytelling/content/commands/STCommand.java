package com.storyteam.storytelling.content.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.network.StoryServer;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.util.java.Cast;

public class STCommand {
   public static void gatherSubcommands(CommandBuildContext ctx, Consumer<ArgumentBuilder<CommandSourceStack, ?>> register) {
      register.accept(createInvulfix());
      register.accept(createStatus());
      register.accept(createReconnect());
      Storytelling.BUS.post(new GatherSTSubcommandsEvent(ctx, register));
   }

   public static void sendSuccess(CommandContext<CommandSourceStack> ctx, Component com, boolean allowLogging) {
      ((CommandSourceStack)ctx.getSource()).sendSuccess(Cast.constant(com), allowLogging);
   }

   public static void register(ICommand.ICommandRegistry registry) {
      LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("st");
      gatherSubcommands(registry.buildContext(), command::then);
      registry.register(command);
   }

   public static ArgumentBuilder<CommandSourceStack, ?> createInvulfix() {
      return Commands.literal("invulfix").executes(cs -> {
         ((CommandSourceStack)cs.getSource()).getPlayerOrException().setInvulnerable(false);
         return 1;
      });
   }

   public static ArgumentBuilder<CommandSourceStack, ?> createStatus() {
      return Commands.literal("status")
         .executes(
            cs -> {
               List<Component> output = new ArrayList<>();
               ScriptEnvironment env = IStoryServer.get().getScriptEnv();
               List<Entry<Constructor<? extends ScriptInstance>>> allScripts = env.constructors
                  .int2ObjectEntrySet()
                  .stream()
                  .sorted(Comparator.comparingInt(Entry::getIntKey))
                  .toList();
               List<Entry<Constructor<? extends ScriptInstance>>> activeScripts = allScripts.stream()
                  .filter(e -> env.scriptInstances.containsKey(e.getIntKey()))
                  .toList();
               output.add(Component.literal("§6§l[§e§l>§6§l] §rСтатус API:"));
               if (allScripts.isEmpty()) {
                  output.add(Component.literal("§6|§r     Отсутствуют инициализированные скрипты."));
               } else {
                  output.add(Component.literal("§6|§r     Активные скрипты [" + activeScripts.size() + "/" + allScripts.size() + "]:"));

                  for (Entry<Constructor<? extends ScriptInstance>> entry : activeScripts) {
                     ScriptPerformer performer = env.getScript(entry.getIntKey()).getMainPerformer();
                     String info = "§6|§r     "
                        + (entry.getIntKey() >= 0 ? "§e" : "§8")
                        + "#"
                        + entry.getIntKey()
                        + " §7⇒§r "
                        + (performer == null ? "Отсутствует главный исполнитель." : performer.gatherHelpReport());
                     output.add(Component.literal(info));
                  }
               }

               for (Component entry : output) {
                  sendSuccess(cs, entry, true);
               }

               return 1;
            }
         );
   }

   public static ArgumentBuilder<CommandSourceStack, ?> createReconnect() {
      return Commands.literal("reconnect")
         .executes(
            cs -> {
               sendSuccess(cs, Component.literal("§6§l[§e§l>§6§l] §rПопытка соединения с сервером!"), true);
               StoryServer.getInstance().disconnect();
               StoryServer.getInstance()
                  .connect()
                  .whenComplete((result, throwable) -> sendSuccess(cs, Component.literal("§6§l[§e§l>§6§l] §rСоединение с сервером установлено!"), true));
               return 1;
            }
         );
   }
}

package com.storyteam.storytelling.system.simulator.me_tab.word;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.simulator.me_tab.word.data.AnimatedWordData;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.util.java.Cast;

public class WordQueueCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return Commands.literal("me")
         .then(
            ((LiteralArgumentBuilder)Commands.literal("word_queue").then(Commands.literal("list").executes(ctx -> {
                  CommandSourceStack source = (CommandSourceStack)ctx.getSource();
                  Collection<AnimatedWordData> queue = AnimatedWordQueue.getQueue();
                  if (queue.isEmpty()) {
                     source.sendSuccess(Cast.constant(Component.literal("Очередь пуста.")), false);
                     return 1;
                  } else {
                     int i = 1;

                     for (AnimatedWordData data : queue) {
                        source.sendSuccess(Cast.constant(Component.literal(i++ + ") " + data.toString())), false);
                     }

                     source.sendSuccess(Cast.constant(Component.literal("Всего элементов: " + queue.size())), false);
                     return 1;
                  }
               })))
               .then(
                  Commands.literal("clear")
                     .executes(
                        ctx -> {
                           int size = AnimatedWordQueue.getQueue().size();
                           AnimatedWordQueue.getQueue().clear();
                           ((CommandSourceStack)ctx.getSource())
                              .sendSuccess(Cast.constant(Component.literal("Очищено " + size + " элементов из очереди AnimatedWordQueue.")), false);
                           return 1;
                        }
                     )
               )
         );
   }
}

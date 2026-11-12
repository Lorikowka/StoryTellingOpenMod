package com.storyteam.storytelling.system.time.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.time.data.TimeOfDay;
import com.storyteam.storytelling.system.time.event.NewDayStartEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.util.java.Cast;

public class TimeSubCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static ArgumentBuilder<CommandSourceStack, ?> create() {
      return ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("time")
                     .then(
                        ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("set").then(Commands.literal("day").then(setCurrentDay())))
                              .then(Commands.literal("time").then(setTime())))
                           .then(Commands.literal("speed").then(setSpeed()))
                     ))
                  .then(Commands.literal("add").then(Commands.literal("day").then(addCurrentDay()))))
               .then(((LiteralArgumentBuilder)Commands.literal("state").then(statePause())).then(stateResume())))
            .then(getCurrentDayTime()))
         .then(newDayStart());
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setCurrentDay() {
      return Commands.argument("day", IntegerArgumentType.integer())
         .executes(
            context -> {
               CommandSourceStack source = (CommandSourceStack)context.getSource();
               int newCurrentDay = (Integer)context.getArgument("day", Integer.class);
               TimeController.get().setCurrentDay(newCurrentDay);
               source.sendSuccess(
                  Cast.constant(
                     Component.empty()
                        .append(Component.literal("Текущий день изменён на "))
                        .append(Component.literal(String.valueOf(newCurrentDay)).withStyle(ChatFormatting.YELLOW))
                  ),
                  true
               );
               return 1;
            }
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> addCurrentDay() {
      return Commands.argument("day", IntegerArgumentType.integer())
         .executes(
            context -> {
               CommandSourceStack source = (CommandSourceStack)context.getSource();
               int newCurrentDay = (Integer)context.getArgument("day", Integer.class);
               TimeController.get().setCurrentDay(TimeController.get().getCurrentDay() + newCurrentDay);
               source.sendSuccess(
                  Cast.constant(
                     Component.empty()
                        .append(Component.literal("Ко дню добавлено "))
                        .append(Component.literal(String.valueOf(newCurrentDay)).withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(", новое значение дня "))
                        .append(Component.literal(String.valueOf(TimeController.get().getCurrentDay())).withStyle(ChatFormatting.YELLOW))
                  ),
                  true
               );
               return 1;
            }
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setTime() {
      return Commands.argument("time", StringArgumentType.greedyString())
         .executes(
            context -> {
               CommandSourceStack source = (CommandSourceStack)context.getSource();
               String newTime = (String)context.getArgument("time", String.class);
               TimeController.get().setTime(TimeOfDay.parse(newTime).time());
               source.sendSuccess(
                  Cast.constant(
                     Component.empty().append(Component.literal("Время изменено на ")).append(Component.literal(newTime).withStyle(ChatFormatting.YELLOW))
                  ),
                  true
               );
               return 1;
            }
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> setSpeed() {
      return Commands.argument("speed", DoubleArgumentType.doubleArg())
         .executes(
            context -> {
               CommandSourceStack source = (CommandSourceStack)context.getSource();
               Double newSpeed = (Double)context.getArgument("speed", Double.class);
               TimeController.get().setTaskSpeedFactor(newSpeed);
               source.sendSuccess(
                  Cast.constant(
                     Component.empty()
                        .append(Component.literal("Скорость хода времени изменена на "))
                        .append(Component.literal(String.valueOf(newSpeed)).withStyle(ChatFormatting.YELLOW))
                  ),
                  true
               );
               return 1;
            }
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> statePause() {
      return Commands.literal("pause")
         .executes(
            context -> {
               CommandSourceStack source = (CommandSourceStack)context.getSource();
               String highestTaskId = TimeController.get().getHighestPriorityTaskId();
               TimeController.get().stopTimeTask();
               source.sendSuccess(
                  Cast.constant(
                     Component.empty()
                        .append(Component.literal("Воркер "))
                        .append(Component.literal(highestTaskId).withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(" приостановлен"))
                  ),
                  true
               );
               return 1;
            }
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> stateResume() {
      return Commands.literal("resume")
         .executes(
            context -> {
               CommandSourceStack source = (CommandSourceStack)context.getSource();
               String highestTaskId = TimeController.get().getHighestPriorityTaskId();
               TimeController.get().resumeTimeTask();
               TimeController.get().unlockTime(false);
               source.sendSuccess(
                  Cast.constant(
                     Component.empty()
                        .append(Component.literal("Воркер "))
                        .append(Component.literal(highestTaskId).withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(" возобновлён"))
                  ),
                  true
               );
               return 1;
            }
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> getCurrentDayTime() {
      return Commands.literal("get")
         .executes(
            context -> {
               CommandSourceStack source = (CommandSourceStack)context.getSource();
               int currentDay = TimeController.get().getCurrentDay();
               String time = new TimeOfDay(TimeController.get().getTime()).getAsString();
               Component dayTime = Component.empty()
                  .append(Component.literal("Время: "))
                  .append(Component.literal(time).withStyle(ChatFormatting.YELLOW))
                  .append(Component.literal("\n"))
                  .append(Component.literal("Текущий день: "))
                  .append(Component.literal(String.valueOf(currentDay)).withStyle(ChatFormatting.YELLOW));
               source.sendSuccess(Cast.constant(dayTime), true);
               return 1;
            }
         );
   }

   private static ArgumentBuilder<CommandSourceStack, ?> newDayStart() {
      return Commands.literal("new_day_start")
         .executes(
            context -> {
               CommandSourceStack source = (CommandSourceStack)context.getSource();
               int previousDay = TimeController.get().getCurrentDay();
               NewDayStartEvent event = new NewDayStartEvent(previousDay, previousDay);
               MinecraftForge.EVENT_BUS.post(event);
               IStoryServer.get().postStoryEvent(EventType.NEW_DAY_START, event);
               Component eventTriggered = Component.empty()
                  .append(Component.literal("Ивент "))
                  .append(Component.literal("NewDayStartEvent").withStyle(ChatFormatting.YELLOW))
                  .append(Component.literal(" вызван"));
               source.sendSuccess(Cast.constant(eventTriggered), true);
               return 1;
            }
         );
   }
}

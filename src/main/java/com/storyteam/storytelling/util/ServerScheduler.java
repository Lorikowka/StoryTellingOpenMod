package com.storyteam.storytelling.util;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system._core.cloud.CloudIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionException;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ServerScheduler {
   private static final List<ServerScheduler.Task> allTasksQueue = new ArrayList<>();
   private static final List<ServerScheduler.Task> allTasks = new ArrayList<>();

   public static void schedule(int ticks, Runnable task) {
      allTasksQueue.add(new ServerScheduler.Task(ticks, task));
   }

   @SubscribeEvent
   public static void serverTick(ServerTickEvent e) {
      if (e.phase == Phase.START) {
         while (!allTasksQueue.isEmpty()) {
            allTasks.add(allTasksQueue.remove(0));
         }

         allTasks.removeIf(task -> task == null || task.tick());
      }
   }

   @SubscribeEvent
   public static void refresh(ServerStartingEvent e) {
      try {
         CloudIO.createReload().join();
      } catch (CompletionException var2) {
         Storytelling.LOG.fatal("Failed to connect to cloud!", var2);
      }
   }

   @SubscribeEvent
   public static void reset(ServerStoppingEvent e) {
      allTasks.clear();
   }

   public static void executeWhen(BooleanSupplier condition, Runnable futureTask, int timeLimit) {
      allTasksQueue.add(new ServerScheduler.FutureTask(condition, futureTask, timeLimit));
   }

   public static void executeWhen(BooleanSupplier condition, Runnable futureTask, int timeLimit, int checkDelay) {
      allTasksQueue.add(new ServerScheduler.FutureTask(condition, futureTask, timeLimit, checkDelay));
   }

   public static void executeWhile(BooleanSupplier value, boolean expect) {
      allTasksQueue.add(new ServerScheduler.RepeatableTask<>(value::getAsBoolean, expect));
   }

   public static void executeWhileI(IntPredicate value, boolean expect) {
      allTasksQueue.add(new ServerScheduler.RepeatableTickTask<>(value::test, expect));
   }

   private static class FutureTask extends ServerScheduler.Task {
      BooleanSupplier condition;
      int delay;
      final int initialTime;

      public FutureTask(BooleanSupplier condition, Runnable task, int timeLimit) {
         super(timeLimit, task);
         this.condition = condition;
         this.delay = 1;
         this.initialTime = timeLimit;
      }

      public FutureTask(BooleanSupplier condition, Runnable task, int timeLimit, int delay) {
         super(timeLimit, task);
         this.condition = condition;
         this.delay = delay;
         this.initialTime = timeLimit;
      }

      @Override
      public boolean tick() {
         if ((this.initialTime - this.ticks) % this.delay == 0 && this.condition.getAsBoolean()) {
            this.task.run();
            return true;
         } else {
            return --this.ticks <= 0;
         }
      }
   }

   private static class RepeatableTask<T> extends ServerScheduler.Task {
      Supplier<T> get;
      T expect;

      public RepeatableTask(Supplier<T> get, T expect) {
         super(0, () -> {
         });
         this.get = get;
         this.expect = expect;
      }

      @Override
      public boolean tick() {
         return !Objects.equals(this.get.get(), this.expect);
      }
   }

   private static class RepeatableTickTask<T> extends ServerScheduler.Task {
      int ticks;
      Function<Integer, T> get;
      T expect;

      public RepeatableTickTask(Function<Integer, T> get, T expect) {
         super(0, () -> {
         });
         this.get = get;
         this.expect = expect;
      }

      @Override
      public boolean tick() {
         this.ticks++;
         return !Objects.equals(this.get.apply(this.ticks), this.expect);
      }
   }

   private static class Task {
      int ticks;
      Runnable task;

      public Task(int ticks, Runnable task) {
         this.ticks = ticks;
         this.task = task;
      }

      public boolean tick() {
         if (this.ticks == 0) {
            this.task.run();
         }

         return --this.ticks < 0;
      }
   }
}

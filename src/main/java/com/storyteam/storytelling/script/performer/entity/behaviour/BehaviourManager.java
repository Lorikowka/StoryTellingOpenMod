package com.storyteam.storytelling.script.performer.entity.behaviour;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.base.BehaviourEvent;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.Cast;

public class BehaviourManager<T extends Entity> implements Cloneable {
   private final ConcurrentHashMap<BehaviourType, IStoryBehaviour<T>> behaviours = new ConcurrentHashMap<>();
   private final HashMap<BehaviourType, BehaviourManager.BehaviorChange<T>> tasks = new HashMap<>();
   private final Stack<BehaviourManager.BehaviorChange<T>> interruptActions = new Stack<>();
   private IStoryBehaviour<T> interrupter;

   public void continueBehaviours(@NotNull IStoryBehaviour<T> interrupter) {
      BehaviourManager.BehaviorChange<T> change = new BehaviourManager.BehaviorChange<>();
      this.interruptActions.add(change.continueBehaviors(interrupter));
   }

   public void interruptBehaviours(@NotNull IStoryBehaviour<T> interrupter) {
      BehaviourManager.BehaviorChange<T> change = new BehaviourManager.BehaviorChange<>();
      this.interruptActions.add(change.interruptBehaviors(interrupter));
   }

   public void addBehaviour(@NotNull IStoryBehaviour<T> behaviour) {
      this.tasks.putIfAbsent(behaviour.getType(), new BehaviourManager.BehaviorChange<>());
      this.tasks.computeIfPresent(behaviour.getType(), (t, c) -> c.addBehavior(behaviour));
   }

   public void stopBehaviour(@NotNull BehaviourType type) {
      this.tasks.putIfAbsent(type, new BehaviourManager.BehaviorChange<>());
      this.tasks.computeIfPresent(type, (t, c) -> c.removeBehavior());
   }

   @Nullable
   public IStoryBehaviour<T> getBehavior(BehaviourType type) {
      return this.behaviours.get(type);
   }

   public void clearBehaviours() {
      this.behaviours.keySet().forEach(this::stopBehaviour);
      this.tasks.keySet().forEach(this::stopBehaviour);
   }

   public void onStopBehaviours(ScriptEnvironment environment, EntityContext<T> context) {
      this.behaviours.values().forEach(b -> b.onStop(environment, context));
   }

   public void tick(@NotNull IStoryServer server, @NotNull EntityContext<T> context) {
      this.behaviours.forEach((type, behaviour) -> {
         if (behaviour.canStillUse(server.getScriptEnv(), context)) {
            if (behaviour instanceof InterruptableBehaviour<T> i && i.isInterrupted()) {
               return;
            }

            behaviour.tickEntity(server.getScriptEnv(), context);
         } else {
            if (this.interrupter == behaviour) {
               this.continueBehaviours(this.interrupter);
            }

            behaviour.onStop(server.getScriptEnv(), context);
            this.behaviours.remove(type);
            MinecraftForge.EVENT_BUS.post(new BehaviourEvent.BehaviourStopEvent<>((IStoryBehaviour<T>)behaviour, context));
            server.postStoryEvent(EventType.BEHAVIOUR_END, new BehaviourEvent.BehaviourStopEvent<>((IStoryBehaviour<T>)behaviour, context));
         }
      });
   }

   public void processBehaviourChanges(@NotNull EntityContext<T> context) {
      if (!this.tasks.isEmpty()) {
         Iterator<Entry<BehaviourType, BehaviourManager.BehaviorChange<T>>> iterator = this.tasks.entrySet().iterator();

         while (iterator.hasNext()) {
            Entry<BehaviourType, BehaviourManager.BehaviorChange<T>> entry = iterator.next();
            iterator.remove();
            BehaviourManager.BehaviorChange<T> change = entry.getValue();
            BehaviourType type = entry.getKey();
            change.process(this, type, context);
         }
      }

      while (!this.interruptActions.isEmpty()) {
         BehaviourManager.BehaviorChange<T> change = this.interruptActions.pop();
         if (change.behaviour != null) {
            change.process(this, change.behaviour.getType(), context);
         }
      }
   }

   public ImmutableMap<BehaviourType, IStoryBehaviour<T>> getBehaviours() {
      return ImmutableMap.copyOf(this.behaviours);
   }

   public <E> void handleEvent(EventType<E> type, E event, @NotNull EntityContext<T> context) {
      this.behaviours.values().forEach(b -> b.handleEvent(type, event, context));
   }

   public BehaviourManager<T> clone() {
      try {
         BehaviourManager<T> clone = (BehaviourManager<T>)super.clone();
         clone.interrupter = this.interrupter;
         clone.behaviours.putAll(this.behaviours);
         clone.interruptActions.addAll(this.interruptActions);
         clone.tasks.putAll(this.tasks);
         return clone;
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError();
      }
   }

   private static class BehaviorChange<T extends Entity> {
      @Nullable
      private IStoryBehaviour<T> behaviour;
      private final Stack<BehaviourManager.BehaviorChangeAction> action = new Stack<>();

      @Nullable
      public IStoryBehaviour<T> getBehaviour() {
         return this.behaviour;
      }

      public BehaviourManager.BehaviorChange<T> addBehavior(@NotNull IStoryBehaviour<T> behaviour) {
         this.action.clear();
         this.action.add(BehaviourManager.BehaviorChangeAction.INTERRUPT);
         this.action.add(BehaviourManager.BehaviorChangeAction.ADD);
         this.behaviour = behaviour;
         return this;
      }

      public BehaviourManager.BehaviorChange<T> removeBehavior() {
         this.action.clear();
         this.action.add(BehaviourManager.BehaviorChangeAction.REMOVE);
         this.action.add(BehaviourManager.BehaviorChangeAction.CONTINUE);
         return this;
      }

      public BehaviourManager.BehaviorChange<T> interruptBehaviors(IStoryBehaviour<T> behaviour) {
         this.action.clear();
         this.action.add(BehaviourManager.BehaviorChangeAction.INTERRUPT_ALL);
         this.behaviour = behaviour;
         return this;
      }

      public BehaviourManager.BehaviorChange<T> continueBehaviors(IStoryBehaviour<T> behaviour) {
         this.action.clear();
         this.action.add(BehaviourManager.BehaviorChangeAction.CONTINUE);
         this.behaviour = behaviour;
         return this;
      }

      public void process(BehaviourManager<T> manager, BehaviourType type, EntityContext<T> context) {
         while (!this.action.isEmpty()) {
            this.action.pop().accept(manager, type, this.behaviour, context);
         }
      }
   }

   private enum BehaviorChangeAction {
      ADD, REMOVE, INTERRUPT, INTERRUPT_ALL, CONTINUE;

      public <E extends Entity> void accept(BehaviourManager<E> manager, BehaviourType type, IStoryBehaviour<E> behaviour, EntityContext<E> context) {
         ScriptEnvironment env = context.getState().getScript().env;
         switch (this) {
            case ADD -> {
               if (behaviour == null) return;
               IStoryBehaviour<E> old = manager.behaviours.get(type);
               if (old != null) {
                  old.onStop(env, context);
                  env.server.postStoryEvent(EventType.BEHAVIOUR_END, new BehaviourEvent.BehaviourStopEvent<>(old, context));
                  MinecraftForge.EVENT_BUS.post(new BehaviourEvent.BehaviourStopEvent<>(old, context));
               }
               behaviour.onStart(env, context);
               env.server.postStoryEvent(EventType.BEHAVIOUR_START, new BehaviourEvent.BehaviourStartEvent<>(behaviour, context));
               MinecraftForge.EVENT_BUS.post(new BehaviourEvent.BehaviourStartEvent<>(behaviour, context));
               manager.behaviours.put(type, behaviour);
            }
            case REMOVE -> {
               IStoryBehaviour<E> old = manager.behaviours.remove(type);
               if (old != null) {
                  old.onStop(env, context);
                  env.server.postStoryEvent(EventType.BEHAVIOUR_END, new BehaviourEvent.BehaviourStopEvent<>(old, context));
                  MinecraftForge.EVENT_BUS.post(new BehaviourEvent.BehaviourStopEvent<>(old, context));
               }
            }
            case INTERRUPT_ALL -> {
               if (behaviour != null && manager.interrupter == null) {
                  manager.interrupter = behaviour;
                  for (IStoryBehaviour<E> current : new ArrayList<>(manager.behaviours.values())) {
                     if (current != behaviour && current instanceof InterruptableBehaviour<E> interruptible && !interruptible.isInterrupted())
                        interruptible.onInterrupt(env, context);
                  }
               }
            }
            case CONTINUE -> {
               IStoryBehaviour<E> currentInterrupter = behaviour != null ? behaviour : manager.getBehavior(type);
               if (currentInterrupter != null && manager.interrupter == currentInterrupter) {
                  manager.interrupter = null;
                  for (IStoryBehaviour<E> current : new ArrayList<>(manager.behaviours.values())) {
                     if (current instanceof InterruptableBehaviour<E> interruptible && interruptible.isInterrupted())
                        interruptible.onContinue(env, context);
                  }
               }
            }
            case INTERRUPT -> { }
         }
      }
   }
}


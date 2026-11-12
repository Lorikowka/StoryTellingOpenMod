package com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightToken;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.Cast;

public class AttackManager<T extends AnimatedEntity> {
   private final HashMap<String, AttackManager.Ticker> actions = new HashMap<>();
   final Stack<Attack<T>> offeredAttacks = new Stack<>();
   public Attack<T> activeAttack = null;
   private final HashMap<String, Object> savedData = new HashMap<>();
   boolean isCancelled;

   public void offerAttack(Attack<T> attack) {
      this.offeredAttacks.push(attack);
   }

   public void cancel() {
      this.isCancelled = true;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   @Nullable
   public Attack<T> nextOffered() {
      return this.offeredAttacks.isEmpty() ? null : this.offeredAttacks.pop();
   }

   public void setTimer(String id, int ticks) {
      this.actions.put(id, new AttackManager.DelayedAction(ticks, () -> {
      }));
   }

   public void setDelayedAction(String id, int ticks, Runnable action) {
      this.actions.put(id, new AttackManager.DelayedAction(ticks, action));
   }

   public void setTickingAction(String id, int ticks, Consumer<Integer> action) {
      this.actions.put(id, new AttackManager.TickingAction(ticks, action));
   }

   public void doOnce(String id, Runnable action) {
      if (!this.actions.containsKey(id)) {
         action.run();
         this.addFlag(id);
      }
   }

   public void addDelayedAction(String id, int ticks, Runnable action) {
      if (!this.actions.containsKey(id)) {
         this.actions.put(id, new AttackManager.DelayedAction(ticks, action));
      }
   }

   public void addFlag(String id) {
      if (!this.actions.containsKey(id)) {
         this.actions.put(id, new AttackManager.DelayedAction(0, () -> {
         }));
      }
   }

   public void addTimer(String id, int ticks) {
      if (!this.actions.containsKey(id)) {
         this.actions.put(id, new AttackManager.DelayedAction(ticks, () -> {
         }));
      }
   }

   public void addTickingAction(String id, int ticks, Consumer<Integer> action) {
      if (!this.actions.containsKey(id)) {
         this.actions.put(id, new AttackManager.TickingAction(ticks, action));
      }
   }

   public boolean containsCompleted(String id) {
      return this.actions.get(id) != null && this.actions.get(id).isFinished();
   }

   public boolean contains(String id) {
      return this.actions.containsKey(id);
   }

   public void remove(String id) {
      this.actions.remove(id);
   }

   public void clearGarbage() {
      this.actions.values().removeIf(AttackManager.Ticker::isFinished);
   }

   public boolean allCompleted() {
      return this.actions.isEmpty();
   }

   public void tick() {
      new HashMap<>(this.actions).values().forEach(AttackManager.Ticker::tick);
   }

   public void tick(boolean remove) {
      if (remove) {
         this.actions.values().removeIf(ticker -> {
            ticker.tick();
            return ticker.isFinished();
         });
      } else {
         this.tick();
      }
   }

   public void reset() {
      this.isCancelled = false;
      this.actions.clear();
      this.savedData.clear();
   }

   public <O> boolean containsToken(FightToken<O> token) {
      return this.savedData.containsKey(token.toString());
   }

   public <O> void saveIfAbsent(FightToken<O> token, O data) {
      this.savedData.putIfAbsent(token.getName(), data);
   }

   public <O> void setData(FightToken<O> token, O data) {
      this.savedData.put(token.getName(), data);
   }

   public <O> void compute(FightToken<O> token, UnaryOperator<O> operation) {
      this.savedData.computeIfPresent(token.getName(), (s, o) -> operation.apply((O)Cast.cast(o, token.getType())));
   }

   public <O> O getData(FightToken<O> token) {
      return (O)Cast.cast(this.savedData.get(token.getName()), token.getType());
   }

   private static class DelayedAction implements AttackManager.Ticker {
      int counter;
      final Runnable runnable;

      public DelayedAction(int ticks, Runnable runnable) {
         this.counter = ticks;
         this.runnable = runnable;
      }

      @Override
      public void tick() {
         if (!this.isFinished()) {
            this.counter--;
            if (this.counter < 0) {
               this.runnable.run();
            }
         }
      }

      @Override
      public boolean isFinished() {
         return this.counter < 0;
      }
   }

   private interface Ticker {
      void tick();

      boolean isFinished();
   }

   private static class TickingAction implements AttackManager.Ticker {
      int counter;
      final Consumer<Integer> action;

      public TickingAction(int ticks, Consumer<Integer> action) {
         this.counter = ticks;
         this.action = action;
      }

      @Override
      public void tick() {
         if (!this.isFinished()) {
            this.counter--;
            if (this.counter >= 0) {
               this.action.accept(this.counter);
            }
         }
      }

      @Override
      public boolean isFinished() {
         return this.counter <= 0;
      }
   }
}

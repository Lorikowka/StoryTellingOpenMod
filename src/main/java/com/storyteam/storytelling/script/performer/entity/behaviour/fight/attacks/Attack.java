package com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.FightEventBehaviour;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightContext;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import net.minecraft.server.MinecraftServer;

public class Attack<T extends AnimatedEntity> {
   private final AttackAction<T> action;
   private AttackAction<T> tryCompleteConditions;
   private final int cooldown;
   private int lastTimeUse = 0;
   private final ArrayList<FightEventBehaviour<?, T>> attackHandlers = new ArrayList<>();
   private FightCondition<T> condition = context -> true;
   public String id;

   public Attack(String id, int cooldown, AttackAction<T> action) {
      this.action = (ticker, context) -> {
         this.lastTimeUse = ((MinecraftServer)IStoryServer.get()).getTickCount();
         return action.doAttack(ticker, context);
      };
      this.cooldown = cooldown;
      this.id = id;
   }

   public Attack(String id, int cooldown, AttackAction<T> action, FightCondition<T> condition) {
      this.action = (ticker, context) -> {
         this.lastTimeUse = ((MinecraftServer)IStoryServer.get()).getTickCount();
         return action.doAttack(ticker, context);
      };
      this.cooldown = cooldown;
      this.condition = condition;
      this.id = id;
   }

   public Attack(String id, int cooldown, AttackAction<T> action, FightCondition<T> condition, AttackAction<T> tryMakeConditions) {
      this.action = (ticker, context) -> {
         this.lastTimeUse = ((MinecraftServer)IStoryServer.get()).getTickCount();
         return action.doAttack(ticker, context);
      };
      this.cooldown = cooldown;
      this.condition = condition;
      this.tryCompleteConditions = tryMakeConditions;
      this.id = id;
   }

   public boolean canCompleteConditions() {
      int ticks = ((MinecraftServer)IStoryServer.get()).getTickCount();
      return ticks - this.cooldown >= this.lastTimeUse && this.tryCompleteConditions != null;
   }

   public boolean tryCompleteConditions(AttackManager<T> scheduler, EntityContext<T> context) {
      return this.tryCompleteConditions.doAttack(scheduler, context);
   }

   public AttackAction<T> getAction() {
      return this.action;
   }

   public int getCooldown() {
      return this.cooldown;
   }

   public FightCondition<T> getCondition() {
      int ticks = ((MinecraftServer)IStoryServer.get()).getTickCount();
      return this.condition.and(s -> ticks - this.cooldown >= this.lastTimeUse);
   }

   public <E> void handleEvent(EventType<E> type, E data, FightContext<T> context) {
      for (FightEventBehaviour<?, T> handler : this.attackHandlers) {
         if (handler.getType() == type) {
            @SuppressWarnings("unchecked")
            FightEventBehaviour<E, T> matching = (FightEventBehaviour<E, T>) handler;
            matching.handle(data, context);
         }
      }
   }

   public static <T extends AnimatedEntity> Attack.AttackBuilder<T> builder(String id, int cooldown, AttackAction<T> action) {
      return new Attack.AttackBuilder<>(id, cooldown, action);
   }

   public static class AttackBuilder<T extends AnimatedEntity> {
      private final int cooldown;
      public String id;
      private final AttackAction<T> action;
      private final ArrayList<FightEventBehaviour<?, T>> attackHandlers = new ArrayList<>();
      private AttackAction<T> tryCompleteConditions = null;
      private FightCondition<T> condition = context -> true;

      AttackBuilder(String id, int cooldown, AttackAction<T> action) {
         this.id = id;
         this.cooldown = cooldown;
         this.action = action;
      }

      public <E> Attack.AttackBuilder<T> addHandler(EventType<E> type, BiConsumer<E, FightContext<T>> event) {
         FightEventBehaviour<E, T> handler = new FightEventBehaviour<>(type, event);
         this.attackHandlers.add(handler);
         return this;
      }

      public Attack.AttackBuilder<T> addCondition(FightCondition<T> condition) {
         this.condition = condition;
         return this;
      }

      public Attack.AttackBuilder<T> tryCompleteConditions(AttackAction<T> tryCompleteConditions) {
         this.tryCompleteConditions = tryCompleteConditions;
         return this;
      }

      public Attack<T> build() {
         Attack<T> attack = new Attack<>(this.id, this.cooldown, this.action, this.condition, this.tryCompleteConditions);
         attack.attackHandlers.addAll(this.attackHandlers);
         return attack;
      }
   }
}

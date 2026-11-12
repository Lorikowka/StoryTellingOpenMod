package com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.FightEventBehaviour;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightContext;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.FightCondition;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.data_structure.WeightedList;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.Generated;

public class FightStage<T extends AnimatedEntity> {
   Consumer<EntityContext<T>> onAchieved = context -> {
   };
   final FightCondition<T> startCondition;
   FightCondition<T> returnCondition = context -> false;
   final WeightedList<Attack<T>> attacks = new WeightedList<>();
   final ArrayList<FightEventBehaviour<?, T>> handlers = new ArrayList<>();

   public FightStage(FightCondition<T> condition) {
      this.startCondition = condition;
   }

   public FightStage(FightCondition<T> condition, Consumer<EntityContext<T>> onAchieved) {
      this.startCondition = condition;
      this.onAchieved = onAchieved;
   }

   public FightStage(FightCondition<T> condition, Consumer<EntityContext<T>> onAchieved, FightCondition<T> returnCondition) {
      this.startCondition = condition;
      this.onAchieved = onAchieved;
      this.returnCondition = returnCondition;
   }

   public void addAttack(Attack<T> attack, double weight) {
      this.attacks.add(attack, weight);
   }

   public <E> void handleEvent(EventType<E> type, E event, FightContext<T> context) {
      for (FightEventBehaviour<?, T> handler : this.handlers) {
         if (handler.getType() == type) {
            @SuppressWarnings("unchecked") FightEventBehaviour<E, T> matching = (FightEventBehaviour<E, T>) handler;
            matching.handle(event, context);
         }
      }
   }

   public <E> void addEvent(EventType<E> type, BiConsumer<E, FightContext<T>> action) {
      this.handlers.add(new FightEventBehaviour<>(type, action));
   }

   @Generated
   public Consumer<EntityContext<T>> getOnAchieved() {
      return this.onAchieved;
   }

   @Generated
   public FightCondition<T> getStartCondition() {
      return this.startCondition;
   }

   @Generated
   public FightCondition<T> getReturnCondition() {
      return this.returnCondition;
   }

   @Generated
   public WeightedList<Attack<T>> getAttacks() {
      return this.attacks;
   }

   @Generated
   public ArrayList<FightEventBehaviour<?, T>> getHandlers() {
      return this.handlers;
   }
}

package com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightContext;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.FightCondition;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StagedAttackChainBuilder<T extends AnimatedEntity> {
   private final ArrayList<FightStage<T>> savedStages = new ArrayList<>();
   private FightStage<T> currentStage = new FightStage<>(c -> true);
   FightCondition<T> stopCondition = context -> false;

   public <E> StagedAttackChainBuilder<T> handleEvent(EventType<E> type, BiConsumer<E, FightContext<T>> event) {
      this.currentStage.addEvent(type, event);
      return this;
   }

   public StagedAttackChainBuilder<T> stopCondition(FightCondition<T> condition) {
      this.stopCondition = condition;
      return this;
   }

   public StagedAttackChainBuilder<T> addAttack(double weight, Attack<T> attack) {
      this.currentStage.addAttack(attack, weight);
      return this;
   }

   public StagedAttackChainBuilder<T> nextStage(FightCondition<T> endCondition) {
      this.savedStages.add(this.currentStage);
      this.currentStage = new FightStage<>(endCondition);
      return this;
   }

   public StagedAttackChainBuilder<T> nextStage(FightCondition<T> endCondition, Consumer<EntityContext<T>> onAchieved) {
      this.savedStages.add(this.currentStage);
      this.currentStage = new FightStage<>(endCondition, onAchieved);
      return this;
   }

   public StagedAttackChainBuilder<T> nextStage(FightCondition<T> endCondition, Consumer<EntityContext<T>> onAchieved, FightCondition<T> returnCondition) {
      this.savedStages.add(this.currentStage);
      this.currentStage = new FightStage<>(endCondition, onAchieved, returnCondition);
      return this;
   }

   public IAttackChain<T> build() {
      this.savedStages.add(this.currentStage);
      StagedAttackChain<T> chain = new StagedAttackChain<>(this.savedStages);
      chain.setExitCondition(this.stopCondition);
      return chain;
   }
}

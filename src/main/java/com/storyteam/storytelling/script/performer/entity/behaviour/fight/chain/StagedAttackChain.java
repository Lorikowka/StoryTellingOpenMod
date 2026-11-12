package com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightContext;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.FightCondition;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.data_structure.WeightedList;
import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;

public class StagedAttackChain<T extends AnimatedEntity> implements IAttackChain<T> {
   private final ArrayList<FightStage<T>> stages;
   int stageLevel;
   private FightStage<T> savedStage;
   FightCondition<T> shouldStop = context -> false;

   StagedAttackChain(ArrayList<FightStage<T>> stages) {
      this.stages = stages;
   }

   @Override
   public boolean isFightActive(EntityContext<T> context) {
      return !this.shouldStop.test(context);
   }

   @Override
   public void setExitCondition(FightCondition<T> condition) {
      this.shouldStop = condition;
   }

   @Override
   public <E> void handleEvent(EventType<E> type, E data, FightContext<T> context) {
      this.getSavedStage().handleEvent(type, data, context);
   }

   @Override
   public boolean onStartOfferedAttack(Attack<T> attack) {
      return true;
   }

   @Nullable
   @Override
   public Attack<T> getAttackAndTick(EntityContext<T> context) {
      if (this.getSavedStage().getReturnCondition().test(context)) {
         this.stageDown();
      }

      if (this.stageLevel < this.stages.size() - 1 && this.getNextStage().getStartCondition().test(context)) {
         this.stageUp();
      }

      WeightedList<Attack<T>> filtered = this.getSavedStage().getAttacks().filtered(a -> a.getCondition().test(context));
      return filtered.isEmpty() ? null : filtered.getRandomItem(RANDOM_SOURCE);
   }

   @Nullable
   @Override
   public Attack<T> getPotentialAttack(EntityContext<T> context) {
      WeightedList<Attack<T>> filtered = this.getSavedStage().getAttacks().filtered(Attack::canCompleteConditions);
      return filtered.isEmpty() ? null : filtered.getRandomItem(RANDOM_SOURCE);
   }

   public int getStage() {
      return this.stageLevel;
   }

   public FightStage<T> getSavedStage() {
      if (this.savedStage == null) {
         this.savedStage = this.stages.get(this.stageLevel);
      }

      return this.savedStage;
   }

   @Nullable
   private FightStage<T> getNextStage() {
      return this.stageLevel + 1 < this.stages.size() ? this.stages.get(this.stageLevel + 1) : null;
   }

   public boolean stageUp() {
      int size = this.stages.size();
      if (this.stageLevel > size) {
         return false;
      } else {
         this.stageLevel++;
         this.savedStage = this.stages.get(this.stageLevel);
         return true;
      }
   }

   public boolean stageDown() {
      if (this.stageLevel == 0) {
         return false;
      } else {
         this.stageLevel--;
         this.savedStage = this.stages.get(this.stageLevel);
         return true;
      }
   }

   @Override
   public IAttackChain<T> copy() {
      StagedAttackChain<T> chain = new StagedAttackChain<>(this.stages);
      chain.stageLevel = this.stageLevel;
      chain.shouldStop = this.shouldStop;
      chain.savedStage = this.savedStage;
      return chain;
   }
}

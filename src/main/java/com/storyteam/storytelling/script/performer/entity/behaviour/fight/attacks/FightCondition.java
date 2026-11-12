package com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.util.data.EntityContext;

@FunctionalInterface
public interface FightCondition<T extends AnimatedEntity> {
   boolean test(EntityContext<T> var1);

   default FightCondition<T> and(FightCondition<T> condition) {
      return context -> this.test(context) && condition.test(context);
   }

   default FightCondition<T> or(FightCondition<T> condition) {
      return context -> this.test(context) || condition.test(context);
   }
}

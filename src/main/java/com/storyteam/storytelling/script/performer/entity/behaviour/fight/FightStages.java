package com.storyteam.storytelling.script.performer.entity.behaviour.fight;

import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.FightCondition;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain.IAttackChain;

public class FightStages {
   public static <T extends BaseMob> IAttackChain<T> createFightStage(IAttackChain<T> chain, FightCondition<T> stopCondition) {
      IAttackChain<T> attackChain = chain.copy();
      attackChain.setExitCondition(stopCondition);
      return attackChain;
   }
}

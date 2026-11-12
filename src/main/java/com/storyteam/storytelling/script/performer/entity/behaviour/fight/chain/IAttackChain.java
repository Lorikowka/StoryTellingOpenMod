package com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightContext;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.FightCondition;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public interface IAttackChain<T extends AnimatedEntity> {
   RandomSource RANDOM_SOURCE = RandomSource.create();

   boolean onStartOfferedAttack(Attack<T> var1);

   @Nullable
   Attack<T> getAttackAndTick(EntityContext<T> var1);

   @Nullable
   Attack<T> getPotentialAttack(EntityContext<T> var1);

   boolean isFightActive(EntityContext<T> var1);

   void setExitCondition(FightCondition<T> var1);

   <E> void handleEvent(EventType<E> var1, E var2, FightContext<T> var3);

   IAttackChain<T> copy();
}

package com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.util.data.EntityContext;

@FunctionalInterface
public interface AttackAction<T extends AnimatedEntity> {
   boolean doAttack(AttackManager<T> var1, EntityContext<T> var2);
}

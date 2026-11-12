package com.storyteam.storytelling.script.performer.entity.behaviour.fight;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.AttackManager;
import com.storyteam.storytelling.util.data.EntityContext;

public class FightContext<T extends AnimatedEntity> extends EntityContext<T> {
   AttackManager<T> manager;

   public FightContext(EntityContext<T> context, AttackManager<T> manager) {
      super(context.getRef(), context.getState(), (T)context.getEntity(), context.getCharacter());
      this.manager = manager;
   }

   public AttackManager<T> getManager() {
      return this.manager;
   }
}

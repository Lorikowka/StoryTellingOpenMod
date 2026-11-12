package com.storyteam.storytelling.events;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.util.data.EntityContext;
import lombok.Generated;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class StoryFightEvent<T extends AnimatedEntity> extends LivingEvent {
   protected EntityContext<T> context;
   protected Attack<T> attack;

   public StoryFightEvent(Attack<T> attack, EntityContext<T> context) {
      super((LivingEntity)context.getEntity());
      this.context = context;
      this.attack = attack;
   }

   @Generated
   public EntityContext<T> getContext() {
      return this.context;
   }

   @Generated
   public Attack<T> getAttack() {
      return this.attack;
   }
}

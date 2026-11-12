package com.storyteam.storytelling.script.performer.entity;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightContext;
import java.util.function.BiConsumer;

public class FightEventBehaviour<E, T extends AnimatedEntity> {
   final EventType<E> type;
   final BiConsumer<E, FightContext<T>> action;

   public FightEventBehaviour(EventType<E> type, BiConsumer<E, FightContext<T>> action) {
      this.action = action;
      this.type = type;
   }

   public EventType<E> getType() {
      return this.type;
   }

   public void handle(E data, FightContext<T> context) {
      this.action.accept(data, context);
   }
}

package com.storyteam.storytelling.events.base;

import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.util.data.EntityContext;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public abstract class BehaviourEvent<T extends Entity> extends Event {
   final IStoryBehaviour<T> behaviour;
   final EntityContext<T> context;

   public BehaviourEvent(IStoryBehaviour<T> behaviour, EntityContext<T> context) {
      this.behaviour = behaviour;
      this.context = context;
   }

   @Generated
   public IStoryBehaviour<T> getBehaviour() {
      return this.behaviour;
   }

   @Generated
   public EntityContext<T> getContext() {
      return this.context;
   }

   public static class BehaviourStartEvent<T extends Entity> extends BehaviourEvent<T> {
      public BehaviourStartEvent(IStoryBehaviour<T> behaviour, EntityContext<T> context) {
         super(behaviour, context);
      }
   }

   public static class BehaviourStopEvent<T extends Entity> extends BehaviourEvent<T> {
      public BehaviourStopEvent(IStoryBehaviour<T> behaviour, EntityContext<T> context) {
         super(behaviour, context);
      }
   }
}

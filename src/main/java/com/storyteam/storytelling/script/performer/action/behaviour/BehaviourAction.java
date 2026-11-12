package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.events.base.BehaviourEvent;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.EntityPerformerAction;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.world.entity.Entity;

public abstract class BehaviourAction<T extends Entity, B extends IStoryBehaviour<T>> extends EntityPerformerAction<T> {
   B behavior = (B)null;
   protected boolean completed;
   protected boolean init;
   protected boolean parallel;
   protected boolean remote;

   public BehaviourAction(ScriptPerformer performer, Character<T> character) {
      super(performer, character);
   }

   public BehaviourAction(ScriptPerformer performer, Character<T> character, boolean parallel, boolean remote) {
      super(performer, character);
      this.parallel = parallel;
      this.remote = remote;
   }

   protected Optional<EntityState<T>> optionalState() {
      return Optional.ofNullable(this.remote ? this.getActiveEntityState() : this.getEntity());
   }

   @Override
   public void tick() {
      if (!this.init) {
         this.init = true;
         this.behavior = this.createBehaviour();
         this.optionalState().ifPresent(s -> s.addBehaviour(this.behavior));
      }
   }

   @Override
   public boolean isDone() {
      return this.parallel || this.completed;
   }

   @Override
   public void rewindToStage() {
      if (this.behavior != null) {
         this.optionalState().ifPresent(s -> s.stopBehaviour(this.behavior.getType()));
      }

      this.init = false;
      this.completed = false;
   }

   @Override
   public void fastForwardToStage() {
      if (this.behavior == null) {
         this.behavior = this.createBehaviour();
      }

      this.completed = true;
      this.init = true;
      this.optionalState().ifPresent(s -> s.addBehaviour(this.behavior));
   }

   @Override
   public void reset() {
      this.init = false;
      this.completed = false;
   }

   public abstract B createBehaviour();

   @Override
   public <U> void handleEvent(EventType<U> eventType, U event) {
      if (eventType == EventType.BEHAVIOUR_END && ((BehaviourEvent.BehaviourStopEvent)event).getBehaviour() == this.behavior) {
         this.completed = true;
      }
   }

   @Override
   public void onInit() {
      this.reset();
   }

   @Generated
   public B getBehavior() {
      return this.behavior;
   }
}

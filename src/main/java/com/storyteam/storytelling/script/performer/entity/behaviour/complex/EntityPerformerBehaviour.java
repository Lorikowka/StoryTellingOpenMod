package com.storyteam.storytelling.script.performer.entity.behaviour.complex;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.interruption.InterruptionBehaviour;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityPerformerBehaviour<T extends Entity> extends InterruptionBehaviour<T> {
   final Consumer<ScriptPerformerBuilder> consumer;
   ScriptPerformer performer;

   public EntityPerformerBehaviour(Consumer<ScriptPerformerBuilder> consumer, String... layers) {
      super(layers);
      this.consumer = consumer;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      super.tickEntity(env, context);
      if (this.performer != null) {
         this.performer.tick();
      }
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      super.onStart(env, context);
      context.getState().interruptBehaviours(this);
      ScriptPerformerBuilder builder = new ScriptPerformerBuilder(context.getState().getScript());
      this.consumer.accept(builder);
      this.performer = builder.build();
      this.performer.onScriptInstanceCreated();
      this.performer.onStart();
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      super.onStop(env, context);
      if (this.performer != null) {
         this.performer.onStop();
      }

      context.getState().reportEntity(context.getEntity(), context.getRef());
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return this.performer != null && !this.performer.isCompleted();
   }

   @Override
   public <E> void handleEvent(EventType<E> type, E event, EntityContext<T> context) {
      super.handleEvent(type, event, context);
      this.performer.handleEvent(type, event);
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.COMPLEX;
   }

   @Generated
   public ScriptPerformer getPerformer() {
      return this.performer;
   }
}

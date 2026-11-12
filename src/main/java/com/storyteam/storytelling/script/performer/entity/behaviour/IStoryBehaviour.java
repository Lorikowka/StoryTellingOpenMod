package com.storyteam.storytelling.script.performer.entity.behaviour;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface IStoryBehaviour<T extends Entity> {
   void tickEntity(ScriptEnvironment var1, EntityContext<T> var2);

   void onStart(ScriptEnvironment var1, EntityContext<T> var2);

   void onStop(ScriptEnvironment var1, EntityContext<T> var2);

   boolean canStillUse(ScriptEnvironment var1, EntityContext<T> var2);

   @NotNull
   BehaviourType getType();

   default <E> void handleEvent(EventType<E> type, E event, EntityContext<T> context) {
   }
}

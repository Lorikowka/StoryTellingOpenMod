package com.storyteam.storytelling.script.performer.entity.behaviour;

import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.world.entity.Entity;

public interface InterruptableBehaviour<T extends Entity> extends IStoryBehaviour<T> {
   void onInterrupt(ScriptEnvironment var1, EntityContext<T> var2);

   void onContinue(ScriptEnvironment var1, EntityContext<T> var2);

   boolean isInterrupted();
}

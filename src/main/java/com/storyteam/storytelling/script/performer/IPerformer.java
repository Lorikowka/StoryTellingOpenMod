package com.storyteam.storytelling.script.performer;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.action.PerformerAction;

public interface IPerformer<P extends IPerformer<P>> {
   boolean isPaused();

   void setPaused(boolean var1);

   void addAction(PerformerAction<P> var1);

   boolean tick();

   boolean isCompleted();

   <T> void handleEvent(EventType<T> var1, T var2);

   void onStart();

   void onStop();

   String getName();

   int getActionIndex(PerformerAction<P> var1);
}

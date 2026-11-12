package com.storyteam.storytelling.script.performer.state;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.Performer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.zeith.hammerlib.util.java.Cast;

public class StateHolder<P extends Performer<?>> {
   private final Object2ObjectOpenHashMap<State.Type<P, ?>, State<P>> statesMap = new Object2ObjectOpenHashMap();
   protected final P performer;

   public StateHolder(P performer) {
      this.performer = performer;
   }

   public void tick() {
      ObjectIterator var1 = this.statesMap.values().iterator();

      while (var1.hasNext()) {
         State<P> state = (State<P>)var1.next();
         state.tick();
      }
   }

   public <T> void handleEvent(P performer, EventType<T> type, T event) {
      ObjectIterator var4 = this.statesMap.values().iterator();

      while (var4.hasNext()) {
         State<P> state = (State<P>)var4.next();
         state.handleEvent(performer, type, event);
      }
   }

   public void onScriptStop() {
      ObjectIterator var1 = this.statesMap.values().iterator();

      while (var1.hasNext()) {
         State<P> state = (State<P>)var1.next();
         state.unregisterAsListener();
         state.onScriptStop();
      }
   }

   public void onScriptInstanceDestroyed() {
      ObjectIterator var1 = this.statesMap.values().iterator();

      while (var1.hasNext()) {
         State<P> state = (State<P>)var1.next();
         state.unregisterAsListener();
         state.onScriptStop();
      }
   }

   public <T extends State<P>> T getOrCreate(State.Type<P, T> type) {
      return (T)Cast.cast(this.statesMap.computeIfAbsent(type, t -> {
         T state = type.getFactory().create(this.performer);
         state.registerAsListener();
         return state;
      }));
   }
}

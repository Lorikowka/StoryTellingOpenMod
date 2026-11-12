package com.storyteam.storytelling.script.performer.state;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.Performer;

public abstract class State<P extends Performer<?>> {
   protected final P performer;

   public State(P performer) {
      this.performer = performer;
   }

   public void tick() {
   }

   public <T> void handleEvent(P performer, EventType<T> type, T event) {
   }

   public final void registerAsListener() {
      Storytelling.BUS.register(this);
   }

   public final void unregisterAsListener() {
      Storytelling.BUS.unregister(this);
   }

   public static <P extends Performer<?>, T extends State<P>> State.Type<P, T> create(State.Factory<P, T> factory) {
      return new State.Type<>(factory);
   }

   public void onScriptStop() {
   }

   public interface Factory<P extends Performer<?>, T extends State<P>> {
      T create(P var1);
   }

   public static class Type<P extends Performer<?>, T extends State<P>> {
      private final State.Factory<P, T> factory;

      public Type(State.Factory<P, T> factory) {
         this.factory = factory;
      }

      public State.Factory<P, T> getFactory() {
         return this.factory;
      }
   }
}

package com.storyteam.storytelling.api.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventHandler<T> {
   public final EventType<T> type;
   public final List<Consumer<T>> handlers = new ArrayList<>();

   public EventHandler(EventType<T> type) {
      this.type = type;
   }

   public void accept(T event) {
      for (Consumer<T> handler : this.handlers) {
         handler.accept(event);
      }
   }

   public void add(Consumer<T> handler) {
      this.handlers.add(handler);
   }
}

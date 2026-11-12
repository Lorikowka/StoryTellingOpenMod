package com.storyteam.storytelling.script.performer.builder.reflective;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public class MacrosInvoker {
   final HashMap<String, HashMap<String, Iterable<?>>> iterators = new HashMap<>();
   final HashMap<String, Object> objects = new HashMap<>();
   String lastIteratorId;

   public <T> MacrosInvoker with(String name, Iterable<T> iterable) {
      if (this.lastIteratorId == null) {
         Storytelling.LOG.error(String.format("Cannot find any iterator for joining \"%s\" iterable.", name));
         return this;
      } else {
         if (this.iterators.containsKey(this.lastIteratorId)) {
            this.iterators.get(this.lastIteratorId).put(name, iterable);
         }

         return this;
      }
   }

   public <T> MacrosInvoker iterator(final String name, final Iterable<T> iterable) {
      this.lastIteratorId = name;
      if (iterable == null) {
         Storytelling.LOG.error(String.format("Cannot invoke null iterable with name \"%s\" in the script macros.", name));
         return this;
      } else {
         this.iterators.put(name, new HashMap<String, Iterable<?>>() {
            {
               this.put(name, iterable);
            }
         });
         return this;
      }
   }

   public <T> MacrosInvoker set(String name, T value) {
      this.objects.put(name, value);
      return this;
   }

   @Nullable
   public <T> T get(String name) {
      return (T)this.objects.get(name);
   }

   private MacrosInvoker copyWithObjects() {
      MacrosInvoker macrosInvoker = new MacrosInvoker();
      macrosInvoker.objects.putAll(this.objects);
      return macrosInvoker;
   }

   public void apply(
      ScriptPerformerBuilder main, BiConsumer<MacrosInvoker, ScriptPerformerBuilder> consumer, ScriptPerformerBuilderReflective.ScriptMacros settings
   ) {
      List<Consumer<ScriptPerformerBuilder>> performersList = new ArrayList<>();
      int index = 0;
      if (this.iterators.isEmpty()) {
         List<Void> l = new ArrayList<>();
         l.add(null);
         this.iterator("singleValue", l);
      }

      for (HashMap<String, Iterable<?>> iteration : this.iterators.values()) {
         HashMap<String, Iterator<?>> copy = new HashMap<>();
         iteration.forEach((s, value) -> copy.put(s, value.iterator()));
         boolean stop = false;

         while (!stop) {
            MacrosInvoker invoker = this.copyWithObjects();

            for (Entry<String, Iterator<?>> entry : copy.entrySet()) {
               if (!entry.getValue().hasNext()) {
                  stop = true;
                  break;
               }

               invoker.set("index", index++);
               invoker.set(entry.getKey(), entry.getValue().next());
            }

            if (stop) {
               break;
            }

            performersList.add(b -> consumer.accept(invoker, b));
         }
      }

      AtomicInteger integer = new AtomicInteger();
      main.thenAllPerformers(performersList.stream().map(f -> {
         ScriptPerformerBuilder builder = new ScriptPerformerBuilder(main.getPerformer().script);
         ScriptPerformerBuilder activeBuilder = builder;
         if (settings.parallel()) {
            activeBuilder = new ScriptPerformerBuilder(main.getPerformer().script);
         }

         if (settings.loop()) {
            activeBuilder.label("macrosLabel", false);
         }

         activeBuilder.thenDynamicPerformer((Consumer<ScriptPerformerBuilder>)f);
         if (settings.loop()) {
            activeBuilder.gotoLabel("macrosLabel", true);
         }

         if (settings.parallel()) {
            builder.createParallelPerformer(settings.value() + integer.getAndIncrement(), activeBuilder.build());
         } else {
            builder = activeBuilder;
         }

         return builder.build();
      }).toArray(ScriptPerformer[]::new));
   }
}

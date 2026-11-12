package com.storyteam.storytelling.system.rts.storage;

import com.storyteam.storytelling.system.persona.PersonaHandler;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class MemoryStorage<T extends Identifiable<TKey>, TKey> implements IStorage<T, TKey> {
   private final Class<T> clazz;
   private final Map<TKey, T> definitions = Collections.synchronizedMap(new LinkedHashMap<>());

   public MemoryStorage(Class<T> clazz) {
      this.clazz = clazz;
   }

   @Override
   public T register(TKey id, T definition) {
      if (id == null) {
         return null;
      } else {
         definition.setId(id);
         this.definitions.put(id, definition);
         return definition;
      }
   }

   @Nullable
   @Override
   public T get(TKey key) {
      return this.definitions.get(key);
   }

   @Override
   public List<T> all() {
      return this.all(t -> true);
   }

   @Override
   public List<T> all(Predicate<T> predicate) {
      Stream<T> stream = this.definitions.values().stream().filter(predicate);
      if (!this.definitions.isEmpty() && this.definitions.values().stream().findFirst().get() instanceof Personalizable) {
         stream = stream.filter(d -> ((Personalizable)d).getPersona().getSharedPersona() == PersonaHandler.getCurrentPersona().getSharedPersona());
      }

      return stream.toList();
   }

   @Generated
   @Override
   public Class<T> getClazz() {
      return this.clazz;
   }
}

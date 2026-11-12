package com.storyteam.storytelling.system.rts.storage;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

public interface IStorage<T extends Identifiable<TKey>, TKey> {
   Class<T> getClazz();

   T register(TKey var1, T var2);

   @Nullable
   T get(TKey var1);

   List<T> all();

   List<T> all(Predicate<T> var1);
}

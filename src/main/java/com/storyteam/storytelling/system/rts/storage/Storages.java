package com.storyteam.storytelling.system.rts.storage;

import java.util.HashMap;

public class Storages {
   private static final HashMap<String, IStorage> storages = new HashMap<>();

   public static <T extends Identifiable<TKey>, TKey> IStorage<T, TKey> get(Class<T> clazz, StorageType type) {
      if (!storages.containsKey(clazz.getSimpleName())) {
         storages.put(clazz.getSimpleName(), create(clazz, type));
      }

      return storages.get(clazz.getSimpleName());
   }

   public static <T extends Identifiable<TKey>, TKey> IStorage<T, TKey> get(Class<T> clazz) {
      return get(clazz, StorageType.MEMORY);
   }

   public static <T extends Identifiable<TKey>, TKey> IStorage<T, TKey> require(Class<T> clazz) {
      return storages.get(clazz.getSimpleName());
   }

   private static <T extends Identifiable<TKey>, TKey> IStorage<T, TKey> create(Class<T> clazz, StorageType type) {
      switch (type) {
         case MEMORY:
            return new MemoryStorage<>(clazz);
         default:
            throw new IncompatibleClassChangeError();
      }
   }
}

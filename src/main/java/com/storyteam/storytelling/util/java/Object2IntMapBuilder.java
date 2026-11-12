package com.storyteam.storytelling.util.java;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.function.Function;
import java.util.function.Supplier;

public record Object2IntMapBuilder<T>(Object2IntMap<T> map) implements Supplier<Object2IntMap<T>> {
   public static <T> Object2IntMapBuilder<T> make() {
      return new Object2IntMapBuilder<>(new Object2IntArrayMap());
   }

   public Object2IntMapBuilder<T> put(T key, int value) {
      this.map.put(key, value);
      return this;
   }

   public <V> Object2IntMapBuilder<V> remap(Function<T, V> remap) {
      Object2IntMapBuilder<V> res = make();
      ObjectIterator var3 = this.map.object2IntEntrySet().iterator();

      while (var3.hasNext()) {
         Entry<T> entry = (Entry<T>)var3.next();
         res.put(remap.apply((T)entry.getKey()), entry.getIntValue());
      }

      return res;
   }

   public Object2IntMap<T> get() {
      return this.map;
   }
}

package com.storyteam.storytelling.util.data_structure;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WeightedList<T> implements Iterable<T> {
   private final TreeMap<Double, T> elements;
   private double count;

   public WeightedList(TreeMap<Double, T> elements) {
      this.elements = elements;
   }

   public WeightedList() {
      this.elements = new TreeMap<>();
   }

   public Collection<T> values() {
      return this.elements.values();
   }

   public void add(T object, double rarity) {
      this.elements.put(this.count, object);
      this.count += rarity;
   }

   public void addAll(@NotNull Iterable<T> collection, Function<T, Float> function) {
      collection.forEach(value -> this.add((T)value, (double)function.apply((T)value).floatValue()));
   }

   public T getRandomItem(RandomSource random) {
      double r = random.nextDouble() * this.count;

      try {
         return this.elements.isEmpty() ? null : this.elements.floorEntry(r).getValue();
      } catch (NullPointerException var5) {
         throw new RuntimeException(var5);
      }
   }

   public void clear() {
      this.elements.clear();
   }

   public int size() {
      return this.elements.size();
   }

   public boolean isEmpty() {
      return this.elements.isEmpty();
   }

   @Nullable
   public WeightedList<T> copy() {
      try {
         return new WeightedList<>((TreeMap<Double, T>)this.elements.clone());
      } catch (ClassCastException var2) {
         return null;
      }
   }

   @Nullable
   public Map<Double, T> toMap() {
      return this.elements;
   }

   public WeightedList<T> filtered(Predicate<T> predicate) {
      WeightedList<T> list = new WeightedList<>();
      double last = 0.0;
      T obj = null;
      Iterator<Entry<Double, T>> iterator = this.elements.entrySet().iterator();

      while (iterator.hasNext()) {
         Entry<Double, T> entry = iterator.next();
         if (obj != null) {
            list.add(obj, entry.getKey() - last);
            obj = null;
         }

         if (predicate.test(entry.getValue())) {
            last = entry.getKey();
            obj = entry.getValue();
            if (!iterator.hasNext()) {
               list.add(obj, this.count - last);
            }
         }
      }

      return list;
   }

   @NotNull
   @Override
   public Iterator<T> iterator() {
      return this.elements.values().iterator();
   }
}

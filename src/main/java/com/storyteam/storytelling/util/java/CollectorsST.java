package com.storyteam.storytelling.util.java;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.stream.Collector;

public class CollectorsST {
   public static Collector<Integer, IntList, IntList> toIntList() {
      return Collector.of(IntArrayList::new, IntList::add, (a, b) -> {
         IntArrayList ial = new IntArrayList();
         ial.addAll(a);
         ial.addAll(b);
         return ial;
      });
   }
}

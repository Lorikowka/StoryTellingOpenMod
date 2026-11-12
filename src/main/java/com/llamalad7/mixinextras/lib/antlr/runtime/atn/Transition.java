package com.llamalad7.mixinextras.lib.antlr.runtime.atn;

import com.llamalad7.mixinextras.lib.antlr.runtime.misc.IntervalSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Transition {
   public static final List<String> serializationNames = Collections.unmodifiableList(
      Arrays.asList("INVALID", "EPSILON", "RANGE", "RULE", "PREDICATE", "ATOM", "ACTION", "SET", "NOT_SET", "WILDCARD", "PRECEDENCE")
   );
   public static final Map<Class<? extends Transition>, Integer> serializationTypes = Collections.unmodifiableMap(
      new HashMap<Class<? extends Transition>, Integer>() {
         {
            this.put(EpsilonTransition.class, Integer.valueOf(1));
            this.put(RangeTransition.class, Integer.valueOf(2));
            this.put(RuleTransition.class, Integer.valueOf(3));
            this.put(PredicateTransition.class, Integer.valueOf(4));
            this.put(AtomTransition.class, Integer.valueOf(5));
            this.put(ActionTransition.class, Integer.valueOf(6));
            this.put(SetTransition.class, Integer.valueOf(7));
            this.put(NotSetTransition.class, Integer.valueOf(8));
            this.put(WildcardTransition.class, Integer.valueOf(9));
            this.put(PrecedencePredicateTransition.class, Integer.valueOf(10));
         }
      }
   );
   public ATNState target;

   protected Transition(ATNState target) {
      if (target == null) {
         throw new NullPointerException("target cannot be null.");
      } else {
         this.target = target;
      }
   }

   public abstract int getSerializationType();

   public boolean isEpsilon() {
      return false;
   }

   public IntervalSet label() {
      return null;
   }

   public abstract boolean matches(int var1, int var2, int var3);
}

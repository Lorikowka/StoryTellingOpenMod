package com.storyteam.storytelling.script.performer.builder.reflective;

import com.storyteam.storytelling.script.ScriptInstance;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import org.zeith.hammerlib.util.java.tuples.Tuples;

public class ScriptSelfBuilder extends ScriptPerformerBuilderReflective {
   public ScriptSelfBuilder(ScriptInstance script) {
      super(script);

      try {
         SortedSet<Tuple3<ScriptPerformerBuilderReflective.IReflectiveInvoker, ScriptPerformerBuilderReflective.ScriptPart, Integer>> parts = new TreeSet<>(
            Comparator.comparingInt(Tuple3::c)
         );

         for (Method m : script.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(ScriptPerformerBuilderReflective.ScriptPart.class)
               && m.getParameterCount() == 1
               && m.getParameterTypes()[0].isInstance(this)) {
               ScriptPerformerBuilderReflective.ScriptPart part = m.getAnnotation(ScriptPerformerBuilderReflective.ScriptPart.class);
               parts.add(Tuples.immutable(ScriptPerformerBuilderReflective.IReflectiveInvoker.ofMethod(m, script, this), part, part.value()));
            }
         }

         for (Tuple3<ScriptPerformerBuilderReflective.IReflectiveInvoker, ScriptPerformerBuilderReflective.ScriptPart, Integer> part : parts) {
            ((ScriptPerformerBuilderReflective.IReflectiveInvoker)part.a()).invoke();
         }
      } catch (Throwable var8) {
         if (var8 instanceof RuntimeException runtime) throw runtime;
         if (var8 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var8);
      }
   }
}

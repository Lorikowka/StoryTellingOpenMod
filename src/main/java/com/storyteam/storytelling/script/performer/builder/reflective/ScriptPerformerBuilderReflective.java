package com.storyteam.storytelling.script.performer.builder.reflective;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import org.zeith.hammerlib.util.java.tuples.Tuples;

public abstract class ScriptPerformerBuilderReflective extends ScriptPerformerBuilder {
   HashMap<String, ScriptPerformerBuilderReflective.MacrosMethodInvoker> macros = new HashMap<>();
   protected final SortedSet<Tuple3<ScriptPerformerBuilderReflective.IReflectiveInvoker, ScriptPerformerBuilderReflective.ScriptPart, Integer>> parts = new TreeSet<>(
      Comparator.comparingInt(Tuple3::c)
   );
   private static Map<Class<?>, Set<String>> IGNORED = new IdentityHashMap<Class<?>, Set<String>>() {
      {
         this.put(ScriptPerformerBuilder.class, Arrays.stream(ScriptPerformerBuilder.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet()));
      }
   };

   public ScriptPerformerBuilderReflective(ScriptInstance script, boolean handleMethods) {
      super(script);
      if (handleMethods) {
         this.handleMethods(script);

         for (Tuple3<ScriptPerformerBuilderReflective.IReflectiveInvoker, ScriptPerformerBuilderReflective.ScriptPart, Integer> part : this.parts) {
            try {
               ((ScriptPerformerBuilderReflective.IReflectiveInvoker)part.a()).invoke();
            } catch (ReflectiveOperationException var7) {
               Throwable thr = var7;
               if (var7.getCause() != null) {
                  thr = var7.getCause();
               }

               throw new RuntimeException(thr);
            }
         }

         this.onReflectiveHandlingEnd();
      }
   }

   public ScriptPerformerBuilderReflective(ScriptInstance script) {
      super(script);
      this.handleMethods(script);

      for (Tuple3<ScriptPerformerBuilderReflective.IReflectiveInvoker, ScriptPerformerBuilderReflective.ScriptPart, Integer> part : this.parts) {
         try {
            ((ScriptPerformerBuilderReflective.IReflectiveInvoker)part.a()).invoke();
         } catch (ReflectiveOperationException var6) {
            Throwable thr = var6;
            if (var6.getCause() != null) {
               thr = var6.getCause();
            }

            throw new RuntimeException(thr);
         }
      }

      this.onReflectiveHandlingEnd();
   }

   public ScriptPerformerBuilderReflective(ScriptInstance script, ScriptPerformer performer) {
      super(script, performer);
      this.handleMethods(script);

      for (Tuple3<ScriptPerformerBuilderReflective.IReflectiveInvoker, ScriptPerformerBuilderReflective.ScriptPart, Integer> part : this.parts) {
         try {
            ((ScriptPerformerBuilderReflective.IReflectiveInvoker)part.a()).invoke();
         } catch (ReflectiveOperationException var7) {
            Throwable thr = var7;
            if (var7.getCause() != null) {
               thr = var7.getCause();
            }

            throw new RuntimeException(thr);
         }
      }

      this.onReflectiveHandlingEnd();
   }

   public void onReflectiveHandlingEnd() {
   }

   public void handleMethods(ScriptInstance instance) {
      List<Method> mths = Arrays.stream(this.getClass().getDeclaredMethods()).toList();

      for (Method mth : mths.stream().filter(e -> e.isAnnotationPresent(ScriptPerformerBuilderReflective.ScriptMacros.class)).toList()) {
         mth.setAccessible(true);
         this.handleMethod(instance, mth);
      }

      for (Method mth : mths.stream().filter(e -> !e.isAnnotationPresent(ScriptPerformerBuilderReflective.ScriptMacros.class)).toList()) {
         mth.setAccessible(true);
         this.handleMethod(instance, mth);
      }
   }

   protected void handleMethod(ScriptInstance instance, Method mth) {
      Tuple3<ScriptPerformerBuilderReflective.IReflectiveInvoker, ScriptPerformerBuilderReflective.ScriptPart, Integer> part = this.handleScriptPartAnnotation(
         instance, mth
      );
      if (part != null) {
         this.parts.add(part);
      }

      this.handleScriptMacrosAnnotation(instance, mth);
      this.handleSecondaryAnnotation(instance, mth);
   }

   public ScriptPerformerBuilderReflective invoke(String name) {
      return this.invoke(name, new MacrosInvoker());
   }

   public ScriptPerformerBuilderReflective invoke(String name, MacrosInvoker invoker) {
      ScriptPerformerBuilderReflective.MacrosMethodInvoker methodInv = this.macros.get(name);
      invoker.apply(this, (inv, b) -> {
         ScriptPerformerBuilderReflective builder = methodInv.builder.get();
         methodInv.invokeFunction.accept(builder, inv);
         b.thenAllPerformers(new Performer[]{builder.build()});
      }, methodInv.settings);
      return this;
   }

   private void handleSecondaryAnnotation(ScriptInstance si, Method mth) {
      if (mth.isAnnotationPresent(ScriptPerformerBuilderReflective.Secondary.class)) {
         if (!Modifier.isStatic(mth.getModifiers())) {
            ScriptPerformerBuilderReflective.Secondary sm = mth.getAnnotation(ScriptPerformerBuilderReflective.Secondary.class);
            mth.setAccessible(true);
            Class<?>[] pars = mth.getParameterTypes();
            ScriptPerformerBuilderReflective copy = this.createChild();

            try {
               if (pars.length == 1 && pars[0].isInstance(si)) {
                  mth.invoke(copy, si);
               }

               if (pars.length == 0) {
                  mth.invoke(copy);
               }
            } catch (IllegalAccessException | InvocationTargetException var7) {
               Storytelling.LOG.error("Failed to add secondary performer " + sm.value() + " to script " + si.getId());
               throw new RuntimeException(var7);
            }

            si.registerSecondaryPerformer(sm.value().equals("") ? mth.getName() : sm.value(), copy.build());
         }
      }
   }

   private void handleScriptMacrosAnnotation(ScriptInstance si, Method mth) {
      if (mth.isAnnotationPresent(ScriptPerformerBuilderReflective.ScriptMacros.class)) {
         if (!Modifier.isStatic(mth.getModifiers())) {
            ScriptPerformerBuilderReflective.ScriptMacros sm = mth.getAnnotation(ScriptPerformerBuilderReflective.ScriptMacros.class);
            mth.setAccessible(true);
            Class<?>[] pars = mth.getParameterTypes();
            if (pars.length == 2 && pars[0].isInstance(si) && MacrosInvoker.class.isAssignableFrom(pars[1])) {
               this.macros
                  .put(
                     sm.value(),
                     new ScriptPerformerBuilderReflective.MacrosMethodInvoker(
                        this::createChild,
                        (builder, invoker) -> ScriptPerformerBuilderReflective.IReflectiveInvoker.ofMethod(mth, builder, si, invoker).unsafeInvoke(),
                        sm
                     )
                  );
            }

            if (pars.length == 1 && pars[0].isInstance(si)) {
               this.macros
                  .put(
                     sm.value(),
                     new ScriptPerformerBuilderReflective.MacrosMethodInvoker(
                        this::createChild, (builder, $) -> ScriptPerformerBuilderReflective.IReflectiveInvoker.ofMethod(mth, builder, si).unsafeInvoke(), sm
                     )
                  );
            }

            if (pars.length == 0) {
               this.macros
                  .put(
                     sm.value(),
                     new ScriptPerformerBuilderReflective.MacrosMethodInvoker(
                        this::createChild, (builder, $) -> ScriptPerformerBuilderReflective.IReflectiveInvoker.ofMethod(mth, builder).unsafeInvoke(), sm
                     )
                  );
            }
         }
      }
   }

   @Nullable
   private Tuple3<ScriptPerformerBuilderReflective.IReflectiveInvoker, ScriptPerformerBuilderReflective.ScriptPart, Integer> handleScriptPartAnnotation(
      ScriptInstance si, Method mth
   ) {
      if (!mth.isAnnotationPresent(ScriptPerformerBuilderReflective.ScriptPart.class)) {
         return null;
      } else if (Modifier.isStatic(mth.getModifiers())) {
         return null;
      } else {
         ScriptPerformerBuilderReflective.ScriptPart sp = mth.getAnnotation(ScriptPerformerBuilderReflective.ScriptPart.class);
         mth.setAccessible(true);
         if (mth.getParameterCount() > 0) {
            Class<?>[] pars = mth.getParameterTypes();
            return pars.length == 1 && pars[0].isInstance(si)
               ? Tuples.immutable(ScriptPerformerBuilderReflective.IReflectiveInvoker.ofMethod(mth, this, si), sp, sp.value())
               : null;
         } else {
            return Tuples.immutable(ScriptPerformerBuilderReflective.IReflectiveInvoker.ofMethod(mth, this), sp, sp.value());
         }
      }
   }

   protected ScriptPerformerBuilderReflective createChild() {
      try {
         ScriptPerformerBuilderReflective reflective = (ScriptPerformerBuilderReflective)UnsafeHacks.newInstance(this.getClass());
         Field performerField = ReflectionUtil.lookupField(ScriptPerformerBuilder.class, ScriptPerformer.class);
         if (performerField == null) {
            throw new IllegalStateException("In ScriptPerformerBuilder class was not found field with ScriptPerformer type.");
         } else {
            performerField.setAccessible(true);
            ScriptPerformer performer = new ScriptPerformer(this.getPerformer().script);
            performer.inheritFrom(this.getPerformer());
            performerField.set(reflective, performer);
            Map<Class<?>, Set<String>> ignored = this.getIgnoredField();

            for (Class<?> clazz = this.getClass(); clazz.getSuperclass() != null; clazz = clazz.getSuperclass()) {
               for (Field field : clazz.getDeclaredFields()) {
                  if ((!ignored.containsKey(clazz) || !ignored.get(clazz).contains(field.getName())) && !field.getName().startsWith("$")) {
                     field.setAccessible(true);
                     field.set(reflective, field.get(this));
                  }
               }
            }

            return reflective;
         }
      } catch (IllegalAccessException e) {
         throw new IllegalStateException("Unable to copy performer fields", e);
      }
   }

   protected Map<Class<?>, Set<String>> getIgnoredField() {
      return IGNORED;
   }

   public interface IReflectiveInvoker {
      @Nullable
      Object invoke() throws ReflectiveOperationException;

      @Nullable
      default Object unsafeInvoke() {
         try {
            return this.invoke();
         } catch (ReflectiveOperationException var2) {
            throw new RuntimeException(var2);
         }
      }

      static ScriptPerformerBuilderReflective.IReflectiveInvoker ofMethod(Method method, Object obj, Object... args) {
         return () -> method.invoke(obj, args);
      }
   }

   private static record MacrosMethodInvoker(
      Supplier<ScriptPerformerBuilderReflective> builder,
      BiConsumer<ScriptPerformerBuilderReflective, MacrosInvoker> invokeFunction,
      ScriptPerformerBuilderReflective.ScriptMacros settings
   ) {
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.METHOD})
   public @interface ScriptMacros {
      String value();

      boolean parallel() default false;

      boolean loop() default false;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.METHOD})
   public @interface ScriptPart {
      int value();
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.METHOD})
   public @interface Secondary {
      String value() default "";
   }
}


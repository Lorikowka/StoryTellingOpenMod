package com.storyteam.storytelling.network;

import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.java.Cast;

public class ReflectionUtil {
   public static boolean isUniqueMethod(Method method) {
      return Arrays.stream(method.getDeclaringClass().getMethods()).map(Method::getName).filter(method.getName()::equals).count() == 1L;
   }

   public static Class<?> getArrayComponent(Class<?> array) {
      return array.isArray() ? getArrayComponent(array.getComponentType()) : array;
   }

   public static <T> Class<?> findCommonSuperClass(Collection<T> coll) {
      if (coll.isEmpty()) {
         return Void.class;
      } else {
         Class<?> oclass = null;

         for (T t : coll) {
            if (oclass == null) {
               oclass = t.getClass();
            } else {
               oclass = findClosestAncestor(oclass, t.getClass());
            }
         }

         return oclass;
      }
   }

   public static <T> Class<?> findCommonSuperClass(Collection<T> coll, Function<T, Class<?>> toClass) {
      if (coll.isEmpty()) {
         return Void.class;
      } else {
         Class<?> oclass = null;

         for (T t : coll) {
            if (oclass == null) {
               oclass = toClass.apply(t);
            } else {
               oclass = findClosestAncestor(oclass, toClass.apply(t));
            }
         }

         return oclass;
      }
   }

   public static Class<?> findClosestAncestor(Class<?> a, Class<?> b) {
      while (!a.isAssignableFrom(b)) {
         a = a.getSuperclass();
      }

      return a;
   }

   public static boolean doesParameterTypeArgsMatch(Parameter param, Class<?>... baseArgs) {
      Type[] args = getTypeArgs(param.getParameterizedType());
      if (args.length != baseArgs.length) {
         return false;
      } else {
         for (int i = 0; i < args.length; i++) {
            if (!(args[i] instanceof Class) || !baseArgs[i].isAssignableFrom((Class<?>)args[i])) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean doesParameterTypeArgsMatch(Field field, Class<?>... baseArgs) {
      Type[] args = getTypeArgs(field.getGenericType());
      if (args.length != baseArgs.length) {
         return false;
      } else {
         for (int i = 0; i < args.length; i++) {
            if (!(args[i] instanceof Class) || !baseArgs[i].isAssignableFrom((Class<?>)args[i])) {
               return false;
            }
         }

         return true;
      }
   }

   public static Type[] getTypeArgs(Type type) {
      return type instanceof ParameterizedType pt ? pt.getActualTypeArguments() : new Type[0];
   }

   public static Class<?> fetchClassAny(org.objectweb.asm.Type type) {
      return fetchClass(type.getSort() < 9 ? type.getClassName() : type.getInternalName().replace('/', '.'));
   }

   public static <T> Class<T> fetchClass(org.objectweb.asm.Type type) {
      return fetchClass(type.getSort() < 9 ? type.getClassName() : type.getInternalName().replace('/', '.'));
   }

   public static <T> Class<T> fetchClass(String name) {
      try {
         return (Class<T>)Cast.cast(Class.forName(name));
      } catch (ClassNotFoundException var2) {
      } catch (RuntimeException var3) {
         if (var3.getMessage().contains("invalid dist")) {
            HammerLib.LOG.warn("Attempted to load class from invalid dist: " + name, var3);
         }
      }

      return null;
   }

   public static Iterable<Field> getFieldsUpTo(@Nonnull Class<?> startClass, @Nullable Class<?> exclusiveParent) {
      List<Field> currentClassFields = Lists.newArrayList(startClass.getDeclaredFields());
      Class<?> parentClass = startClass.getSuperclass();
      if (parentClass != null && (exclusiveParent == null || !parentClass.equals(exclusiveParent))) {
         List<Field> parentClassFields = (List<Field>)getFieldsUpTo(parentClass, exclusiveParent);
         currentClassFields.addAll(parentClassFields);
      }

      return currentClassFields;
   }

   public static Object lookupValue(Object object, Class<?> type) {
      Field field = lookupField(object.getClass(), type);
      if (field == null) {
         return null;
      } else {
         try {
            return field.get(object);
         } catch (Exception var4) {
            throw new RuntimeException(var4);
         }
      }
   }

   public static Field lookupField(Class<?> clazz, Class<?> type) {
      Field ret = null;

      for (Field field : clazz.getDeclaredFields()) {
         if (type.isAssignableFrom(field.getType())) {
            if (ret != null) {
               return null;
            }

            field.setAccessible(true);
            ret = field;
         }
      }

      return ret;
   }

   public static List<Field> lookupFields(Class<?> clazz, Class<?> type) {
      List<Field> fields = new ArrayList<>();

      for (Field field : clazz.getDeclaredFields()) {
         if (type.isAssignableFrom(field.getType())) {
            field.setAccessible(true);
            fields.add(field);
         }
      }

      return fields;
   }

   public static Field lookupField(Class<?> clazz, String name) {
      Field ret = null;

      for (Field field : clazz.getDeclaredFields()) {
         if (name.equals(field.getName())) {
            if (ret != null) {
               return null;
            }

            field.setAccessible(true);
            ret = field;
         }
      }

      return ret;
   }

   public static <T> Optional<T> fetchValue(Field field, Object instance, Class<T> targetType) {
      try {
         field.setAccessible(true);
         return Cast.optionally(field.get(instance), targetType);
      } catch (Throwable var4) {
         return Optional.empty();
      }
   }

   public static Class<?> getCaller() {
      try {
         return Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());
      } catch (ClassNotFoundException var1) {
         return null;
      }
   }

   public static <T> Optional<T> getField(Object anything, int i, Class<T> cast) {
      try {
         Field f = anything.getClass().getDeclaredFields()[i];
         f.setAccessible(true);
         return Cast.optionally(f.get(anything), cast);
      } catch (Throwable var4) {
         var4.printStackTrace();
         return Optional.empty();
      }
   }

   public static <T> Optional<T> getStaticFinalField(Class<?> owner, String member) {
      try {
         Field f = owner.getDeclaredField(member);
         f.setAccessible(true);
         return Optional.ofNullable((T)Cast.cast(f.get(null)));
      } catch (ReflectiveOperationException var3) {
         if (!(var3 instanceof NoSuchFieldException)) {
            var3.printStackTrace();
         }

         return Optional.empty();
      }
   }

   public static Method findDeclaredMethod(Class<?> c, String member, Predicate<Method> o) throws NoSuchMethodException {
      for (Method method : c.getDeclaredMethods()) {
         if (method.getName().equals(member) && o.test(method)) {
            return method;
         }
      }

      throw new NoSuchMethodException(c.getName() + "/" + member);
   }
}

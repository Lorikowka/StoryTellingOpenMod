package com.storyteam.storytelling.client;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.api.animations.IAnimationGroup;
import com.storyteam.storytelling.client.animations.AnimationsST2;
import com.storyteam.storytelling.client.animations.AnimationsST3;
import com.storyteam.storytelling.client.animations.AnimationsST4;
import com.storyteam.storytelling.client.animations.AnimationsST5;
import com.storyteam.storytelling.client.animations.AnimationsST6;
import com.storyteam.storytelling.client.animations.AnimationsST7;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.hammeranims.api.animation.AnimationLocation;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.core.init.DefaultsHA;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class Animations implements AnimationsST8, AnimationsST7, AnimationsST6, AnimationsST5, AnimationsST4, AnimationsST3, AnimationsST2 {
   @Generated
   private static final Logger log = LogManager.getLogger(Animations.class);
   private static final BiMap<String, AnimationWithMetadata> ANIMATION_MAP = HashBiMap.create();
   private static final Map<AnimationWithMetadata, String> ANIMATION_MAP_REVERSE = Collections.unmodifiableMap(ANIMATION_MAP.inverse());
   private static final Map<String, AnimationWithMetadata> ANIMATION_MAP_VIEW = Collections.unmodifiableMap(ANIMATION_MAP);
   private static final List<String> ANIMATION_LIST = new ArrayList<>();
   private static final List<String> ANIMATION_LIST_VIEW = Collections.unmodifiableList(ANIMATION_LIST);
   public static IAnimationSource NULL = new IAnimationSource() {
      public AnimationLocation getLocation() {
         return DefaultsHA.NULL_ANIMATION.getLocation();
      }

      public ConfiguredAnimation configure() {
         return DefaultsHA.NULL_ANIMATION.configure();
      }
   };

   public static Stream<AnimationWithMetadata> animations() {
      Set<Class<?>> all = new HashSet<>();
      downstepWithInnerClasses(Animations.class, all::add);
      all.remove(Animations.class);
      all.removeIf(Class::isRecord);
      return all.stream().mapMulti((cls, cons) -> {
         Since since = cls.getAnnotation(Since.class);
         if (since == null) {
            throw new RuntimeException("Unable to find Since in " + cls);
         } else {
            for (Field field : cls.getDeclaredFields()) {
               int mod = field.getModifiers();
               if (Modifier.isStatic(mod)) {
                  try {
                     Object value = field.get(null);
                     if (value instanceof IAnimationGroup) {
                        IAnimationGroup group = (IAnimationGroup)value;

                        for (String key : group.getKeys()) {
                           String fullKey = field.getName() + "." + key;
                           IAnimationSource an = group.getAnimation(key).orElse(null);
                           cons.accept(new AnimationWithMetadata(fullKey, an, since, field));
                        }
                     } else {
                        if (value == null) {
                           throw new NullPointerException(cls + ": " + field.getName());
                        }

                        if (!(value instanceof IAnimationSource src)) {
                           throw new ClassCastException(field.toGenericString() + " can not be cast to IAnimationSource");
                        }

                        cons.accept(new AnimationWithMetadata(field.getName(), src, since, field));
                     }
                  } catch (ReflectiveOperationException var14) {
                     throw new RuntimeException(var14);
                  }
               }
            }
         }
      });
   }

   private static void downstepWithInnerClasses(Class<?> base, Consumer<Class<?>> type) {
      Set<Class<?>> checked = new HashSet<>();
      Stack<Class<?>> checks = new Stack<>();
      checks.push(base);

      while (!checks.isEmpty()) {
         Class<?> cur = checks.pop();
         if (!Object.class.equals(cur) && !Record.class.equals(cur)) {
            type.accept(cur);
         }

         for (Class<?> i : cur.getInterfaces()) {
            if (checked.add(i)) {
               checks.push(i);
            }
         }

         Class<?> sup = cur.getSuperclass();
         if (sup != null && checked.add(sup)) {
            checks.push(sup);
         }

         for (Class<?> ix : cur.getClasses()) {
            if (checked.add(ix)) {
               checks.push(ix);
            }
         }
      }
   }

   public static List<String> getAnimationList() {
      getAnimationMap();
      return ANIMATION_LIST_VIEW;
   }

   public static Map<String, AnimationWithMetadata> getAnimationMap() {
      if (ANIMATION_MAP_VIEW.isEmpty()) {
         Map<String, AnimationWithMetadata> animations = new HashMap<>();
         Iterator<AnimationWithMetadata> itr = animations().iterator();

         while (itr.hasNext()) {
            AnimationWithMetadata anim = itr.next();
            animations.put(anim.name(), anim);
         }

         ANIMATION_MAP.putAll(animations);
         ANIMATION_LIST.addAll(animations.keySet());
         log.info("Animations initialized ({} key-value pairs).", animations.size());
      }

      return ANIMATION_MAP_VIEW;
   }

   public static Map<AnimationWithMetadata, String> getAnimationMapBySource() {
      return ANIMATION_MAP_REVERSE;
   }

   static {
      log.info("Detected {} animations.", animations().count());
      downstepWithInnerClasses(Animations.class, cls -> {
         log.info("Animations sub-class detected: {}", cls);
         if (cls.isInterface()) {
            Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, (proxy, method, args) -> null);
         } else {
            try {
               cls.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException var2) {
            }
         }
      });
   }
}

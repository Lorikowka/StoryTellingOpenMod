package com.storyteam.storytelling.client;

import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.client.core.AnimationStorage;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import com.storyteam.storytelling.network.ReflectionUtil;
import java.util.NoSuchElementException;
import org.zeith.hammeranims.api.animation.IAnimationContainer;
import org.zeith.hammeranims.api.animation.IAnimationSource;

public class AnimationsRegistrySeasonal {
   public static AnimationStorage STORAGE = new AnimationsRegistry();
   public static final int MAX_DEPTH = 5;

   public static SidedAnimationSet registerSidedSeparate(String rightPath, String leftPath) {
      return registerSidedSeparate(fromStackTrace(5), rightPath, leftPath);
   }

   public static SidedAnimationSet registerSided(String rightVariant, String leftVariant, String path) {
      return registerSided(fromStackTrace(5), rightVariant, leftVariant, path);
   }

   public static IAnimationContainer register(String path) {
      return register(fromStackTrace(5), path);
   }

   public static IAnimationSource register(String path, String variant) {
      return register(fromStackTrace(5), path, variant);
   }

   private static SidedAnimationSet registerSidedSeparate(Since.Season season, String rightPath, String leftPath) {
      return STORAGE.registerSidedSeparate(prefix(season) + rightPath, prefix(season) + leftPath);
   }

   private static SidedAnimationSet registerSided(Since.Season season, String rightVariant, String leftVariant, String path) {
      return STORAGE.registerSided(rightVariant, leftVariant, prefix(season) + path);
   }

   private static IAnimationContainer register(Since.Season season, String path) {
      return STORAGE.register(prefix(season) + path);
   }

   private static IAnimationSource register(Since.Season season, String path, String variant) {
      return register(season, path).holder(variant);
   }

   private static Since.Season fromStackTrace(int maxDepth) {
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      int i = 0;

      for (int len = Math.min(maxDepth, stackTrace.length); i < len; i++) {
         StackTraceElement element = stackTrace[i];
         Class<?> cls = ReflectionUtil.fetchClass(element.getClassName());
         if (cls != null) {
            Since since = cls.getAnnotation(Since.class);
            if (since != null) {
               return since.value();
            }
         }
      }

      throw new NoSuchElementException();
   }

   private static String prefix(Since.Season season) {
      return season.getPrefix();
   }
}

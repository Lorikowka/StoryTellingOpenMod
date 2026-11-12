package com.storyteam.storytelling.util;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;

public class MathUtils {
   public static <T> T randomChoice(Collection<T> collection, Random random) {
      if (collection.isEmpty()) {
         return null;
      } else {
         int index = random.nextInt(collection.size());
         if (collection instanceof List) {
            return ((List<T>)collection).get(index);
         } else {
            int i = 0;

            for (T t : collection) {
               if (i == index) {
                  return t;
               }

               i++;
            }

            return null;
         }
      }
   }

   public static <T> T randomChoice(Collection<T> collection) {
      return randomChoice(collection, new Random());
   }

   public static float randomFloat(RandomSource random) {
      return -1.0F + 2.0F * random.nextFloat();
   }

   public static float randomFloat(Random random) {
      return -1.0F + 2.0F * random.nextFloat();
   }

   public static double clamp(double value, double min, double max) {
      return Math.min(max, Math.max(value, min));
   }

   public static int clamp(int value, int min, int max) {
      return Math.min(max, Math.max(min, value));
   }

   public static float clamp(float value, float min, float max) {
      return Math.min(max, Math.max(value, min));
   }

   public static float ensureRange(float value, float min, float max) {
      return Math.min(Math.max(value, min), max);
   }

   public static double ensureRange(double value, double min, double max) {
      return Math.min(Math.max(value, min), max);
   }

   public static float lerp(float x, float y, float p) {
      return (y - x) * p + x;
   }

   public static float randomBetween(Random random, float min, float max) {
      return random.nextFloat() * (max - min) + min;
   }

   public static double randomBetween(Random random, double min, double max) {
      return random.nextDouble() * (max - min) + min;
   }

   public static int randomBetween(Random random, int min, int max) {
      return random.nextInt() * (max - min) + min;
   }

   public static double round(double value, int steps) {
      double multiplier = Math.pow(10.0, (double)steps);
      return (double)Math.round(value * multiplier) / multiplier;
   }

   public static float lerpThroughBoundaries(float bVal, float tVal, float percent, float bottom, float top) {
      float fullDist = bVal - bottom + (top - tVal);
      float dist = fullDist * percent;
      float diff = dist - bVal;
      return diff > 0.0F ? top - diff : bVal - dist;
   }

   public static Function<Float, Float> cubicBezier(float x1, float y1, float x2, float y2) {
      Vec2 p1 = new Vec2(x1, y1);
      Vec2 p2 = new Vec2(x2, y2);
      int iterations = 100;
      float epsilon = 1.0E-6F;
      return x -> {
         float low = 0.0F;
         float high = 1.0F;

         for (int i = 0; i < iterations; i++) {
            float mid = (low + high) / 2.0F;
            Vec2 curvePoint = cubicBezierPoint(p1, p2, mid);
            if (Math.abs(x - curvePoint.x) < epsilon) {
               return curvePoint.y;
            }

            if (curvePoint.x < x) {
               low = mid;
            } else {
               high = mid;
            }
         }

         return null;
      };
   }

   public static Vec2 cubicBezierPoint(Vec2 p1, Vec2 p2, float t) {
      if (!(t < 0.0F) && !(t > 1.0F)) {
         float u = 1.0F - t;
         float tt = t * t;
         float uu = u * u;
         float uuu = uu * u;
         float ttt = tt * t;
         float x = 3.0F * uu * t * p1.x + 3.0F * u * tt * p2.x + ttt * 1.0F;
         float y = 3.0F * uu * t * p1.y + 3.0F * u * tt * p2.y + ttt * 1.0F;
         return new Vec2(x, y);
      } else {
         throw new IllegalArgumentException("Parameter t must be between 0 and 1");
      }
   }
}

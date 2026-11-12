package com.storyteam.storytelling.system.octospizdel.client.animation;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.octospizdel.util.AnimationUtils;
import java.lang.reflect.Field;
import oshi.util.tuples.Pair;

public class PropertyTweener<T> extends Tweener {
   private final Object target;
   private final String[] field;
   private T initialValue;
   private T baseFinalValue;
   private T finalValue;
   private T deltaValue;
   private final double duration;
   private TransitionType transitionType;
   private EaseType easeType;
   private Runnable customMethod;
   private double delay = 0.0;
   boolean doContinue = true;
   boolean doContinueDelayed = false;
   boolean relative = false;

   PropertyTweener(Object target, String field, T to, double duration) {
      this.target = target;
      this.field = field.split("\\.");
      this.initialValue = this.getField(target, this.field);
      this.baseFinalValue = to;
      this.finalValue = this.baseFinalValue;
      this.duration = duration;
   }

   @Override
   public void start() {
      super.start();
      if (Tween.isObjectInvalid(this.target)) {
         Storytelling.LOG.warn("Target object is invalid!");
      } else {
         if (this.doContinue) {
            if (this.delay < 0.001) {
               this.initialValue = this.getField(this.target, this.field);
            } else {
               this.doContinueDelayed = true;
            }
         }

         if (this.relative) {
            this.finalValue = AnimationUtils.add(this.initialValue, this.baseFinalValue);
         }

         this.deltaValue = AnimationUtils.subtract(this.finalValue, this.initialValue);
      }
   }

   @Override
   public boolean step() {
      if (this.finished) {
         return false;
      } else if (Tween.isObjectInvalid(this.target)) {
         this.finish();
         return false;
      } else if (this.getElapsedTime() < this.delay) {
         return true;
      } else {
         if (this.doContinueDelayed && this.delay >= 0.001) {
            this.initialValue = this.getField(this.target, this.field);
            this.deltaValue = AnimationUtils.subtract(this.finalValue, this.initialValue);
            this.doContinueDelayed = false;
         }

         double time = Math.min(this.getElapsedTime() - this.delay, this.duration);
         if (time < this.duration) {
            this.setField(
               this.target,
               this.field,
               this.tween.interpolateVariable(this.initialValue, this.deltaValue, time, this.duration, this.transitionType, this.easeType)
            );
            return true;
         } else {
            this.setField(this.target, this.field, this.finalValue);
            this.finish();
            return false;
         }
      }
   }

   @Override
   public void setTween(Tween tween) {
      super.setTween(tween);
      if (this.transitionType == null) {
         this.transitionType = tween.getDefaultTransition();
      }

      if (this.easeType == null) {
         this.easeType = tween.getDefaultEase();
      }
   }

   public PropertyTweener<T> asRelative() {
      this.relative = true;
      return this;
   }

   public PropertyTweener<T> setTransitionType(TransitionType transitionType) {
      this.transitionType = transitionType;
      return this;
   }

   public PropertyTweener<T> setEaseType(EaseType easeType) {
      this.easeType = easeType;
      return this;
   }

   public PropertyTweener<T> setDelay(double delay) {
      this.delay = delay;
      return this;
   }

   public PropertyTweener<T> fromCurrent() {
      this.doContinue = false;
      return this;
   }

   public PropertyTweener<T> from(T value) {
      this.initialValue = value;
      this.doContinue = false;
      return this;
   }

   public T getField(Object object, String[] field) {
      try {
         Pair<Object, Field> finalField = this.getFinalField(object, field);
         return (T)((Field)finalField.getB()).get(finalField.getA());
      } catch (Throwable var4) {
         if (var4 instanceof RuntimeException runtime) throw runtime;
         if (var4 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var4);
      }
   }

   public void setField(Object object, String[] field, T value) {
      try {
         Pair<Object, Field> finalField = this.getFinalField(object, field);
         ((Field)finalField.getB()).set(finalField.getA(), value);
      } catch (Throwable var5) {
         if (var5 instanceof RuntimeException runtime) throw runtime;
         if (var5 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var5);
      }
   }

   private Pair<Object, Field> getFinalField(Object object, String[] field) {
      Field currentField = null;
      boolean isStatic = object instanceof Class;

      for (String s : field) {
         try {
            if (currentField != null) {
               object = currentField.get(object);
            }

            if (isStatic) {
               currentField = ((Class)object).getDeclaredField(s);
               currentField.setAccessible(true);
            } else {
               currentField = object.getClass().getDeclaredField(s);
               currentField.setAccessible(true);
            }
         } catch (NoSuchFieldException var10) {
            throw new RuntimeException("Attempted to tween a nonexistent field: " + s);
         } catch (IllegalAccessException var11) {
            throw new RuntimeException(var11);
         }
      }

      return new Pair(object, currentField);
   }
}

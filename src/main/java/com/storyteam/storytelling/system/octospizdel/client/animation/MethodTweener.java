package com.storyteam.storytelling.system.octospizdel.client.animation;

import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.octospizdel.util.AnimationUtils;
import java.util.function.Consumer;

public class MethodTweener<T> extends Tweener {
   private double duration;
   private double delay;
   private TransitionType transitionType;
   private EaseType easeType;
   private T initialValue;
   private T deltaValue;
   private T finalValue;
   private Consumer<T> method;

   @Override
   public boolean step() {
      if (this.finished) {
         return false;
      } else if (Tween.isObjectInvalid(this.method)) {
         this.finish();
         return false;
      } else if (this.getElapsedTime() < this.delay) {
         return true;
      } else {
         double time = Math.min(this.getElapsedTime() - this.delay, this.duration);
         T currentValue;
         if (time < this.duration) {
            currentValue = this.tween.interpolateVariable(this.initialValue, this.deltaValue, time, this.duration, this.transitionType, this.easeType);
         } else {
            currentValue = this.finalValue;
         }

         TweenSystem.RenderThreadExecutor.runOnRenderThread(() -> this.method.accept(currentValue));
         if (time < this.duration) {
            return true;
         } else {
            this.finish();
            return false;
         }
      }
   }

   public MethodTweener(Consumer<T> method, T from, T to, double duration) {
      this.method = method;
      this.initialValue = from;
      this.deltaValue = AnimationUtils.subtract(to, from);
      this.finalValue = to;
      this.duration = duration;
   }

   public MethodTweener<T> setTransitionType(TransitionType transitionType) {
      this.transitionType = transitionType;
      return this;
   }

   public MethodTweener<T> setEaseType(EaseType easeType) {
      this.easeType = easeType;
      return this;
   }

   public MethodTweener<T> setDelay(double delay) {
      this.delay = delay;
      return this;
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
}

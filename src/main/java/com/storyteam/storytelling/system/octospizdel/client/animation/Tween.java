package com.storyteam.storytelling.system.octospizdel.client.animation;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.octospizdel.util.AnimationUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Generated;

public class Tween {
   private final List<List<Tweener>> tweeners = new ArrayList<>();
   TransitionType defaultTransition = TransitionType.LINEAR;
   EaseType defaultEase = EaseType.EASE_IN_OUT;
   long startTimestampMillis;
   int currentStep = -1;
   int loops = 1;
   int loopsDone = 0;
   float speedScale = 1.0F;
   boolean ignoreTimeScale = false;
   volatile boolean started = false;
   volatile boolean running = true;
   volatile boolean dead = false;
   boolean defaultParallel = false;
   boolean parallelEnabled = false;

   public void start() {
      TweenSystem.addTween(this);
   }

   public double getTotalTime() {
      return (double)(System.currentTimeMillis() - this.startTimestampMillis) / 1000.0;
   }

   private void startTweeners() {
      if (this.tweeners.isEmpty()) {
         this.dead = true;
         Storytelling.LOG.warn("Tween without commands, aborting.");
      } else {
         for (Tweener tweener : this.tweeners.get(this.currentStep)) {
            tweener.start();
         }
      }
   }

   private void stopInternal(boolean reset) {
      this.running = false;
      if (reset) {
         this.started = false;
         this.dead = false;
         this.startTimestampMillis = System.currentTimeMillis();
      }
   }

   protected void append(Tweener tweener) {
      tweener.setTween(this);
      if (this.parallelEnabled) {
         this.currentStep = Math.max(this.currentStep, 0);
      } else {
         this.currentStep++;
      }

      this.parallelEnabled = this.defaultParallel;
      if (this.tweeners.size() <= this.currentStep) {
         this.tweeners.add(new ArrayList<>());
      }

      this.tweeners.get(this.currentStep).add(tweener);
   }

   public void stop() {
      this.stopInternal(true);
   }

   public void pause() {
      this.stopInternal(false);
   }

   public void play() {
      if (this.dead) {
         Storytelling.LOG.warn("Can't play finished Tween, use stop() first to reset its state.");
      }

      this.running = true;
   }

   public void kill() {
      this.running = false;
      this.dead = true;
   }

   public void clear() {
      this.tweeners.clear();
   }

   public Tween setIgnoreTimeScale(boolean ignore) {
      this.ignoreTimeScale = ignore;
      return this;
   }

   public Tween setParallel(boolean parallel) {
      this.defaultParallel = parallel;
      this.parallelEnabled = parallel;
      return this;
   }

   public Tween setLoops(int loops) {
      this.loops = loops;
      return this;
   }

   public int getLoopsLeft() {
      return this.loops <= 0 ? -1 : this.loops - this.loopsDone;
   }

   public Tween setSpeedScale(float speedScale) {
      this.speedScale = speedScale;
      return this;
   }

   public Tween setTransitionType(TransitionType transitionType) {
      this.defaultTransition = transitionType;
      return this;
   }

   public Tween setEase(EaseType easeType) {
      this.defaultEase = easeType;
      return this;
   }

   public Tween parallel() {
      this.parallelEnabled = true;
      return this;
   }

   public Tween chain() {
      this.parallelEnabled = false;
      return this;
   }

   protected boolean step() {
      if (this.dead) {
         return false;
      } else if (!this.running) {
         return true;
      } else {
         if (!this.started) {
            if (this.tweeners.isEmpty()) {
               Storytelling.LOG.warn("Tween started with no tweeners :(");
               return false;
            }

            this.currentStep = 0;
            this.loopsDone = 0;
            this.startTimestampMillis = System.currentTimeMillis();
            this.startTweeners();
            this.started = true;
         }

         boolean stepActive = false;

         while (this.speedScale > 0.0F && this.running) {
            for (Tweener tweener : this.tweeners.get(this.currentStep)) {
               stepActive = tweener.step() || stepActive;
            }

            if (stepActive) {
               return true;
            }

            this.currentStep++;
            if (this.currentStep != this.tweeners.size()) {
               this.startTweeners();
               break;
            }

            this.loopsDone++;
            if (this.loopsDone == this.loops) {
               this.running = false;
               this.dead = true;
               break;
            }

            this.currentStep = 0;
            this.startTweeners();
         }

         return true;
      }
   }

   public static Tween create() {
      return new Tween();
   }

   protected double runEquation(TransitionType transitionType, EaseType easeType, double time, double initial, double delta, double duration) {
      return duration <= 0.0 ? initial + delta : transitionType.runEquation(easeType, time, initial, delta, duration);
   }

   protected <T> T interpolateVariable(T initialValue, T deltaValue, double time, double duration, TransitionType transitionType, EaseType easeType) {
      T added = AnimationUtils.add(initialValue, deltaValue);
      return AnimationUtils.lerp(initialValue, added, this.runEquation(transitionType, easeType, time, 0.0, 1.0, duration));
   }

   @Deprecated
   public <T> PropertyTweener<T> tweenProperty(Object target, String property, T to, double durationInSeconds) {
      PropertyTweener<T> tweener = new PropertyTweener<>(target, property, to, durationInSeconds);
      this.append(tweener);
      return tweener;
   }

   public IntervalTweener tweenInterval(double durationInSeconds) {
      IntervalTweener tweener = new IntervalTweener(durationInSeconds);
      this.append(tweener);
      return tweener;
   }

   public RunnableTweener tweenRunnable(Runnable runnable) {
      RunnableTweener tweener = new RunnableTweener(runnable);
      this.append(tweener);
      return tweener;
   }

   public <T> MethodTweener<T> tweenMethod(Consumer<T> method, T from, T to, double duration) {
      MethodTweener<T> tweener = new MethodTweener<>(method, from, to, duration);
      this.append(tweener);
      return tweener;
   }

   public static boolean isObjectInvalid(Object object) {
      return object == null;
   }

   @Generated
   public TransitionType getDefaultTransition() {
      return this.defaultTransition;
   }

   @Generated
   public EaseType getDefaultEase() {
      return this.defaultEase;
   }

   @Generated
   public int getLoops() {
      return this.loops;
   }

   @Generated
   public boolean isIgnoreTimeScale() {
      return this.ignoreTimeScale;
   }

   @Generated
   public boolean isStarted() {
      return this.started;
   }

   @Generated
   public boolean isRunning() {
      return this.running;
   }
}

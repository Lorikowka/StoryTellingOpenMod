package com.storyteam.storytelling.system.octospizdel.client.animation;

public class RunnableTweener extends Tweener {
   private final Runnable runnable;
   private double delay;

   @Override
   public boolean step() {
      if (this.finished) {
         return false;
      } else if (this.getElapsedTime() < this.delay) {
         return true;
      } else {
         TweenSystem.RenderThreadExecutor.runOnRenderThread(this.runnable);
         this.finish();
         return false;
      }
   }

   public RunnableTweener setDelay(double delayInSeconds) {
      this.delay = delayInSeconds;
      return this;
   }

   public RunnableTweener(Runnable runnable) {
      this.runnable = runnable;
   }
}

package com.storyteam.storytelling.system.octospizdel.client.animation;

public class IntervalTweener extends Tweener {
   private final double duration;

   @Override
   public boolean step() {
      if (this.finished) {
         return false;
      } else if (this.getElapsedTime() < this.duration) {
         return true;
      } else {
         this.finish();
         return false;
      }
   }

   public IntervalTweener(double duration) {
      this.duration = duration;
   }
}

package com.storyteam.storytelling.system.octospizdel.client.animation;

import lombok.Generated;

public abstract class Tweener {
   protected Tween tween;
   protected long startTimestampMillis;
   protected boolean finished;

   public void start() {
      this.startTimestampMillis = System.currentTimeMillis();
      this.finished = false;
   }

   public double getElapsedTime() {
      return (double)(System.currentTimeMillis() - this.startTimestampMillis) / 1000.0 * (double)this.tween.speedScale;
   }

   protected void finish() {
      this.finished = true;
   }

   public abstract boolean step();

   @Generated
   public Tween getTween() {
      return this.tween;
   }

   @Generated
   public void setTween(Tween tween) {
      this.tween = tween;
   }

   @Generated
   public boolean isFinished() {
      return this.finished;
   }
}

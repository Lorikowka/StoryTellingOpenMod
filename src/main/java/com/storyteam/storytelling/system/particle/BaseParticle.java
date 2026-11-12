package com.storyteam.storytelling.system.particle;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;

public class BaseParticle<T extends BaseParticle<T>> implements IParticle {
   public Vec3 pos;
   public Vec3 speed = Vec3.ZERO;
   float lifetime = 0.0F;
   float maxLifetime;
   boolean collides = false;
   private BiConsumer<T, Float> renderTick;
   private Predicate<T> tick;

   public T addRenderTicker(BiConsumer<? super T, Float> renderTick) {
      if (this.renderTick == null) {
         this.renderTick = (BiConsumer<T, Float>)renderTick;
         return this.cast();
      } else {
         BiConsumer<T, Float> oldRenderTick = this.renderTick;
         this.renderTick = (p, dt) -> {
            oldRenderTick.accept(p, dt);
            renderTick.accept(p, dt);
         };
         return this.cast();
      }
   }

   public T addTicker(Predicate<? super T> tick) {
      if (this.tick == null) {
         this.tick = (Predicate<T>)tick;
         return this.cast();
      } else {
         Predicate<T> oldTick = this.tick;
         this.tick = p -> oldTick.test(p) || tick.test(p);
         return this.cast();
      }
   }

   @Override
   public void renderTick(float deltaTime) {
      this.lifetime += deltaTime;
      this.pos = this.pos.add(this.speed.scale((double)deltaTime));
      if (this.renderTick != null) {
         this.renderTick.accept(this.cast(), deltaTime);
      }
   }

   @Override
   public boolean tick() {
      if (this.lifetime > this.maxLifetime) {
         return true;
      } else {
         return this.tick != null ? this.tick.test(this.cast()) : false;
      }
   }

   public final T cast() {
      return (T)this;
   }

   public final T speed(Vec3 speed) {
      this.speed = speed;
      return this.cast();
   }
}

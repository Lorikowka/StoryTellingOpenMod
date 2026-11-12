package com.storyteam.storytelling.system.time_function_anims;

import java.time.Duration;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.layer.ActiveAnimation;
import org.zeith.hammeranims.api.time.TimeFunction;
import org.zeith.hammeranims.api.time.TimeFunctionInstance;
import org.zeith.hammeranims.core.contents.time.LinearTimeFunction;

public class ClampedTimeFunction extends LinearTimeFunction {
   public double computeTime(AnimationSystem system, double sysTime, float partialTicks, ActiveAnimation animation, TimeFunctionInstance instance) {
      double rawTime = (sysTime - animation.activationTime) * (double)animation.config.speed + (double)animation.config.startTime;
      double clampDuration = this.getLengthSeconds(animation, instance);
      return Math.min(rawTime, clampDuration);
   }

   public double getLengthSeconds(ActiveAnimation animation, TimeFunctionInstance instance) {
      return instance instanceof ClampedTimeFunction.Instance ? ((ClampedTimeFunction.Instance)instance).duration : super.getLengthSeconds(animation, instance);
   }

   @NotNull
   protected TimeFunctionInstance createInstance() {
      return new ClampedTimeFunction.Instance(this, 1.0);
   }

   public ClampedTimeFunction.Instance of(double duration) {
      return new ClampedTimeFunction.Instance(this, duration);
   }

   public ClampedTimeFunction.Instance of(Duration duration) {
      return this.of((double)duration.toMillis() / 1000.0);
   }

   public static class Instance extends TimeFunctionInstance {
      protected double duration;

      public Instance(TimeFunction function, double duration) {
         super(function);
         this.duration = duration;
      }

      public CompoundTag serializeNBT() {
         CompoundTag tag = super.serializeNBT();
         tag.putDouble("Duration", this.duration);
         return tag;
      }

      public void deserializeNBT(CompoundTag nbt) {
         super.deserializeNBT(nbt);
         this.duration = nbt.getDouble("Duration");
      }
   }
}

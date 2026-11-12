package com.storyteam.storytelling.script.performer.entity.look;

import com.storyteam.storytelling.util.data.EntityContext;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractLookController<T extends Entity> {
   protected Deque<Consumer<EntityContext<T>>> onRotationEnd = new ArrayDeque<>();
   protected boolean endWork = true;
   @Nullable
   protected ILookTarget<T> target;
   protected boolean isQuick = false;

   public void setTarget(@Nullable ILookTarget<T> target) {
      if (this.target != target) {
         this.target = target;
         this.endWork = false;
      }
   }

   public void onRotationEnd(Consumer<EntityContext<T>> consumer) {
      this.onRotationEnd.add(consumer);
   }

   public abstract void lookAtTarget(EntityContext<T> var1);

   public abstract void lookAtInstantTarget(EntityContext<T> var1);

   public boolean hasEndedRotation() {
      return this.target == null || this.endWork;
   }

   protected final Vec3 calculateViewVector(float pXRot, float pYRot) {
      float f = pXRot * (float) (Math.PI / 180.0);
      float f1 = -pYRot * (float) (Math.PI / 180.0);
      float f2 = Mth.cos(f1);
      float f3 = Mth.sin(f1);
      float f4 = Mth.cos(f);
      float f5 = Mth.sin(f);
      return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
   }

   @Nullable
   @Generated
   public ILookTarget<T> getTarget() {
      return this.target;
   }

   @Generated
   public boolean isQuick() {
      return this.isQuick;
   }

   @Generated
   public void setQuick(boolean isQuick) {
      this.isQuick = isQuick;
   }
}

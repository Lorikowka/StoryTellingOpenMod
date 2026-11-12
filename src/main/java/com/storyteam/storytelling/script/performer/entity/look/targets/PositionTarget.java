package com.storyteam.storytelling.script.performer.entity.look.targets;

import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PositionTarget<T extends Entity> implements ILookTarget<T> {
   @NotNull
   final Vec3 target;
   final boolean relative;

   public PositionTarget(@NotNull Vec3 target, boolean relative) {
      this.target = target;
      this.relative = relative;
   }

   @Nullable
   @Override
   public Vec3 getTarget(EntityContext<T> context) {
      return this.relative ? context.getEntity().getEyePosition().add(this.target) : this.target;
   }
}

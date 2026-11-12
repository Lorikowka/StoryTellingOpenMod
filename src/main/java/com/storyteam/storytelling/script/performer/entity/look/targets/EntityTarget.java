package com.storyteam.storytelling.script.performer.entity.look.targets;

import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityTarget<T extends Entity> implements ILookTarget<T> {
   final Entity e;

   public EntityTarget(Entity e) {
      this.e = e;
   }

   @Nullable
   @Override
   public Vec3 getTarget(EntityContext<T> context) {
      return this.e == null ? null : this.e.getEyePosition();
   }
}

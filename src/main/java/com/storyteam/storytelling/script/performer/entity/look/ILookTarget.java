package com.storyteam.storytelling.script.performer.entity.look;

import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface ILookTarget<T extends Entity> {
   @Nullable
   Vec3 getTarget(EntityContext<T> var1);
}

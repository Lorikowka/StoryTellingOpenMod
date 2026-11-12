package com.storyteam.storytelling.script.performer.entity.look.targets;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.Comparator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class NearestTarget<T extends Entity> implements ILookTarget<T> {
   double range;
   boolean onlyStorytelling;

   public NearestTarget(double range, boolean onlyStorytelling) {
      this.range = range;
      this.onlyStorytelling = onlyStorytelling;
   }

   @Nullable
   @Override
   public Vec3 getTarget(EntityContext<T> context) {
      Level level = context.getEntity().level();
      Vec3 eyePos = context.getEntity().getEyePosition();
      AABB aabb = new AABB(eyePos, eyePos).inflate(this.range);
      List<Entity> entities = level.getEntitiesOfClass(Entity.class, aabb, e -> !this.onlyStorytelling || IStoryEntity.of(e).isStorytellingEntity());
      if (entities.isEmpty()) {
         return null;
      } else {
         entities.remove(context.getEntity());
         return entities.stream()
            .filter(e -> e.distanceToSqr(context.getEntity()) <= this.range * this.range)
            .min(Comparator.comparingDouble(e -> e.distanceToSqr(context.getEntity())))
            .<Vec3>map(Entity::getEyePosition)
            .orElse(null);
      }
   }
}

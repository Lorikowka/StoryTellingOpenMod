package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import java.util.Collection;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.util.AbortableIterationConsumer.Continuation;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntitySection.class})
public class EntitySectionMixin<T extends EntityAccess> {
   @Shadow
   @Final
   private ClassInstanceMultiMap<T> storage;

   @Inject(
      method = {"getEntities(Lnet/minecraft/world/phys/AABB;Lnet/minecraft/util/AbortableIterationConsumer;)Lnet/minecraft/util/AbortableIterationConsumer$Continuation;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetEntities(AABB pBounds, AbortableIterationConsumer<T> pConsumer, CallbackInfoReturnable<Continuation> cir) {
      for (T t : this.storage) {
         if (t instanceof BaseMob baseMob) {
            Vec3 vec3 = baseMob.getInflate_box_collide();
            double max_inf = Math.max(vec3.x(), vec3.z());
            AABB aabb = baseMob.getBoundingBox().inflate(max_inf, vec3.y(), max_inf);
            if (aabb.intersects(pBounds) && pConsumer.accept(t).shouldAbort()) {
               cir.setReturnValue(Continuation.ABORT);
               return;
            }
         } else if (t.getBoundingBox().intersects(pBounds) && pConsumer.accept(t).shouldAbort()) {
            cir.setReturnValue(Continuation.ABORT);
            return;
         }
      }

      cir.setReturnValue(Continuation.CONTINUE);
   }

   @Inject(
      method = {"getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/util/AbortableIterationConsumer;)Lnet/minecraft/util/AbortableIterationConsumer$Continuation;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private <U extends T> void onGetEntities(
      EntityTypeTest<T, U> pTest, AABB pBounds, AbortableIterationConsumer<? super U> pConsumer, CallbackInfoReturnable<Continuation> cir
   ) {
      Collection<? extends T> collection = this.storage.find(pTest.getBaseClass());
      if (collection.isEmpty()) {
         cir.setReturnValue(Continuation.CONTINUE);
      } else {
         for (T t : collection) {
            U u = (U)pTest.tryCast(t);
            if (u != null) {
               if (u instanceof BaseMob baseMob) {
                  Vec3 vec3 = baseMob.getInflate_box_collide();
                  double max_inf = Math.max(vec3.x(), vec3.z());
                  AABB aabb = baseMob.getBoundingBox().inflate(max_inf, vec3.y(), max_inf);
                  if (aabb.intersects(pBounds) && pConsumer.accept(u).shouldAbort()) {
                     cir.setReturnValue(Continuation.ABORT);
                     return;
                  }
               } else if (t.getBoundingBox().intersects(pBounds) && pConsumer.accept(u).shouldAbort()) {
                  cir.setReturnValue(Continuation.ABORT);
                  return;
               }
            }
         }

         cir.setReturnValue(Continuation.CONTINUE);
      }
   }
}

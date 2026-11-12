package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.api.data.AABBWithShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Shapes.class})
public class ShapesMixin {
   @Inject(
      method = {"create(Lnet/minecraft/world/phys/AABB;)Lnet/minecraft/world/phys/shapes/VoxelShape;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void ST_addCollissions(AABB pAabb, CallbackInfoReturnable<VoxelShape> cir) {
      if (pAabb instanceof AABBWithShape w) {
         cir.setReturnValue(w.getShape());
      }
   }
}

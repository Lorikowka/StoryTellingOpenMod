package com.storyteam.storytelling.client;

import com.storyteam.storytelling.util.MathUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class MixinThings {
   public static float cameraMixinCalculateYaw(Entity entity, float pPartialTick) {
      float yRotOld = entity.yRotO;
      float yRot = entity.getYRot();
      float yaw;
      if (Math.abs(yRotOld - yRot) > 180.0F) {
         if (yRotOld > yRot) {
            yaw = MathUtils.lerpThroughBoundaries(yRot, yRotOld, 1.0F - pPartialTick, 0.0F, 360.0F);
         } else {
            yaw = MathUtils.lerpThroughBoundaries(yRotOld, yRot, pPartialTick, 0.0F, 360.0F);
         }
      } else {
         yaw = Mth.lerp(pPartialTick, yRotOld, yRot);
      }

      return yaw;
   }
}

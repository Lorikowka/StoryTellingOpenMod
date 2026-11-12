package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.util.Necr0manthUtils;
import java.util.OptionalLong;
import net.minecraft.util.Mth;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({DimensionType.class})
public class DimensionTypeMixin {
   @Final
   @Shadow
   private OptionalLong fixedTime;

   @Overwrite
   public float timeOfDay(long pDayTime) {
      if (Necr0manthUtils.fuckFixedTime) {
         double d0 = Mth.frac((double)pDayTime / 24000.0 - 0.25);
         double d1 = 0.5 - Math.cos(d0 * Math.PI) / 2.0;
         return (float)(d0 * 2.0 + d1) / 3.0F;
      } else {
         double d0 = Mth.frac((double)this.fixedTime.orElse(pDayTime) / 24000.0 - 0.25);
         double d1 = 0.5 - Math.cos(d0 * Math.PI) / 2.0;
         return (float)(d0 * 2.0 + d1) / 3.0F;
      }
   }
}

package com.storyteam.storytelling.mixins.minecraft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.storyteam.storytelling.content.fluids.IWaterLikeFluidType;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({WaterlilyBlock.class})
public class WaterlilyBlockMixin {
   @ModifyExpressionValue(
      method = {"mayPlaceOn"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/material/FluidState;getType()Lnet/minecraft/world/level/material/Fluid;",
         ordinal = 0
      )}
   )
   private Fluid ST_mayPlaceOn(Fluid original) {
      return (Fluid)(original.getFluidType() instanceof IWaterLikeFluidType ? Fluids.WATER : original);
   }
}

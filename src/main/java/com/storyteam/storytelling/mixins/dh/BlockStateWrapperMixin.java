package com.storyteam.storytelling.mixins.dh;

import com.seibel.distanthorizons.api.enums.rendering.EDhApiBlockMaterial;
import com.storyteam.storytelling.init.FluidsST;
import loaderCommon.forge.com.seibel.distanthorizons.common.wrappers.block.BlockStateWrapper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockStateWrapper.class})
public class BlockStateWrapperMixin {
   @Final
   @Shadow
   public BlockState blockState;

   @Inject(
      method = {"calculateEDhApiBlockMaterialId"},
      at = {@At("RETURN")},
      remap = false,
      cancellable = true
   )
   private void calculateEDhApiBlockMaterialId(CallbackInfoReturnable<EDhApiBlockMaterial> cir) {
      if (this.blockState != null && FluidsST.PINK_WATER.is(this.blockState.getBlock())) {
         cir.setReturnValue(EDhApiBlockMaterial.WATER);
      }
   }
}

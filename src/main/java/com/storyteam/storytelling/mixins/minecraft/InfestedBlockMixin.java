package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.InfestedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InfestedBlock.class})
public class InfestedBlockMixin {
   @Inject(
      method = {"spawnInfestation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void hurt(ServerLevel pLevel, BlockPos pPos, CallbackInfo ci) {
      ci.cancel();
   }
}

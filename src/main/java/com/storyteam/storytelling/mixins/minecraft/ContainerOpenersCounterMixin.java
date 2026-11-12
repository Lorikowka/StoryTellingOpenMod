package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ContainerOpenersCounter.class})
public class ContainerOpenersCounterMixin {
   @Shadow
   private int openCount;

   @Inject(
      method = {"incrementOpeners"},
      at = {@At("HEAD")}
   )
   public void incrementOpeners(Player pPlayer, Level pLevel, BlockPos pPos, BlockState pState, CallbackInfo ci) {
      if (this.openCount < 0) {
         this.openCount = 0;
      }
   }
}

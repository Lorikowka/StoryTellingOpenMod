package com.storyteam.storytelling.mixins.system._core.morph;

import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import com.storyteam.storytelling.system._core.morph.client.BlockedInput;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LocalPlayer.class})
public abstract class LocalPlayerMixin implements IAnimatedPlayer {
   @Shadow
   public Input input;

   @Inject(
      method = {"aiStep"},
      at = {@At("HEAD")}
   )
   private void ST$tick(CallbackInfo ci) {
      boolean mb = this.isPlayerMotionBlocked();
      if (mb && !(this.input instanceof BlockedInput)) {
         this.input = new BlockedInput(this.input);
      } else {
         if (!mb && this.input instanceof BlockedInput bi) {
            this.input = bi.getBacking();
         }
      }
   }
}

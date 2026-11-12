package com.storyteam.storytelling.mixins.system.playerspeed;

import com.storyteam.storytelling.init.BindsST;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public class PlayerMixin {
   @Inject(
      method = {"getSpeed"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getSpeed(CallbackInfoReturnable<Float> cir) {
      Optional.ofNullable(BindsST.overrideSpeed).ifPresent(cir::setReturnValue);
   }
}

package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LocalPlayer.class})
public class LocalPlayerMixin {
   @Inject(
      method = {"drop"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onDrop(boolean pFullStack, CallbackInfoReturnable<Boolean> cir) {
      LocalPlayer localPlayer = (LocalPlayer)(Object)this;
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player == null || !player.isCreative() && localPlayer.getInventory().selected == 8) {
         cir.setReturnValue(false);
      }
   }
}

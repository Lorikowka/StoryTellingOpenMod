package com.storyteam.storytelling.mixins._client.minecraft;

import com.storyteam.storytelling.system.persona.PersonaHandler;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerInfo.class})
public class PlayerInfoMixin {
   @Inject(
      method = {"getModelName"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getModelName(CallbackInfoReturnable<String> cir) {
      cir.setReturnValue(PersonaHandler.getCurrentPersona().getInfo().getModel().name().toLowerCase());
      cir.cancel();
   }
}

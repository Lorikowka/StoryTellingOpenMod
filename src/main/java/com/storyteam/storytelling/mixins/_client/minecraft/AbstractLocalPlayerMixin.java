package com.storyteam.storytelling.mixins._client.minecraft;

import com.storyteam.storytelling.system.persona.PersonaHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.util.mcf.Resources;

@Mixin({AbstractClientPlayer.class})
public class AbstractLocalPlayerMixin {
   @Inject(
      method = {"getSkinTextureLocation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getSkinTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
      ResourceLocation resource = PersonaHandler.getCurrentPersona().getInfo().getTextureOverride();
      if (PersonaHandler.textureOverride != null && !PersonaHandler.textureOverride.isEmpty()) {
         resource = Resources.location(PersonaHandler.textureOverride);
      }

      if (resource != null) {
         cir.setReturnValue(resource);
      }
   }
}

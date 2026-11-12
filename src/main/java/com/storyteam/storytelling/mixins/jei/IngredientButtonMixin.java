package com.storyteam.storytelling.mixins.jei;

import com.storyteam.storytelling.events.base.RenderJEIEvent;
import mezz.jei.gui.recipes.RecipeTransferButton;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RecipeTransferButton.class})
public class IngredientButtonMixin {
   @Inject(
      method = {"onRelease"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private void onRelease(double mouseX, double mouseY, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderJEIEvent())) {
         ci.cancel();
      }
   }
}

package com.storyteam.storytelling.mixins._client.minecraft;

import com.storyteam.storytelling.system.octospizdel.particle.ParticleSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Screen.class})
public class ScreenMixin {
   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   private void beforeRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      Screen screen = (Screen)(Object)this;
      if (!(screen instanceof AbstractContainerScreen)) {
         ParticleSystem.renderScreenParticles(screen, guiGraphics, partialTick);
      }
   }
}

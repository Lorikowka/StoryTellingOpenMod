package com.storyteam.storytelling.mixins.jei;

import com.storyteam.storytelling.events.base.RenderJEIEvent;
import mezz.jei.gui.elements.GuiIconButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiIconButton.class})
public class GuiIconButtonMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderJEIEvent())) {
         ci.cancel();
      }
   }
}

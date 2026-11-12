package com.storyteam.storytelling.mixins.journeymap;

import com.storyteam.storytelling.events.base.RenderMiniMapEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(
   targets = {"journeymap/client/ui/minimap/MiniMap"},
   remap = false
)
public class MiniMapMixin {
   @Inject(
      method = {"drawMap(Lnet/minecraft/client/gui/GuiGraphics;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void drawMap(GuiGraphics graphics, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderMiniMapEvent())) {
         ci.cancel();
      }
   }
}

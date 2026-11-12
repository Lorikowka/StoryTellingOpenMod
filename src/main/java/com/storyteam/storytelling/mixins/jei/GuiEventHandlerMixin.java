package com.storyteam.storytelling.mixins.jei;

import com.storyteam.storytelling.events.base.RenderJEIEvent;
import mezz.jei.gui.events.GuiEventHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {GuiEventHandler.class},
   remap = false
)
public class GuiEventHandlerMixin {
   @Inject(
      method = {"onDrawScreenPost"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderJEIEvent())) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"onDrawForeground"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render2(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderJEIEvent())) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"onDrawBackgroundPost"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render3(Screen screen, GuiGraphics guiGraphics, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderJEIEvent())) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"onGuiOpen"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render3(Screen screen, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderJEIEvent())) {
         ci.cancel();
      }
   }
}

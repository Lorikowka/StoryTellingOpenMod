package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.platform.Window;
import com.storyteam.storytelling.client.screen.base.IAutoScaled;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Window.class})
public class MixinWindow {
   @Inject(
      method = {"getGuiScale"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getGuiScale(CallbackInfoReturnable<Double> cir) {
      Minecraft MC = Minecraft.getInstance();
      if (MC.screen instanceof IAutoScaled screen) {
         cir.setReturnValue(screen.defaultScale());
      }
   }

   @Inject(
      method = {"getGuiScaledHeight"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getGuiScaledHeight(CallbackInfoReturnable<Integer> cir) {
      Minecraft MC = Minecraft.getInstance();
      Window window = (Window)(Object)this;
      if (MC.screen instanceof IAutoScaled screen) {
         double var8 = screen.defaultScale();
         int height = (int)((double)window.getHeight() / var8);
         cir.setReturnValue((double)window.getHeight() / var8 > (double)height ? height + 1 : height);
      }
   }

   @Inject(
      method = {"getGuiScaledWidth"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getGuiScaledWidth(CallbackInfoReturnable<Integer> cir) {
      Minecraft MC = Minecraft.getInstance();
      Window window = (Window)(Object)this;
      if (MC.screen instanceof IAutoScaled screen) {
         double var8 = screen.defaultScale();
         int width = (int)((double)window.getWidth() / var8);
         cir.setReturnValue((double)window.getWidth() / var8 > (double)width ? width + 1 : width);
      }
   }
}

package com.storyteam.storytelling.mixins._client.minecraft;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {ForgeGui.class},
   remap = false
)
public class ForgeGuiMixin {
   @Inject(
      method = {"renderHealth"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderHotbar(int width, int height, GuiGraphics pStack, CallbackInfo ci) {
      ci.cancel();
   }

   @Inject(
      method = {"renderHealthMount"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderMount(int width, int height, GuiGraphics pStack, CallbackInfo ci) {
      ci.cancel();
   }

   @Inject(
      method = {"renderAir"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderAir(int width, int height, GuiGraphics poseStack, CallbackInfo ci) {
      ci.cancel();
   }

   @Inject(
      method = {"renderFood"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderFood(int width, int height, GuiGraphics poseStack, CallbackInfo ci) {
      ci.cancel();
   }

   @Inject(
      method = {"renderArmor"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderArmor(GuiGraphics poseStack, int width, int height, CallbackInfo ci) {
      ci.cancel();
   }

   @Inject(
      method = {"renderExperience"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderExperience(int x, GuiGraphics poseStack, CallbackInfo ci) {
      ci.cancel();
   }
}

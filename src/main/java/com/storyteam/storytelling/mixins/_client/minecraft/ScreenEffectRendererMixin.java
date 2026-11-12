package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.events.base.RenderScreenEffectEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ScreenEffectRenderer.class})
public class ScreenEffectRendererMixin {
   @Inject(
      method = {"renderScreenEffect"},
      at = {@At("HEAD")}
   )
   private static void renderTOP(Minecraft pMinecraft, PoseStack pPoseStack, CallbackInfo ci) {
      RenderScreenEffectEvent event = new RenderScreenEffectEvent(pMinecraft, pPoseStack);
      MinecraftForge.EVENT_BUS.post(event);
   }
}

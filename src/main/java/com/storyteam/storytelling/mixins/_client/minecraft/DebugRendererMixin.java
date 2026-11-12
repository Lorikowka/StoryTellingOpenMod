package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.Storytelling;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.debug.PathfindingRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DebugRenderer.class})
public class DebugRendererMixin {
   @Shadow
   @Final
   public PathfindingRenderer pathfindingRenderer;

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   public void renderEntityPaths(PoseStack matrices, BufferSource buffer, double camX, double camY, double camZ, CallbackInfo ci) {
      if (Storytelling.renderDebugPaths) {
         this.pathfindingRenderer.render(matrices, buffer, camX, camY, camZ);
      }
   }
}

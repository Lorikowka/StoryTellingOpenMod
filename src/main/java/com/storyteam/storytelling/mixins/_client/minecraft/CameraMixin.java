package com.storyteam.storytelling.mixins._client.minecraft;

import com.storyteam.storytelling.client.MixinThings;
import com.storyteam.storytelling.system._core.st_cutscene.ClientsideCutsceneHandler;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Camera.class})
public abstract class CameraMixin {
   @Shadow
   private float xRot;

   @Shadow
   protected abstract void setRotation(float var1, float var2);

   @Inject(
      method = {"setup"},
      at = {@At("TAIL")}
   )
   public void setup(BlockGetter pLevel, Entity pEntity, boolean pDetached, boolean pThirdPersonReverse, float pPartialTick, CallbackInfo ci) {
      if (ClientsideCutsceneHandler.currentData != null) {
         float yaw = MixinThings.cameraMixinCalculateYaw(pEntity, pPartialTick);
         this.setRotation(yaw, this.xRot);
      }
   }
}

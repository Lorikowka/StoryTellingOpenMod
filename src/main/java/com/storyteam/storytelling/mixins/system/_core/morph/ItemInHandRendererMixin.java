package com.storyteam.storytelling.mixins.system._core.morph;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.system._core.morph.client.IAnimatedPlayerRenderer;
import com.storyteam.storytelling.system._core.morph.client.PlayerBedrockModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemInHandRenderer.class})
public class ItemInHandRendererMixin {
   @Shadow
   @Final
   private Minecraft minecraft;

   @Inject(
      method = {"renderArmWithItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void ST$renderArmWithItem(
      AbstractClientPlayer pPlayer,
      float pPartialTicks,
      float pPitch,
      InteractionHand pHand,
      float pSwingProgress,
      ItemStack pStack,
      float pEquippedProgress,
      PoseStack pPoseStack,
      MultiBufferSource pBuffer,
      int pCombinedLight,
      CallbackInfo ci
   ) {
      if (((IAnimatedPlayerRenderer)this.minecraft.getEntityRenderDispatcher().getRenderer(pPlayer)).getAnimatedPlayerModel(pPlayer) instanceof PlayerBedrockModel pbm
         && (pHand == InteractionHand.MAIN_HAND ? pbm.getRightHandBone() : pbm.getLeftHandBone()) == null) {
         ci.cancel();
      }
   }
}

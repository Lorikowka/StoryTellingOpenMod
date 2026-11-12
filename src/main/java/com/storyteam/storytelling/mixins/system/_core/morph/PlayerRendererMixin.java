package com.storyteam.storytelling.mixins.system._core.morph;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.init.system.MorphsST;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import com.storyteam.storytelling.system._core.morph.PlayerMorph;
import com.storyteam.storytelling.system._core.morph.client.IAnimatedPlayerRenderer;
import com.storyteam.storytelling.system._core.morph.client.IPlayerModelWithRoot;
import com.storyteam.storytelling.system._core.morph.client.PlayerBedrockModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerRenderer.class})
public abstract class PlayerRendererMixin
   extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>
   implements IAnimatedPlayerRenderer {
   @Unique
   PlayerModel<AbstractClientPlayer> st$originModel;

   public PlayerRendererMixin(Context pContext, PlayerModel<AbstractClientPlayer> pModel, float pShadowRadius) {
      super(pContext, pModel, pShadowRadius);
   }

   @Inject(
      method = {"setModelProperties"},
      at = {@At("HEAD")}
   )
   private void Minitelling_render(AbstractClientPlayer pClientPlayer, CallbackInfo ci) {
      this.getAnimatedPlayerModel(pClientPlayer);
   }

   @Override
   public PlayerModel<AbstractClientPlayer> getAnimatedPlayerModel(AbstractClientPlayer pClientPlayer) {
      if (this.st$originModel == null) {
         this.st$originModel = (PlayerModel<AbstractClientPlayer>)this.getModel();
      }

      PlayerMorph tf = ((IAnimatedPlayer)pClientPlayer).getMorph();
      if (tf == MorphsST.NONE) {
         this.model = this.st$originModel;
      } else {
         this.model = (PlayerModel<AbstractClientPlayer>)MoreObjects.firstNonNull(
            PlayerBedrockModel.get(((IPlayerModelWithRoot)this.st$originModel).getPlayerModelRoot(), false, tf), this.st$originModel
         );
      }

      return (PlayerModel<AbstractClientPlayer>)this.model;
   }

   @Unique
   public PlayerModel<AbstractClientPlayer> mt$originModel() {
      if (this.st$originModel == null) {
         this.st$originModel = (PlayerModel<AbstractClientPlayer>)this.getModel();
      }

      return this.st$originModel;
   }

   @Inject(
      method = {"getRenderOffset(Lnet/minecraft/client/player/AbstractClientPlayer;F)Lnet/minecraft/world/phys/Vec3;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void st$getRenderOffset(AbstractClientPlayer pEntity, float pPartialTicks, CallbackInfoReturnable<Vec3> cir) {
      PlayerMorph tf = ((IAnimatedPlayer)pEntity).getMorph();
      if (tf != null && tf.disableVanillaAnimations(pEntity)) {
         cir.setReturnValue(Vec3.ZERO);
      }
   }

   @Inject(
      method = {"renderHand"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void ST_renderHand_Vanity(
      PoseStack pMatrixStack,
      MultiBufferSource pBuffer,
      int pCombinedLight,
      AbstractClientPlayer pPlayer,
      ModelPart pRendererArm,
      ModelPart pRendererArmwear,
      CallbackInfo ci
   ) {
      PlayerMorph tf = ((IAnimatedPlayer)pPlayer).getMorph();
      if (tf != MorphsST.NONE) {
         HumanoidArm arm = pRendererArm == ((PlayerModel)this.model).leftArm ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
         this.ST_renderArmorPieceFirstPerson(arm, pMatrixStack, pBuffer, tf, pCombinedLight, pPlayer);
         ci.cancel();
      }
   }

   @Unique
   private void ST_renderArmorPieceFirstPerson(
      HumanoidArm arm, PoseStack pPoseStack, MultiBufferSource pBuffer, PlayerMorph tf, int pPackedLight, AbstractClientPlayer pPlayer
   ) {
      PlayerBedrockModel model = PlayerBedrockModel.get(((IPlayerModelWithRoot)this.mt$originModel()).getPlayerModelRoot(), false, tf);
      ((PlayerModel)this.getModel()).copyPropertiesTo(model);
      model.prepareFirstPerson(arm);
      this.ST_renderModel(pPoseStack, pBuffer, pPackedLight, model, 1.0F, 1.0F, 1.0F, pPlayer);
   }

   @Unique
   private void ST_renderModel(
      PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, PlayerBedrockModel model, float r, float g, float b, AbstractClientPlayer pPlayer
   ) {
      model.mtRenderToBuffer(pPoseStack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F, pPlayer, false);
   }
}

package com.storyteam.storytelling.system._core.morph.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.render.entity.layer.MainHandItemRenderLayer;
import com.storyteam.storytelling.client.render.entity.layer.OffHandItemRenderLayer;
import com.storyteam.storytelling.client.texture.Material;
import com.storyteam.storytelling.client.texture.MultiDrawBufferSource;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import com.storyteam.storytelling.system._core.morph.MorphSystem;
import com.storyteam.storytelling.system._core.morph.PlayerMorph;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.HammerAnimationsApi;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.event.RefreshStaleModelsEvent;
import org.zeith.hammeranims.api.geometry.model.GeometryPose;
import org.zeith.hammeranims.api.geometry.model.IRenderableBone;
import org.zeith.hammeranims.api.geometry.model.RenderData;
import org.zeith.hammeranims.core.client.model.GeometricModelImpl;
import org.zeith.hammeranims.core.client.render.IVertexRenderer;
import org.zeith.hammeranims.core.client.render.vertex.ColorMulVertexOp;
import org.zeith.hammeranims.joml.Math;
import org.zeith.hammerlib.util.java.Cast;

@EventBusSubscriber({Dist.CLIENT})
public class PlayerBedrockModel extends PlayerModel<AbstractClientPlayer> implements IPlayerMultiBufferRenderer {
   private static final Map<PlayerMorph, PlayerBedrockModel> PLAYER_MODEL_CACHE = new HashMap<>();
   private static final Map<PlayerMorph, GeometricModelImpl> MODEL_MAP = new HashMap<>();
   protected final PlayerMorph morph;
   protected final GeometricModelImpl mesh;
   protected final Material material;
   private HumanoidArm renderingFirstPerson;
   protected RenderData data = new RenderData();

   public PlayerBedrockModel(ModelPart pRoot, boolean pSlim, PlayerMorph morph, GeometricModelImpl mesh) {
      super(pRoot, pSlim);
      this.morph = morph;
      this.mesh = mesh;
      this.material = morph.getMaterial();
   }

   public void setupAnim(AbstractClientPlayer pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
      super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
      IAnimatedPlayer player = (IAnimatedPlayer)pEntity;
      float pPartialTicks = pAgeInTicks % 1.0F;
      GeometryPose pose = this.mesh.emptyPose();
      player.getAnimationSystem().applyAnimation(pPartialTicks, pose);
      this.mesh.applyPose(pose);
      IRenderableBone root = this.mesh.getRoot();
      root.getRotation().add(0.0F, 0.0F, Math.toRadians(180.0F));
      root.getTranslation().add(0.0F, -24.0F, 0.0F);
      PlayerMorph morph = player.getMorph();
      if (morph == null || morph.getDisableVanillaAnimationsWhen() == null || !morph.getDisableVanillaAnimationsWhen().test(pEntity)) {
         this.copyVanillaRotations();
      } else if (pEntity.isFallFlying()) {
         float f1 = (float)pEntity.getFallFlyingTicks() + pPartialTicks;
         float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
         if (!pEntity.isAutoSpinAttack()) {
            root.getRotation().add(f2 * (-90.0F - pEntity.getXRot()), 0.0F, 0.0F);
         }
      }

      IRenderableBone hb = this.mesh.getBone("Head");
      if (hb != null) {
         hb.getRotation().add(-this.head.xRot, -this.head.yRot, this.head.zRot);
      }
   }

   private void copyVanillaRotations() {
      IRenderableBone b = this.mesh.getBone("RightArm");
      if (b != null) {
         b.getRotation().add(-this.rightArm.xRot, -this.rightArm.yRot, this.rightArm.zRot);
      }

      b = this.mesh.getBone("LeftArm");
      if (b != null) {
         b.getRotation().add(-this.leftArm.xRot, -this.leftArm.yRot, this.leftArm.zRot);
      }

      b = this.mesh.getBone("RightLeg");
      if (b != null) {
         b.getRotation().add(-this.rightLeg.xRot, -this.rightLeg.yRot, this.rightLeg.zRot);
      }

      b = this.mesh.getBone("LeftLeg");
      if (b != null) {
         b.getRotation().add(-this.leftLeg.xRot, -this.leftLeg.yRot, this.leftLeg.zRot);
      }

      b = this.mesh.getBone("Body");
      if (b != null) {
         b.getRotation().add(-this.body.xRot, -this.body.yRot, this.body.zRot);
      }
   }

   @Nullable
   public IRenderableBone getRightPlaceholderBone() {
      return this.mesh.getBone("Right_placeholder");
   }

   @Nullable
   public IRenderableBone getRightHandBone() {
      return this.mesh.getBone("RightHand");
   }

   @Nullable
   public IRenderableBone getLeftHandBone() {
      return this.mesh.getBone("LeftHand");
   }

   @Nullable
   public IRenderableBone getLeftPlaceholderBone() {
      return this.mesh.getBone("Left_placeholder");
   }

   @Override
   public boolean renderLayers() {
      return this.morph.isRenderLayers();
   }

   @Override
   public void mtRenderToBuffer(
      PoseStack pPoseStack,
      MultiBufferSource pBuffer,
      int pPackedLight,
      int pPackedOverlay,
      float pRed,
      float pGreen,
      float pBlue,
      float pAlpha,
      Player player,
      boolean renderItems
   ) {
      if (this.mesh != null) {
         MultiDrawBufferSource mdbs = new MultiDrawBufferSource();
         this.renderToBufferMod(pPoseStack, this.material.get(mdbs, true), pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha, player);
         mdbs.pipe(pBuffer);
         if (renderItems) {
            IRenderableBone rightHand = this.getRightPlaceholderBone();
            IRenderableBone leftHand = this.getLeftPlaceholderBone();
            if (rightHand != null) {
               MainHandItemRenderLayer.render(pPoseStack, pBuffer, pPackedLight, Cast.cast(player), rightHand);
            }

            if (leftHand != null) {
               OffHandItemRenderLayer.render(pPoseStack, pBuffer, pPackedLight, Cast.cast(player), leftHand);
            }
         }
      }
   }

   public void renderToBuffer(
      PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha
   ) {
   }

   public void renderToBufferMod(
      PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, Player player
   ) {
      GeometricModelImpl model = this.mesh;
      RenderData renderData = this.data;
      if (model != null) {
         renderData.apply(poseStack, vertexConsumer, packedLight, packedOverlay);
         IVertexRenderer vertices = renderData.getOutput().apply(new ColorMulVertexOp(renderData.red, renderData.green, renderData.blue, renderData.alpha));
         renderData.red = red;
         renderData.green = green;
         renderData.blue = blue;
         renderData.alpha = alpha;
         renderData.output = vertices;
         if (this.morph.getDisableVanillaAnimationsWhen().test(player) && this.renderingFirstPerson == null) {
            poseStack.pushPose();
            model.renderModel(renderData);
         } else {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.translate(0.0, -1.5, 0.0);
            IRenderableBone bone = model.getBone("Head");
            if (bone != null && this.renderingFirstPerson == null) {
               bone.getTranslation().set(this.head.x, this.head.y - 16.0F, this.head.z);
               bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
            }

            bone = model.getBone("Body");
            if (bone != null && this.renderingFirstPerson == null) {
               bone.getTranslation().set(this.body.x, this.body.y, this.body.z);
               if (this.crouching) {
                  bone.getTranslation().add(0.0F, -1.75F, 5.6F);
               }

               IRenderableBone b1 = model.getBone("RightArm");
               if (b1 != null) {
                  b1.getScale().set(0.0F);
               }

               b1 = model.getBone("LeftArm");
               if (b1 != null) {
                  b1.getScale().set(0.0F);
               }

               b1 = model.getBone("Head");
               if (b1 != null) {
                  b1.getScale().set(0.0F);
               }

               bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
            }

            bone = model.getBone("RightLeg");
            if (bone != null && this.renderingFirstPerson == null) {
               bone.getTranslation().set(this.rightLeg.x + 2.0F, this.rightLeg.y - 24.0F, this.rightLeg.z);
               bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
            }

            bone = model.getBone("LeftLeg");
            if (bone != null && this.renderingFirstPerson == null) {
               bone.getTranslation().set(this.leftLeg.x - 2.0F, this.leftLeg.y - 24.0F, this.leftLeg.z);
               bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
            }
         }

         if (this.renderingFirstPerson != null) {
            IRenderableBone bonex = model.getBone("RightArm");
            if (bonex != null && this.renderingFirstPerson != HumanoidArm.LEFT) {
               bonex.getScale().set(1.0F);
               bonex.getTranslation().set(this.rightArm.x + 5.0F, this.rightArm.y - 18.0F, this.rightArm.z);
               bonex.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
            }

            bonex = model.getBone("LeftArm");
            if (bonex != null && this.renderingFirstPerson != HumanoidArm.RIGHT) {
               bonex.getScale().set(1.0F);
               bonex.getTranslation().set(this.leftArm.x - 5.0F, this.leftArm.y - 18.0F, this.leftArm.z);
               bonex.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
            }
         }

         poseStack.popPose();
         this.renderingFirstPerson = null;
      }
   }

   public void prepareFirstPerson(HumanoidArm arm) {
      this.renderingFirstPerson = arm;
   }

   public static PlayerBedrockModel get(ModelPart javaPart, boolean pSlim, PlayerMorph tf) {
      return PLAYER_MODEL_CACHE.computeIfAbsent(tf, k -> new PlayerBedrockModel(javaPart, pSlim, tf, MODEL_MAP.get(tf)));
   }

   @SubscribeEvent
   public static void refreshGeo(RefreshStaleModelsEvent e) {
      MODEL_MAP.clear();

      for (PlayerMorph tf : MorphSystem.playerMorphs()) {
         IGeometryContainer m = tf.getModel();
         if (m != null) {
            MODEL_MAP.put(tf, (GeometricModelImpl)m.createModel());
         }
      }

      PLAYER_MODEL_CACHE.clear();
   }

   @Generated
   public PlayerMorph getMorph() {
      return this.morph;
   }

   @Generated
   public GeometricModelImpl getMesh() {
      return this.mesh;
   }

   static {
      HammerAnimationsApi.EVENT_BUS.register(PlayerBedrockModel.class);
   }
}

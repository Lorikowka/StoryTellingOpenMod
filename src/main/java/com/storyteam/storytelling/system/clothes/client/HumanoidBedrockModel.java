package com.storyteam.storytelling.system.clothes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.system.clothes.ArmorGeometryConfiguration;
import com.storyteam.storytelling.system.clothes.item.ItemVanityArmor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.HammerAnimationsApi;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.event.RefreshStaleModelsEvent;
import org.zeith.hammeranims.api.geometry.model.GeometryPose;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.IRenderableBone;
import org.zeith.hammeranims.api.geometry.model.RenderData;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammeranims.core.client.render.IVertexRenderer;
import org.zeith.hammeranims.core.client.render.vertex.ColorMulVertexOp;
import org.zeith.hammeranims.core.client.render.vertex.IVertexOperator;

public class HumanoidBedrockModel<T extends LivingEntity> extends HumanoidModel<T> {
   public static final int EMISSIVE_LIGHTING = 15728880;
   private final IGeometryContainer modelCtr;
   private final Map<IGeometryContainer, IGeometryContainer> modelToArmor = new HashMap<>();
   private final Map<IGeometryContainer, IGeometricModel> overrides = new HashMap<>();
   private final Map<IGeometryContainer, VanityRenderOptions> renderOverrides = new HashMap<>();
   private final VanityRenderOptions renderOptions;
   public ItemStack contextStack = ItemStack.EMPTY;
   private HumanoidArm renderingFirstPerson;
   private IGeometricModel model = IGeometricModel.EMPTY;
   private final RenderData renderData = new RenderData();

   public HumanoidBedrockModel(ArmorGeometryConfiguration configs) {
      super(LayerDefinition.create(createHumanoidMesh(), 64, 64).bakeRoot(), configs.renderOptions().renderer()::getNMC);
      this.renderOptions = configs.renderOptions();
      this.modelToArmor.putAll(configs.modelOverrides());

      for (IGeometryContainer override : configs.modelOverrides().keySet()) {
         this.overrides.put(override, IGeometricModel.EMPTY);
      }

      this.renderOverrides.putAll(configs.textureOverrides());
      HammerAnimationsApi.EVENT_BUS.addListener(this::refreshGeometry);
      this.modelCtr = configs.container();
      this.refreshGeometry(null);
   }

   public IGeometricModel getModelFor(IGeometryContainer ctr) {
      return this.overrides.getOrDefault(ctr, this.model);
   }

   @Nullable
   public IGeometryContainer getModelContainerFor(IGeometryContainer ctr) {
      return this.modelToArmor.get(ctr);
   }

   public VanityRenderOptions getOptionsFor(IGeometryContainer ctr) {
      return this.renderOverrides.getOrDefault(ctr, this.renderOptions);
   }

   private void refreshGeometry(RefreshStaleModelsEvent e) {
      this.model = this.modelCtr.createModel();
      this.overrides.clear();

      for (Entry<IGeometryContainer, IGeometryContainer> entry : this.modelToArmor.entrySet()) {
         this.overrides.put(entry.getKey(), entry.getValue().createModel());
      }
   }

   public static MeshDefinition createHumanoidMesh() {
      MeshDefinition md = new MeshDefinition();
      PartDefinition def = md.getRoot();
      def.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
      return md;
   }

   public void copyPose(GeometryPose pose) {
      this.model.applyPose(pose);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      if (entity instanceof AnimatedEntity ae && ae.rotateModel180) {
         this.model.getRoot().getRotation().add(0.0F, (float) Math.PI, 0.0F);
         this.model.getRoot().getTranslation().add(0.0F, 0.0F, 18.0F);
      }

      if (entity instanceof IAnimatedEntity iae) {
         AnimationSystem sys = iae.getAnimationSystem();
         this.model.applySystem(ageInTicks % 1.0F, sys);
      }
   }

   public void renderToBuffer(
      PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha
   ) {
      if (this.model == null) {
         this.refreshGeometry(null);
      } else {
         if (this.renderOptions.emissive()) {
            packedLight = 15728880;
         }

         poseStack.pushPose();
         poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
         poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
         poseStack.translate(0.0, -1.5, 0.0);
         if (!this.contextStack.isEmpty() && this.contextStack.getItem() instanceof ItemVanityArmor iva) {
            iva.setupColors(this.contextStack, this.renderData);
         }

         this.renderData.red = red;
         this.renderData.green = green;
         this.renderData.blue = blue;
         this.renderData.alpha = alpha;
         this.renderData
            .apply(
               poseStack,
               vertexConsumer,
               packedLight,
               packedOverlay,
               new IVertexOperator[]{new ColorMulVertexOp(this.renderData.red, this.renderData.green, this.renderData.blue, this.renderData.alpha)}
            );
         IVertexRenderer vertices = this.renderData.getOutput();
         IRenderableBone bone = this.model.getBone("Head");
         if (bone != null && this.renderingFirstPerson == null) {
            bone.getRotation().set(-this.head.xRot, -this.head.yRot, this.head.zRot);
            bone.getTranslation().set(this.head.x, this.head.y - 16.0F, this.head.z);
            bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         }

         bone = this.model.getBone("Body");
         if (bone != null && this.renderingFirstPerson == null) {
            bone.getRotation().set(-this.body.xRot, -this.body.yRot, this.body.zRot);
            bone.getTranslation().set(this.body.x, this.body.y, this.body.z);
            if (this.crouching) {
               bone.getTranslation().add(0.0F, -1.75F, 5.6F);
            }

            IRenderableBone b1 = this.model.getBone("RightArm");
            if (b1 != null) {
               b1.getScale().set(0.0F);
            }

            b1 = this.model.getBone("LeftArm");
            if (b1 != null) {
               b1.getScale().set(0.0F);
            }

            b1 = this.model.getBone("Head");
            if (b1 != null) {
               b1.getScale().set(0.0F);
            }

            bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         }

         bone = this.model.getBone("RightArm");
         if (bone != null && this.renderingFirstPerson != HumanoidArm.LEFT) {
            bone.getScale().set(1.0F);
            bone.getRotation().set(-this.rightArm.xRot, -this.rightArm.yRot, this.rightArm.zRot);
            bone.getTranslation().set(this.rightArm.x + 5.0F, this.rightArm.y - 18.0F, this.rightArm.z);
            bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         }

         bone = this.model.getBone("LeftArm");
         if (bone != null && this.renderingFirstPerson != HumanoidArm.RIGHT) {
            bone.getScale().set(1.0F);
            bone.getRotation().set(-this.leftArm.xRot, -this.leftArm.yRot, this.leftArm.zRot);
            bone.getTranslation().set(this.leftArm.x - 5.0F, this.leftArm.y - 18.0F, this.leftArm.z);
            bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         }

         bone = this.model.getBone("RightLeg");
         if (bone != null && this.renderingFirstPerson == null) {
            bone.getRotation().set(-this.rightLeg.xRot, -this.rightLeg.yRot, this.rightLeg.zRot);
            bone.getTranslation().set(this.rightLeg.x + 2.0F, this.rightLeg.y - 24.0F, this.rightLeg.z);
            bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         }

         bone = this.model.getBone("LeftLeg");
         if (bone != null && this.renderingFirstPerson == null) {
            bone.getRotation().set(-this.leftLeg.xRot, -this.leftLeg.yRot, this.leftLeg.zRot);
            bone.getTranslation().set(this.leftLeg.x - 2.0F, this.leftLeg.y - 24.0F, this.leftLeg.z);
            bone.render(poseStack, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         }

         poseStack.popPose();
         this.model.resetPose();
         this.renderingFirstPerson = null;
      }
   }

   public void prepareFirstPerson(HumanoidArm arm) {
      this.renderingFirstPerson = arm;
   }

   @Generated
   public VanityRenderOptions getRenderOptions() {
      return this.renderOptions;
   }

   @Generated
   public IGeometricModel getModel() {
      return this.model;
   }

   @Generated
   public RenderData getRenderData() {
      return this.renderData;
   }
}

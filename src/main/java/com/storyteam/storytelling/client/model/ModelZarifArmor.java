package com.storyteam.storytelling.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class ModelZarifArmor<T extends LivingEntity> extends HumanoidModel<T> {
   public ModelZarifArmor(ModelPart root) {
      super(root);
   }

   public static class Body<T extends LivingEntity> extends ModelZarifArmor<T> {
      public Body(ModelPart root) {
         super(root);
      }

      public static LayerDefinition createBodyLayer() {
         MeshDefinition meshdefinition = ArmorModelHolder.createHumanoidMesh();
         PartDefinition partdefinition = meshdefinition.getRoot();
         PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
         PartDefinition armorBody = body.addOrReplaceChild(
            "armorBody",
            CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new CubeDeformation(0.4F)),
            PartPose.offset(0.0F, 0.0F, 0.0F)
         );
         PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, 2.0F, 0.0F));
         PartDefinition bone = left_arm.addOrReplaceChild(
            "bone",
            CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -2.01F, -2.01F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)),
            PartPose.offset(0.0F, 0.0F, 0.0F)
         );
         PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, 2.0F, 0.0F));
         PartDefinition bone2 = right_arm.addOrReplaceChild(
            "bone2",
            CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-3.0F, -2.01F, -2.01F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false),
            PartPose.offset(0.0F, 0.0F, 0.0F)
         );
         return LayerDefinition.create(meshdefinition, 64, 64);
      }

      public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      }

      public void renderToBuffer(
         PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha
      ) {
         this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
         this.leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
         this.rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
      }
   }

   public static class Boots<T extends LivingEntity> extends ModelZarifArmor<T> {
      public Boots(ModelPart root) {
         super(root);
      }

      public static LayerDefinition createBodyLayer() {
         MeshDefinition meshdefinition = ArmorModelHolder.createHumanoidMesh();
         PartDefinition root = meshdefinition.getRoot();
         PartDefinition bipedLeftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));
         PartDefinition armorLeftLeg = bipedLeftLeg.addOrReplaceChild("armorLeftLeg", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 0.0F));
         PartDefinition armorLeftBoot = bipedLeftLeg.addOrReplaceChild(
            "armorLeftBoot",
            CubeListBuilder.create().texOffs(0, 24).addBox(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.48F)),
            PartPose.offset(0.0F, 0.0F, 0.0F)
         );
         PartDefinition bipedRightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F));
         PartDefinition armorRightLeg = bipedRightLeg.addOrReplaceChild("armorRightLeg", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 0.0F));
         PartDefinition armorRightBoot = bipedRightLeg.addOrReplaceChild(
            "armorRightBoot",
            CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, 7.99F, -2.01F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.48F)),
            PartPose.offset(0.0F, 0.0F, 0.0F)
         );
         return LayerDefinition.create(meshdefinition, 32, 32);
      }

      public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      }

      public void renderToBuffer(
         PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha
      ) {
         this.leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
         this.rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
      }
   }

   public static class Head<T extends LivingEntity> extends ModelZarifArmor<T> {
      public Head(ModelPart root) {
         super(root);
      }

      public static LayerDefinition createBodyLayer() {
         MeshDefinition meshdefinition = ArmorModelHolder.createHumanoidMesh();
         PartDefinition partdefinition = meshdefinition.getRoot();
         PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
         PartDefinition armorHead = head.addOrReplaceChild(
            "armorHead",
            CubeListBuilder.create()
               .texOffs(24, 24)
               .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.49F))
               .texOffs(0, 0)
               .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
               .texOffs(0, 16)
               .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)),
            PartPose.offset(0.0F, 0.0F, 0.0F)
         );
         PartDefinition HeadRaincoatRight_r1 = armorHead.addOrReplaceChild(
            "HeadRaincoatRight_r1",
            CubeListBuilder.create().texOffs(24, 8).mirror().addBox(0.0F, 0.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.49F)).mirror(false),
            PartPose.offsetAndRotation(-4.3063F, -7.8199F, 0.0F, 0.0F, 0.0F, 0.5079F)
         );
         PartDefinition HeadRaincoatLeft_r1 = armorHead.addOrReplaceChild(
            "HeadRaincoatLeft_r1",
            CubeListBuilder.create().texOffs(24, 8).addBox(-8.0F, 0.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.49F)),
            PartPose.offsetAndRotation(4.3063F, -7.8199F, 0.0F, 0.0F, 0.0F, -0.5079F)
         );
         return LayerDefinition.create(meshdefinition, 64, 64);
      }

      public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      }

      public void renderToBuffer(
         PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha
      ) {
         this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
      }
   }

   public static class Legs<T extends LivingEntity> extends ModelZarifArmor<T> {
      public Legs(ModelPart root) {
         super(root);
      }

      public static LayerDefinition createBodyLayer() {
         MeshDefinition meshdefinition = ArmorModelHolder.createHumanoidMesh();
         PartDefinition root = meshdefinition.getRoot();
         PartDefinition bipedLeftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));
         PartDefinition armorLeftLeg = bipedLeftLeg.addOrReplaceChild(
            "armorLeftLeg",
            CubeListBuilder.create().texOffs(40, 16).mirror().addBox(2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false),
            PartPose.offset(-4.0F, 0.0F, 0.0F)
         );
         PartDefinition armorLeftBoot = bipedLeftLeg.addOrReplaceChild("armorLeftBoot", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 0.0F));
         PartDefinition bipedRightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F));
         PartDefinition armorRightLeg = bipedRightLeg.addOrReplaceChild(
            "armorRightLeg",
            CubeListBuilder.create().texOffs(32, 32).mirror().addBox(-6.0F, -0.01F, -2.01F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false),
            PartPose.offset(4.0F, 0.0F, 0.0F)
         );
         PartDefinition armorRightBoot = bipedRightLeg.addOrReplaceChild("armorRightBoot", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 0.0F));
         return LayerDefinition.create(meshdefinition, 64, 64);
      }

      public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      }

      public void renderToBuffer(
         PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha
      ) {
         this.leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
         this.rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
      }
   }
}

package com.storyteam.storytelling.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.geometry.model.IRenderableBone;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

public class OffHandItemRenderLayer<T extends LivingEntity & IAnimatedEntity, M extends SimpleBedrockModel<T>> extends RenderLayer<T, M> {
   private final String itemBoneName;

   public OffHandItemRenderLayer(RenderLayerParent<T, M> renderLayerParent, String itemBoneName) {
      super(renderLayerParent);
      this.itemBoneName = itemBoneName;
   }

   public void render(
      PoseStack pose,
      MultiBufferSource buffer,
      int packedLight,
      T entity,
      float limbSwing_,
      float limbSwingAmount_,
      float partialTicks_,
      float ageInTicks_,
      float netHeadYaw_,
      float headPitch_
   ) {
      IRenderableBone bone = ((SimpleBedrockModel)this.getParentModel()).getBone(this.itemBoneName);
      if (bone != null) {
         render(pose, buffer, packedLight, entity, bone);
      }
   }

   public static <T extends LivingEntity & IAnimatedEntity> void render(
      PoseStack pose, MultiBufferSource buffer, int packedLight, T entity, IRenderableBone bone
   ) {
      ItemStack stack = entity.getOffhandItem();
      if (!stack.isEmpty()) {
         Item item = stack.getItem();
         pose.pushPose();
         bone.applyTransformTree(pose);
         pose.mulPose(Axis.YN.rotationDegrees(90.0F));
         pose.mulPose(Axis.XN.rotationDegrees(90.0F));
         pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
         if (item instanceof SwordItem && entity instanceof AnimatedEntity h) {
            AnimationSystem asys = h.getAnimationSystem();
            if (asys.isActiveAnimation("action", Animations.HUMANOID_HAND_HOLD_LEFT)) {
               pose.mulPose(Axis.ZN.rotationDegrees(-90.0F));
            } else if (asys.isActiveAnimation("action", Animations.PLACEHOLDER_POSE_SWORDFORWARD.left())) {
               pose.mulPose(Axis.ZN.rotationDegrees(-30.0F));
            }
         }

         ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
         BakedModel itemModel = ir.getModel(stack, entity.level(), entity, 0);
         ir.render(stack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, true, pose, buffer, packedLight, OverlayTexture.NO_OVERLAY, itemModel);
         pose.popPose();
      }
   }
}

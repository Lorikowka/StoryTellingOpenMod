package com.storyteam.storytelling.system.clothes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.client.render.item.ZeithTechISTER;
import com.storyteam.storytelling.system.clothes.item.ItemVanityArmor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.RenderData;

public class ItemVanityArmorRenderer extends ZeithTechISTER {
   private final ItemVanityArmor item;

   public ItemVanityArmorRenderer(ItemVanityArmor item) {
      this.item = item;
   }

   @Override
   public void renderByItem(
      ItemStack pStack, ItemDisplayContext pTransformType, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay
   ) {
      HumanoidBedrockModel<?> model = this.item.getModel();
      VanityRenderOptions renderOptions = model.getRenderOptions();
      RenderData renderData = model.getRenderData();
      IGeometricModel m = model.getModel();
      VertexConsumer renderer = ItemRenderer.getArmorFoilBuffer(pBuffer, renderOptions.renderType(), false, pStack.hasFoil());
      poseStack.pushPose();
      poseStack.scale(1.5F, 1.5F, 1.5F);
      switch (this.item.getType()) {
         case HELMET:
            poseStack.translate(0.65F, -1.15F, 0.0F);
            break;
         case CHESTPLATE:
            poseStack.translate(0.65F, -0.57F, 0.0F);
            break;
         case LEGGINGS:
            poseStack.translate(0.65F, 0.15F, 0.0F);
            break;
         case BOOTS:
            poseStack.translate(0.65F, 0.5F, 0.0F);
      }

      renderData.apply(poseStack, renderer, renderOptions.emissive() ? 15728880 : pPackedLight, OverlayTexture.NO_OVERLAY);
      this.item.setupColors(pStack, renderData);
      m.resetPose();
      m.renderModel(renderData);
      poseStack.popPose();
   }
}

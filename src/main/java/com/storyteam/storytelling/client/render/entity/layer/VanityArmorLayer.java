package com.storyteam.storytelling.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.mixins._client.minecraft.LivingEntityRendererAccessor;
import com.storyteam.storytelling.mixins._client.minecraft.RenderLayerAccessor;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.clothes.client.HumanoidBedrockModel;
import com.storyteam.storytelling.system.clothes.client.VanityRenderOptions;
import com.storyteam.storytelling.system.clothes.item.ItemVanityArmor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammeranims.api.geometry.model.GeometryPose;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.RenderData;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammeranims.core.client.render.entity.proc.CentralModelProcessor;

public class VanityArmorLayer<T extends LivingEntity & IAnimatedEntity, M extends SimpleBedrockModel<T>> extends RenderLayer<T, M> {
   public boolean renderingToro;

   public VanityArmorLayer(RenderLayerParent<T, M> pRenderer) {
      super(pRenderer);
   }

   public void render(
      PoseStack pPoseStack,
      MultiBufferSource pBuffer,
      int pPackedLight,
      T pLivingEntity,
      float pLimbSwing,
      float pLimbSwingAmount,
      float pPartialTick,
      float pAgeInTicks,
      float pNetHeadYaw,
      float pHeadPitch
   ) {
      SimpleBedrockModel<T> parentModel = (SimpleBedrockModel<T>)this.getParentModel();
      CentralModelProcessor<T> proc = parentModel.getProcessor();

      for (ItemStack armor : pLivingEntity.getArmorSlots()) {
         Item model = armor.getItem();
         if (model instanceof ItemVanityArmor) {
            ItemVanityArmor iva = (ItemVanityArmor)model;
            HumanoidBedrockModel<?> modelx = iva.getModel();
            VanityRenderOptions renderOptions = modelx.getOptionsFor(parentModel.getModelContainer());
            RenderData renderData = modelx.getRenderData();
            IGeometricModel m = modelx.getModelFor(parentModel.getModelContainer());
            GeometryPose lp = parentModel.getLastPose();
            m.applyPose(lp);
            if (!this.renderingToro) {
               proc.apply(pLivingEntity, m, pAgeInTicks % 1.0F);
            }

            VertexConsumer renderer = ItemRenderer.getArmorFoilBuffer(pBuffer, renderOptions.renderType(), false, armor.hasFoil());
            renderData.apply(pPoseStack, renderer, renderOptions.emissive() ? 15728880 : pPackedLight, OverlayTexture.NO_OVERLAY);
            iva.setupColors(armor, renderData);
            m.renderModel(renderData);
            RenderType typeWhite = renderOptions.renderTypeWhite();
            this.renderFadeable(typeWhite, m, pPoseStack, pBuffer, pLivingEntity, renderData);
            m.resetPose();
         }
      }

      this.renderingToro = false;
   }

   public void renderFadeable(RenderType typeWhite, IGeometricModel m, PoseStack pPoseStack, MultiBufferSource pBuffer, T pLivingEntity, RenderData renderData) {
      if (((RenderLayerAccessor)this).getRenderer() instanceof LivingEntityRenderer livingEntityRenderer) {
         for (RenderLayer<T, M> layer : ((LivingEntityRendererAccessor)livingEntityRenderer).<T, M>getLayers()) {
            if (layer instanceof FadeableGlowingLayer) {
               FadeableGlowingLayer<T, M> fadeableGlowingLayer = (FadeableGlowingLayer<T, M>)layer;
               if (pLivingEntity instanceof AnimatedEntity) {
                  AnimatedEntity animatedEntity = (AnimatedEntity)pLivingEntity;
                  IStoryEntity ie = IStoryEntity.of(animatedEntity);
                  if (ie.isStorytellingEntity()) {
                     Character<?> character = Character.getById(ie.getStoryName());
                     if (character == null) {
                        return;
                     }

                     EntityState<?> state = IStoryServer.get().getEntityRepository().getOrCreate(character).getActiveState();
                     if (state != null) {
                        float alpha = state.getData().getAlphaFadeable();
                        if (alpha != 0.0F) {
                           renderData.red = alpha;
                           renderData.green = alpha;
                           renderData.blue = alpha;
                           renderData.alpha = alpha;
                           VertexConsumer whiteRenderer = pBuffer.getBuffer(typeWhite);
                           renderData.apply(pPoseStack, whiteRenderer, 15728880, OverlayTexture.NO_OVERLAY);
                           m.renderModel(renderData);
                        }
                     }
                  }
               }
            }
         }
      }
   }
}

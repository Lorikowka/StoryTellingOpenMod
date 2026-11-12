package com.storyteam.storytelling.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.system.clothes.client.HumanoidBedrockModel;
import com.storyteam.storytelling.system.clothes.client.VanityRenderOptions;
import com.storyteam.storytelling.system.clothes.item.ItemVanityArmor;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.IAnimatedObject;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer.Builder;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.event.RefreshStaleModelsEvent;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.IRenderableBone;
import org.zeith.hammeranims.api.geometry.model.RenderData;
import org.zeith.hammeranims.core.init.ContainersHA;

public class EntityOnHandItemRenderer extends ZeithTechISTER {
   protected IGeometryContainer modelCtr;
   protected ResourceLocation texture;
   private IGeometricModel model;
   private final RenderData renderData = new RenderData();
   protected final Consumer<AnimationSystem> startAnimations;
   protected final Supplier<Outfit> outfit;
   protected AnimationSystem animationSystem;

   public EntityOnHandItemRenderer(IGeometryContainer modelCtr, ResourceLocation texture, Consumer<AnimationSystem> startAnimations, Supplier<Outfit> outfit) {
      this.modelCtr = modelCtr;
      this.texture = texture;
      this.startAnimations = startAnimations;
      this.outfit = outfit;
      Storytelling.BUS.addListener(this::refreshGeometry);
      Storytelling.BUS.addListener(this::clientTick);
   }

   private void refreshGeometry(RefreshStaleModelsEvent e) {
      this.model = this.modelCtr.createModel();
   }

   private void clientTick(ClientTickEvent e) {
      if (e.phase == Phase.START) {
         this.getAnimationSystem().tick();
      }
   }

   public AnimationSystem getAnimationSystem() {
      if (this.animationSystem == null) {
         this.animationSystem = AnimationSystem.builder((IAnimatedObject)ContainersHA.BILLY_TILE.create(BlockPos.ZERO, Blocks.AIR.defaultBlockState()))
            .disableSync()
            .addLayers(new Builder[]{AnimationLayer.builder("ambient")})
            .addLayers(new Builder[]{AnimationLayer.builder("action")})
            .addLayers(new Builder[]{AnimationLayer.builder("emotion")})
            .addLayers(new Builder[]{AnimationLayer.builder("hands")})
            .build();
         this.startAnimations.accept(this.animationSystem);
      }

      return this.animationSystem;
   }

   private void transform(ItemStack pStack, ItemDisplayContext pTransformType, IRenderableBone rt) {
      if (rt != null) {
         if (pTransformType == ItemDisplayContext.GUI) {
            rt.getTranslation().add(-25.0F, -6.0F, -6.0F);
            rt.getScale().mul(0.6F);
         } else {
            rt.getScale().mul(1.75F);
            rt.getRotation().add((float) (-Math.PI / 12), 0.0F, 0.5F);
            rt.getTranslation().add(-15.0F, 0.0F, 15.0F);
         }
      }
   }

   @Override
   public void renderByItem(
      ItemStack pStack, ItemDisplayContext pTransformType, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay
   ) {
      if (this.model == null) {
         this.model = this.modelCtr.createModel();
      }

      this.renderData.apply(poseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(this.texture)), pPackedLight, pPackedOverlay);
      this.renderData.red = 1.0F;
      this.renderData.green = 1.0F;
      this.renderData.blue = 1.0F;
      float pt = Minecraft.getInstance().getPartialTick();
      this.model.applySystem(pt, this.getAnimationSystem());
      this.transform(pStack, pTransformType, this.model.getRoot());
      if (pTransformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
         poseStack.translate(-1.5, 0.0, 1.0);
         poseStack.mulPose(Axis.YP.rotationDegrees(-60.0F));
         poseStack.mulPose(Axis.ZP.rotationDegrees(-20.0F));
         poseStack.mulPose(Axis.XP.rotationDegrees(-45.0F));
         poseStack.scale(1.0F, 1.0F, 1.0F);
      }

      this.model.renderModel(this.renderData);

      for (ItemStack value : this.outfit.get().items().values()) {
         if (value.getItem() instanceof ItemVanityArmor iva) {
            HumanoidBedrockModel<?> model = iva.getModel();
            IGeometricModel armodel = model.getModelFor(this.modelCtr);
            armodel.applySystem(pt, this.getAnimationSystem());
            this.transform(pStack, pTransformType, armodel.getRoot());
            VanityRenderOptions renderOptions = model.getOptionsFor(this.modelCtr);
            RenderData data = model.getRenderData();
            data.apply(poseStack, pBuffer.getBuffer(renderOptions.renderType()), pPackedLight, pPackedOverlay);
            armodel.renderModel(data);
            armodel.resetPose();
         }
      }
   }
}

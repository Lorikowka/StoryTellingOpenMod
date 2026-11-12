package com.storyteam.storytelling.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.Storytelling;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
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
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.event.RefreshStaleModelsEvent;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.IRenderableBone;
import org.zeith.hammeranims.api.geometry.model.RenderData;
import org.zeith.hammeranims.core.init.ContainersHA;
import org.zeith.hammerlib.util.java.consumers.Consumer3;

public class RenderedItemRender extends ZeithTechISTER {
   protected IGeometryContainer modelCtr;
   protected Supplier<ResourceLocation> texture;
   private IGeometricModel model;
   private final RenderData renderData = new RenderData();
   protected final Consumer3<ItemStack, ItemDisplayContext, PoseStack> transforms;
   protected final UnaryOperator<Builder> setup;
   protected AnimationSystem system;

   public RenderedItemRender(IGeometryContainer modelCtr, Supplier<ResourceLocation> texture) {
      this(modelCtr, texture, UnaryOperator.identity(), (s, t, p) -> {
      });
   }

   public RenderedItemRender(
      IGeometryContainer modelCtr,
      Supplier<ResourceLocation> texture,
      UnaryOperator<Builder> setup,
      Consumer3<ItemStack, ItemDisplayContext, PoseStack> transforms
   ) {
      this.modelCtr = modelCtr;
      this.texture = texture;
      this.setup = setup;
      this.transforms = transforms;
      Storytelling.BUS.addListener(this::refreshGeometry);
      Storytelling.BUS.addListener(this::clientTick);
   }

   private void refreshGeometry(RefreshStaleModelsEvent e) {
      this.model = this.modelCtr.createModel();
   }

   private void clientTick(ClientTickEvent e) {
      if (e.phase == Phase.START) {
         this.getSetup().tick();
      }
   }

   public AnimationSystem getSetup() {
      if (this.system == null) {
         this.system = this.setup
            .apply(AnimationSystem.builder((IAnimatedObject)ContainersHA.BILLY_TILE.create(BlockPos.ZERO, Blocks.AIR.defaultBlockState())))
            .disableSync()
            .build();
      }

      return this.system;
   }

   private void transform(ItemStack pStack, ItemDisplayContext pTransformType, IRenderableBone rt) {
      if (rt != null) {
         if (pTransformType == ItemDisplayContext.GUI) {
            rt.getTranslation().add(-22.0F, -10.0F, -6.0F);
         } else {
            if (pTransformType == ItemDisplayContext.FIXED) {
               rt.getTranslation().add(0.0F, 0.0F, 8.0F);
               rt.getRotation().add((float) (Math.PI / 9), 0.0F, 0.0F);
            }

            rt.getScale().mul(1.75F);
            if (pTransformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || pTransformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
               rt.getTranslation().add(0.0F, 0.0F, 16.0F);
               rt.getRotation().add((float) (-Math.PI / 4), -0.87266463F, 0.0F);
            }

            if (pTransformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
               rt.getTranslation().add(-16.0F, 0.0F, 16.0F);
               rt.getRotation().add((float) (-Math.PI / 4), (float) (Math.PI * 2.0 / 9.0), 0.0F);
            }

            if (pTransformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
               rt.getRotation().add((float) (-Math.PI / 4), (float) (Math.PI * 11.0 / 9.0), 0.0F);
            }
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

      poseStack.pushPose();
      this.transforms.accept(pStack, pTransformType, poseStack);
      this.renderData.apply(poseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(this.texture.get())), pPackedLight, pPackedOverlay);
      this.renderData.red = 1.0F;
      this.renderData.green = 1.0F;
      this.renderData.blue = 1.0F;
      float pt = Minecraft.getInstance().getPartialTick();
      this.model.applySystem(pt, this.getSetup());
      this.transform(pStack, pTransformType, this.model.getRoot());
      this.model.renderModel(this.renderData);
      poseStack.popPose();
   }
}

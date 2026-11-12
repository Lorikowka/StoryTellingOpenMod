package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import lombok.Generated;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.zeith.hammeranims.api.HammerAnimationsApi;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.IAnimatedObject;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.event.RefreshStaleModelsEvent;
import org.zeith.hammeranims.api.geometry.model.GeometryPose;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.IRenderableBone;
import org.zeith.hammeranims.api.geometry.model.RenderData;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammeranims.core.client.render.entity.proc.CentralModelProcessor;
import org.zeith.hammeranims.core.client.render.entity.proc.IProcessor;

public class SimpleBedrockModel<T extends Entity & IAnimatedEntity> extends EntityModel<T> {
   protected final CentralModelProcessor<T> processor = new CentralModelProcessor();
   protected final IGeometryContainer modelContainer;
   protected IGeometricModel model;
   protected final RenderData renderData = new RenderData();
   protected GeometryPose lastPose = new GeometryPose(o -> false);
   public MultiBufferSource buffers;
   public LivingEntity entity;

   public SimpleBedrockModel(ModelConfiguration config) {
      super(config.renderType());
      this.modelContainer = config.model();
      HammerAnimationsApi.EVENT_BUS.addListener(this::refreshGeometry);
   }

   public SimpleBedrockModel<T> withModelProcessor(IProcessor<? super T> processor) {
      this.processor.add(processor);
      return this;
   }

   private void refreshGeometry(RefreshStaleModelsEvent e) {
      this.model = this.modelContainer.createModel();
   }

   public void applyAnimSystem(float partialTicks, AnimationSystem system) {
      GeometryPose pose = this.model.emptyPose();
      system.applyAnimation(partialTicks, pose);
      this.model.applyPose(pose);
      this.lastPose = pose;
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      boolean pause = false;
      if (entityIn instanceof AnimatedEntity ae && ae.isPaused()) {
         pause = true;
      }

      this.applyAnimSystem(pause ? 0.0F : ageInTicks % 1.0F, ((IAnimatedObject)entityIn).getAnimationSystem());
      this.processor.apply(entityIn, this.model, ageInTicks % 1.0F);
   }

   public void renderToBuffer(
      PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha
   ) {
      this.renderData.apply(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
      this.renderData.red = red;
      this.renderData.green = green;
      this.renderData.blue = blue;
      this.model.renderModel(this.renderData);
   }

   public IRenderableBone getRoot() {
      return this.model.getRoot();
   }

   public IRenderableBone getBone(String bone) {
      return this.model.getBone(bone);
   }

   public void applyAnimations(AnimationSystem sys, float pt) {
      if (sys == null) {
         this.model.resetPose();
      } else {
         this.model.applySystem(pt, sys);
      }
   }

   @Generated
   public CentralModelProcessor<T> getProcessor() {
      return this.processor;
   }

   @Generated
   public IGeometryContainer getModelContainer() {
      return this.modelContainer;
   }

   @Generated
   public IGeometricModel getModel() {
      return this.model;
   }

   @Generated
   public RenderData getRenderData() {
      return this.renderData;
   }

   @Generated
   public GeometryPose getLastPose() {
      return this.lastPose;
   }

   @Generated
   public MultiBufferSource getBuffers() {
      return this.buffers;
   }

   @Generated
   public LivingEntity getEntity() {
      return this.entity;
   }
}

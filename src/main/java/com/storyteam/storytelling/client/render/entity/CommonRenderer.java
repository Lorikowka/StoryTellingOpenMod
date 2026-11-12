package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import com.storyteam.storytelling.system.fight.Collider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammeranims.core.client.render.entity.proc.IProcessor;

@OnlyIn(Dist.CLIENT)
public class CommonRenderer<T extends LivingEntity & IAnimatedEntity> extends LivingEntityRenderer<T, SimpleBedrockModel<T>> {
   private final ResourceLocation texture;
   private ResourceLocation lastUsedTexture;
   private boolean always_renderer;

   public <M extends SimpleBedrockModel<T>> CommonRenderer(Context manager, M model, float shadowSize, ResourceLocation texture, boolean alwaysRenderer) {
      super(manager, model, shadowSize);
      this.texture = texture;
      this.always_renderer = alwaysRenderer;
   }

   public <M extends SimpleBedrockModel<T>> CommonRenderer(Context manager, M model, float shadowSize, ResourceLocation texture) {
      super(manager, model, shadowSize);
      this.texture = texture;
      this.always_renderer = false;
   }

   public CommonRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, boolean alwaysRenderer) {
      super(manager, new SimpleBedrockModel(modelConfiguration), shadowSize);
      this.texture = texture;
      this.always_renderer = alwaysRenderer;
   }

   public CommonRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture) {
      super(manager, new SimpleBedrockModel(modelConfiguration), shadowSize);
      this.texture = texture;
      this.always_renderer = false;
   }

   public boolean shouldRender(T pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
      return this.always_renderer || super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
   }

   public void render(
      @NotNull T pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight
   ) {
      ((SimpleBedrockModel)this.model).entity = pEntity;
      ((SimpleBedrockModel)this.model).buffers = pBuffer;
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
      if (pEntity instanceof AnimatedEntityLikeHumanoid entity
         && entity.getAttackAnimAction() != null
         && entity.getAttackAnimAction().is_active()
         && Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()
         && !entity.isInvisible()
         && !Minecraft.getInstance().showOnlyReducedInfo()) {
         Collider.draw(pMatrixStack, pBuffer, entity, entity.getAttackAnimAction().getModelBoneFList(), (float)pPackedLight);
      }

      if (pEntity instanceof AnimatedEntity animatedEntity && animatedEntity.onLead) {
         this.renderLeash(pEntity, pPartialTicks, pMatrixStack, pBuffer, true);
      }
   }

   private <E extends Entity> void renderLeash(T pEntityLiving, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, boolean wire) {
      pMatrixStack.pushPose();
      Vec3 vec3;
      if (wire && pEntityLiving instanceof AnimatedEntity animatedEntity) {
         vec3 = animatedEntity.connectPos;
      } else {
         vec3 = new Vec3(0.0, (double)pEntityLiving.getEyeHeight(), (double)(pEntityLiving.getBbWidth() * 0.4F));
      }

      double d0 = (double)(Mth.lerp(pPartialTicks, pEntityLiving.yBodyRotO, pEntityLiving.yBodyRot) * (float) (Math.PI / 180.0)) + (Math.PI / 2);
      Vec3 vec31 = pEntityLiving.getLeashOffset(pPartialTicks);
      double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
      double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
      double d3 = Mth.lerp((double)pPartialTicks, pEntityLiving.xo, pEntityLiving.getX()) + d1;
      double d4 = Mth.lerp((double)pPartialTicks, pEntityLiving.yo, pEntityLiving.getY()) + vec31.y;
      double d5 = Mth.lerp((double)pPartialTicks, pEntityLiving.zo, pEntityLiving.getZ()) + d2;
      pMatrixStack.translate(d1, vec31.y - 0.4, d2 - 0.1);
      float f = (float)(vec3.x - d3);
      float f1 = (float)(vec3.y - d4);
      float f2 = (float)(vec3.z - d5);
      float f3 = 0.025F;
      VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
      Matrix4f matrix4f = pMatrixStack.last().pose();
      float f4 = (float)Mth.fastInvSqrt((double)(f * f + f2 * f2)) * 0.025F / 2.0F;
      float f5 = f2 * f4;
      float f6 = f * f4;
      Vec3 pos = pEntityLiving.getEyePosition(pPartialTicks);
      BlockPos blockpos = new BlockPos(Mth.floor(pos.x), Mth.floor(pos.y), Mth.floor(pos.z));
      BlockPos blockpos1 = new BlockPos(Mth.floor(pos.x), Mth.floor(pos.y), Mth.floor(pos.z));
      int i = this.getBlockLightLevel(pEntityLiving, blockpos);
      int j = this.getBlockLightLevel(pEntityLiving, blockpos);
      int k = pEntityLiving.level().getBrightness(LightLayer.SKY, blockpos);
      int l = pEntityLiving.level().getBrightness(LightLayer.SKY, blockpos1);

      for (int i1 = 0; i1 <= 24; i1++) {
         addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
      }

      for (int j1 = 24; j1 >= 0; j1--) {
         addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
      }

      pMatrixStack.popPose();
   }

   private static void addVertexPair(
      VertexConsumer p_174308_,
      Matrix4f p_174309_,
      float p_174310_,
      float p_174311_,
      float p_174312_,
      int p_174313_,
      int p_174314_,
      int p_174315_,
      int p_174316_,
      float p_174317_,
      float p_174318_,
      float p_174319_,
      float p_174320_,
      int p_174321_,
      boolean p_174322_
   ) {
      float f = (float)p_174321_ / 24.0F;
      int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
      int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
      int k = LightTexture.pack(i, j);
      float f1 = p_174321_ % 2 == (p_174322_ ? 1 : 0) ? 0.7F : 1.0F;
      float f2 = 0.5F * f1;
      float f3 = 0.4F * f1;
      float f4 = 0.3F * f1;
      float f5 = p_174310_ * f;
      float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
      float f7 = p_174312_ * f;
      p_174308_.vertex(p_174309_, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
      p_174308_.vertex(p_174309_, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
   }

   protected void setupRotations(@NotNull T pEntityLiving, @NotNull PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
      super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
      pMatrixStack.translate(0.0F, 1.5F, 0.0F);
      pMatrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
      pMatrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
   }

   public void addModelProcessor(IProcessor<T> proc) {
      ((SimpleBedrockModel)this.getModel()).withModelProcessor(proc);
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull T entity) {
      StoryData data = StoryData.of(entity);
      if (data != null && data.texture.get() != null) {
         this.lastUsedTexture = (ResourceLocation)data.texture.get();
         return IStoryEntity.transformTexture(this.lastUsedTexture, entity);
      } else {
         return data == null && this.lastUsedTexture != null && !entity.isAlive()
            ? IStoryEntity.transformTexture(this.lastUsedTexture, entity)
            : IStoryEntity.transformTexture(this.texture, entity);
      }
   }
}

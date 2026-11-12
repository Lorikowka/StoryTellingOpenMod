package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.processor.EyeGazeProcessor;
import com.storyteam.storytelling.client.render.entity.processor.ViewDirectionProcessor;
import com.storyteam.storytelling.content.entities.SkyEyeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkyEyeRenderer<T extends SkyEyeEntity> extends CommonRenderer<T> {
   protected final ResourceLocation emissive = St.tx("entity/sky_eye_emmisive");
   protected int renderPass = 1;

   public SkyEyeRenderer(Context manager, ModelConfiguration model, float shadowSize, ResourceLocation texture) {
      super(manager, model, shadowSize, texture, true);
      this.addModelProcessor(new ViewDirectionProcessor("Head", () -> false));
      this.addModelProcessor(new EyeGazeProcessor<>());
   }

   public boolean shouldRender(T livingEntity, Frustum camera, double camX, double camY, double camZ) {
      return true;
   }

   protected int getBlockLightLevel(T entity, BlockPos pos) {
      return 15;
   }

   protected void renderNameTag(T pEntity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
   }

   @Nullable
   protected RenderType getRenderType(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
      return this.renderPass == 1 ? RenderType.entityTranslucentEmissive(this.emissive) : RenderType.entityCutoutNoCull(this.getTextureLocation(livingEntity));
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull T entity) {
      return this.renderPass == 2 ? this.emissive : super.getTextureLocation(entity);
   }

   public void render(@NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
      poseStack.pushPose();
      RenderSystem.disableCull();
      RenderSystem.enableBlend();
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player != null) {
         Vec3 spawnPos = entity.getRenderPos();
         double px = player.xOld + (player.getX() - player.xOld) * (double)partialTicks;
         double py = player.yOld + (player.getY() - player.yOld) * (double)partialTicks;
         double pz = player.zOld + (player.getZ() - player.zOld) * (double)partialTicks;
         Vec3 playerPos = new Vec3(px, py, pz);
         Vec3 flatDir = new Vec3(spawnPos.x - playerPos.x, spawnPos.y - playerPos.y, spawnPos.z - playerPos.z);
         double flatDist = flatDir.length();
         double maxDist = 500.0;
         if (flatDist > maxDist) {
            flatDir = flatDir.normalize().scale(maxDist);
            flatDist = maxDist;
         }

         float yOffset = -entity.getEyeHeight();
         Vec3 renderPos = playerPos.add(flatDir).add(0.0, (double)(-yOffset), 0.0);
         poseStack.translate(renderPos.x - playerPos.x, renderPos.y - playerPos.y, renderPos.z - playerPos.z);
         float defScale = 0.03F;
         float scale = Math.max(10.0F, 0.03F * (float)flatDist);
         poseStack.scale(scale, scale, scale);
         this.renderPass = 0;
         super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
         this.renderPass = 1;
         super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
         poseStack.popPose();
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
      }
   }
}

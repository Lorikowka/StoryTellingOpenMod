package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FlytrapRenderer extends CommonRenderer<BaseMob> {
   private SimpleBedrockModel<AnimatedEntity> leavesModel = new SimpleBedrockModel(new ModelConfiguration(EntityModelsST.FLYTRAP_LEAVES));

   public FlytrapRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, boolean alwaysRenderer) {
      super(manager, new SimpleBedrockModel(modelConfiguration), shadowSize, texture);
   }

   public void render(BaseMob entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      poseStack.pushPose();
      this.leavesModel.applyAnimations(entity.getAnimationSystem(), partialTicks);
      Minecraft minecraft = Minecraft.getInstance();
      boolean flag = this.isBodyVisible(entity);
      boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
      boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
      RenderType renderType = this.getRenderType(entity, flag, flag1, flag2);
      int i = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
      this.leavesModel.renderToBuffer(poseStack, bufferSource.getBuffer(renderType), packedLight, i, 1.0F, 1.0F, 1.0F, 1.0F);
      poseStack.popPose();
      poseStack.pushPose();
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
      poseStack.popPose();
   }
}

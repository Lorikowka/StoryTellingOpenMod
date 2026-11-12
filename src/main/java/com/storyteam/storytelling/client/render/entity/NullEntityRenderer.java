package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.zeith.hammerlib.util.mcf.Resources;

public class NullEntityRenderer<T extends Entity> extends EntityRenderer<T> {
   public NullEntityRenderer(Context p_i46179_1_) {
      super(p_i46179_1_);
   }

   public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
   }

   public ResourceLocation getTextureLocation(T pEntity) {
      return Resources.location("minecraft", "null");
   }
}

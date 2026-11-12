package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;

public interface IAlphaRenderer {
   void renderAlpha(EntityModel var1, PoseStack var2, VertexConsumer var3, int var4, int var5, float var6, float var7, float var8, float var9);
}

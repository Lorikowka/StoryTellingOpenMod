package com.storyteam.storytelling.system._core.morph.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

public interface IPlayerMultiBufferRenderer {
   boolean renderLayers();

   void mtRenderToBuffer(PoseStack var1, MultiBufferSource var2, int var3, int var4, float var5, float var6, float var7, float var8, Player var9, boolean var10);
}

package com.storyteam.storytelling.system._core.structio.entity.client;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.storyteam.storytelling.system._core.structio.entity.StructureEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class RenderStructureEntity extends EntityRenderer<StructureEntity> {
   private final Map<RenderType, Supplier<ShaderInstance>> shaders = new HashMap<>();

   public RenderStructureEntity(Context pContext) {
      super(pContext);
      this.shaders.put(RenderType.solid(), GameRenderer::getRendertypeSolidShader);
      this.shaders.put(RenderType.cutoutMipped(), GameRenderer::getRendertypeCutoutMippedShader);
      this.shaders.put(RenderType.cutout(), GameRenderer::getRendertypeCutoutShader);
      this.shaders.put(RenderType.translucent(), GameRenderer::getRendertypeTranslucentShader);
      this.shaders.put(RenderType.tripwire(), GameRenderer::getRendertypeTripwireShader);
   }

   public void render(StructureEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
      super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
      String structId = pEntity.getStructure();
      if (structId != null && !structId.isBlank()) {
         CompletableFuture<RenderedStructure> frs = StructuresInVRAM.getOrRender(pEntity.level().registryAccess(), structId);
         if (frs.isDone()) {
            RenderedStructure struct = frs.getNow(null);
            if (struct != null) {
               pPoseStack.pushPose();

               for (Entry<RenderType, VertexBuffer> entry : struct.getBuffers().entrySet()) {
                  RenderType key = entry.getKey();
                  Supplier<ShaderInstance> shader = this.shaders.get(key);
                  if (shader != null) {
                     VertexBuffer value = entry.getValue();
                     ShaderInstance sdr = shader.get();
                     Uniform uniform = sdr.CHUNK_OFFSET;
                     value.bind();
                     key.setupRenderState();
                     if (uniform != null) {
                     }

                     value.drawWithShader(pPoseStack.last().pose(), RenderSystem.getProjectionMatrix(), sdr);
                     key.clearRenderState();
                  }
               }

               pPoseStack.popPose();
            }
         }
      }
   }

   public ResourceLocation getTextureLocation(StructureEntity pEntity) {
      return InventoryMenu.BLOCK_ATLAS;
   }

   public boolean shouldRender(StructureEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
      return true;
   }
}

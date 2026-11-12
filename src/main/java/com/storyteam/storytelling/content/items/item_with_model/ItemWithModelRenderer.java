package com.storyteam.storytelling.content.items.item_with_model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemWithModelRenderer extends BlockEntityWithoutLevelRenderer {
   protected SimpleBedrockModel<?> model;
   protected List<ResourceLocation> textures;

   public ItemWithModelRenderer(ModelContainer container) {
      this(container, List.of(container.texture()));
   }

   public ItemWithModelRenderer(ModelContainer container, List<ResourceLocation> textures) {
      super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
      this.model = container.model();
      this.textures = new ArrayList<>(textures);
   }

   public void setTextures(List<ResourceLocation> textures) {
      this.textures = new ArrayList<>(textures);
   }

   public void renderByItem(ItemStack item, ItemDisplayContext transform, PoseStack matrices, MultiBufferSource src, int light, int overlay) {
      matrices.pushPose();
      matrices.translate(0.5, 0.5, 0.5);
      if (transform == ItemDisplayContext.GUI) {
         matrices.mulPose(Axis.XN.rotationDegrees(-45.0F));
         matrices.mulPose(Axis.YP.rotationDegrees(135.0F));
      }

      for (int i = 0; i < this.textures.size(); i++) {
         ResourceLocation texture = this.textures.get(i);
         if (texture != null) {
            matrices.pushPose();
            matrices.translate(0.0F, 0.0F, (float)(-i) * 5.0E-4F);
            this.model.renderToBuffer(matrices, src.getBuffer(RenderType.entityTranslucent(texture)), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrices.popPose();
         }
      }

      matrices.popPose();
   }
}

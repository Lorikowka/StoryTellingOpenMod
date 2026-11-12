package com.storyteam.storytelling.content.items.base;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.content.items.item_with_model.ItemWithModelRenderer;
import com.storyteam.storytelling.content.items.item_with_model.ModelContainer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.api.fml.IRegisterListener;

public class CommonCustomBlockItem extends BlockItem implements IRegisterListener {
   String texture;
   IGeometryContainer model;
   float scale = 0.2F;
   Vec3 offsetFromCenter = Vec3.ZERO;
   private Runnable createModel;

   public CommonCustomBlockItem(Block block, IGeometryContainer model, String texture) {
      super(block, new Properties());
      this.texture = texture;
      this.model = model;
   }

   public CommonCustomBlockItem(Block block, IGeometryContainer model, String texture, float scale) {
      super(block, new Properties());
      this.texture = texture;
      this.model = model;
      this.scale = scale;
   }

   public CommonCustomBlockItem(Block block, IGeometryContainer model, String texture, Vec3 offsetFromCenter) {
      super(block, new Properties());
      this.texture = texture;
      this.model = model;
      this.offsetFromCenter = offsetFromCenter;
   }

   public CommonCustomBlockItem(Block block, IGeometryContainer model, String texture, float scale, Vec3 offsetFromCenter) {
      super(block, new Properties());
      this.texture = texture;
      this.model = model;
      this.scale = scale;
      this.offsetFromCenter = offsetFromCenter;
   }

   public CommonCustomBlockItem(Block block, IGeometryContainer model, ResourceLocation textureLocation, float scale) {
      this(block, model, textureLocation.getPath(), scale);
   }

   public CommonCustomBlockItem(Block block, IGeometryContainer model, ResourceLocation textureLocation, float scale, Vec3 offsetFromCenter) {
      this(block, model, textureLocation, scale, offsetFromCenter, false);
   }

   public CommonCustomBlockItem(
      Block block, IGeometryContainer model, ResourceLocation textureLocation, float scale, Vec3 offsetFromCenter, boolean replaceTexture
   ) {
      this(
         block,
         model,
         replaceTexture ? textureLocation.getPath().replace(".png", "").replace("textures/", "") : textureLocation.getPath(),
         scale,
         offsetFromCenter
      );
   }

   public void onPostRegistered() {
      if (this.createModel != null) {
         this.createModel.run();
      }
   }

   public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
      super.initializeClient(consumer);
      final Supplier<CommonCustomBlockItem.Renderer> renderer = Suppliers.memoize(
         () -> new CommonCustomBlockItem.Renderer(ModelContainer.create(this.model, this.texture), this.scale, this.offsetFromCenter)
      );
      consumer.accept(new IClientItemExtensions() {
         public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer.get();
         }
      });
      this.createModel = renderer::get;
   }

   static class Renderer extends ItemWithModelRenderer {
      float scale;
      Vec3 offsetFromCenter = Vec3.ZERO;

      public Renderer(ModelContainer container, float scale, Vec3 offsetFromCenter) {
         super(container);
         this.scale = scale;
         this.offsetFromCenter = offsetFromCenter;
      }

      @Override
      public void renderByItem(ItemStack item, ItemDisplayContext transform, PoseStack matrices, MultiBufferSource src, int light, int overlay) {
         if (this.model.getModel() != null) {
            matrices.pushPose();
            matrices.scale(this.scale, this.scale, this.scale);
            float offsetX = (1.0F - this.scale) / this.scale / 2.0F;
            float offsetY = (1.0F - this.scale) / this.scale / 2.0F;
            float offsetZ = (1.0F - this.scale) / this.scale / 2.0F;
            matrices.translate(
               (double)offsetX + this.offsetFromCenter.x(), (double)offsetY + this.offsetFromCenter.y(), (double)offsetZ + this.offsetFromCenter.z()
            );
            super.renderByItem(item, transform, matrices, src, light, overlay);
            matrices.popPose();
         }
      }
   }
}

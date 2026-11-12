package com.storyteam.storytelling.content.items.item_with_model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.Storytelling;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.TickEvent.Phase;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.IAnimatedObject;
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.api.fml.IRegisterListener;

public class ItemWithModelAnimated extends ItemWithModel implements IRegisterListener, IAnimatedObject {
   protected AnimationSystem system;

   public ItemWithModelAnimated(Properties pProperties, ModelContainer container) {
      super(pProperties, container);
   }

   public void onPostRegistered(ResourceLocation id) {
      Storytelling.BUS.addListener((net.minecraftforge.event.TickEvent.ClientTickEvent e) -> {
         if (e.phase == Phase.START) {
            if (this.getAnimatedObjectWorld() != null) {
               if (this.system == null) {
                  this.system = AnimationSystem.create(this);
               }

               this.system.tick();
            } else if (this.system != null) {
               this.system = null;
            }
         }
      });
   }

   public void setupSystem(Builder builder) {
      builder.disableSync().addLayers(new String[]{"action"});
   }

   public AnimationSystem getAnimationSystem() {
      return this.system;
   }

   public IObjectSource<?> getAnimationSource() {
      return null;
   }

   public Level getAnimatedObjectWorld() {
      return Minecraft.getInstance().level;
   }

   public Vec3 getAnimatedObjectPosition() {
      return Vec3.ZERO;
   }

   @Override
   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      super.initializeClient(consumer);
      consumer.accept(new IClientItemExtensions() {
         private ItemWithModelRenderer renderer;

         public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (this.renderer == null) {
               this.renderer = ItemWithModelAnimated.this.new ItemWithModelRendererAnimated(ItemWithModelAnimated.this.model);
            }

            return this.renderer;
         }
      });
   }

   public class ItemWithModelRendererAnimated extends ItemWithModelRenderer {
      public ItemWithModelRendererAnimated(ModelContainer container) {
         super(container);
      }

      @Override
      public void renderByItem(ItemStack item, ItemDisplayContext transform, PoseStack pose, MultiBufferSource src, int light, int overlay) {
         pose.pushPose();
         if (transform == ItemDisplayContext.FIXED) {
            pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
            pose.mulPose(Axis.YP.rotationDegrees(90.0F));
            pose.scale(1.5F, 1.5F, 1.5F);
            pose.translate(-1.0F, -0.8F, 0.0F);
            if (item.getEntityRepresentation() instanceof ItemFrame iframe) {
               iframe.setInvisible(true);
            }
         }

         this.model.applyAnimations(ItemWithModelAnimated.this.system, Minecraft.getInstance().getPartialTick());
         super.renderByItem(item, transform, pose, src, light, overlay);
         pose.popPose();
      }
   }
}

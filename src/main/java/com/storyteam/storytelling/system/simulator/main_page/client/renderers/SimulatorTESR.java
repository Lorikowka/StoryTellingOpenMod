package com.storyteam.storytelling.system.simulator.main_page.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.tile.AnimatedBESR;
import com.storyteam.storytelling.system.simulator.main_page.MainTabStorage;
import com.storyteam.storytelling.system.simulator.main_page.MainTabType;
import com.storyteam.storytelling.system.simulator.main_page.init.MainTabModels;
import com.storyteam.storytelling.system.simulator.main_page.tiles.SimulatorTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.zeith.hammeranims.api.animsys.AnimationSystem;

@OnlyIn(Dist.CLIENT)
public class SimulatorTESR extends AnimatedBESR<SimulatorTile> {
   private final ResourceLocation texture = St.tx("block/system/simulator/main_tab/simulator");
   private final ResourceLocation textureEmissive = St.tx("block/system/simulator/main_tab/simulator_emissive");
   private final ResourceLocation textureBlocked = St.tx("block/system/simulator/main_tab/simulator_gray");

   public SimulatorTESR(Context context) {
      super(new ModelConfiguration(MainTabModels.SIMULATOR), RenderType::entityTranslucentCull);
   }

   protected ResourceLocation getTexture(SimulatorTile tileEntityIn) {
      return this.texture;
   }

   public void render(SimulatorTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
      if (tile.parent == null) {
         RenderSystem.enableBlend();
         Direction direction = (Direction)tile.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
         poseStack.translate(0.5, 0.0, 0.5);
         poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(new Vector3f(0.0F, 1.0F, 0.0F), 180.0F - direction.toYRot()));
         poseStack.translate(-0.5, 0.0, -0.5);
         AnimationSystem sys = tile.getAnimationSystem();
         this.model.applyAnimations(sys, partialTicks);
         this.processModel(tile, partialTicks);
         poseStack.pushPose();
         poseStack.translate(0.5F, 0.0F, 0.5F);
         if (MainTabStorage.isBlocked(MainTabType.MAIN)) {
            this.model
               .renderToBuffer(
                  poseStack, bufferIn.getBuffer(RenderType.entityCutout(this.textureBlocked)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F
               );
         } else {
            this.model
               .renderToBuffer(poseStack, bufferIn.getBuffer(RenderType.entityCutout(this.texture)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model
               .renderToBuffer(
                  poseStack,
                  bufferIn.getBuffer(RenderType.entityTranslucentEmissive(this.textureEmissive)),
                  combinedLightIn,
                  combinedOverlayIn,
                  1.0F,
                  1.0F,
                  1.0F,
                  1.0F
               );
         }

         poseStack.popPose();
         RenderSystem.disableBlend();
      }
   }

   public int getViewDistance() {
      return 128;
   }
}

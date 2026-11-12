package com.storyteam.storytelling.system.rts.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.util.Color;
import java.util.HashMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BuildZonesHandler {
   private static final HashMap<String, AABB> ZONES = Maps.newHashMap();
   public static AABB ZONE_1_1 = register("zone1_1", 0.0, 102.0, -456.0, -54.0, 202.0, -516.0);
   public static AABB ZONE_1_2 = register("zone1_2", -14.0, 102.0, -362.0, -82.0, 202.0, -431.0);
   public static AABB ZONE_1_3 = register("zone1_3", -14.0, 102.0, -432.0, -54.0, 202.0, -455.0);

   @OnlyIn(Dist.CLIENT)
   @SubscribeEvent
   public static void onRenderLevelStage(RenderLevelStageEvent event) {
      if (BuildingClientEvents.getBuildingToPlace() != null || Minecraft.getInstance().options.renderDebug) {
         if (event.getStage() == Stage.AFTER_TRIPWIRE_BLOCKS) {
            Level level = Minecraft.getInstance().level;
            if (level == null) {
               return;
            }

            event.getPoseStack().pushPose();
            float pRed = 0.1F;
            float pGreen = 1.0F;
            float pBlue = 0.1F;
            float pAlpha = 0.4F;
            Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.mulPoseMatrix(event.getPoseStack().last().pose());
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderTexture(0, St.tx("entity/walter_white"));
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);

            for (AABB box : ZONES.values()) {
               BlockPos.betweenClosedStream(box.setMaxY(box.minY + 5.0))
                  .filter(bp -> !level.getBlockState(bp).canBeReplaced() && level.getBlockState(bp.above()).canBeReplaced())
                  .forEach(bp -> renderBox(bufferBuilder, new AABB(bp).move(cam.getPosition().scale(-1.0)), new Color(pRed, pGreen, pBlue, pAlpha)));
            }

            Tesselator.getInstance().end();
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
            event.getPoseStack().popPose();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static void renderBox(BufferBuilder builder, AABB box, Color color) {
      double minX = box.minX;
      double minY = box.minY;
      double minZ = box.minZ;
      double maxX = box.maxX;
      double maxY = box.maxY + 0.05;
      double maxZ = box.maxZ;
      builder.vertex(maxX, maxY, minZ).uv(1.0F, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv2(0, 1).endVertex();
      builder.vertex(maxX, maxY, maxZ).uv(1.0F, 1.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv2(1, 1).endVertex();
      builder.vertex(minX, maxY, maxZ).uv(0.0F, 1.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv2(1, 0).endVertex();
      builder.vertex(minX, maxY, minZ).uv(0.0F, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv2(0, 0).endVertex();
   }

   public static boolean isBuildingAllowed(AABB building) {
      return BlockPos.betweenClosedStream(building).noneMatch(bp -> {
         boolean contained = false;

         for (AABB box : ZONES.values()) {
            if (box.contains(bp.getCenter())) {
               contained = true;
               break;
            }
         }

         return !contained;
      });
   }

   private static AABB register(String id, double pX1, double pY1, double pZ1, double pX2, double pY2, double pZ2) {
      AABB aabb = new AABB(pX1, pY1, pZ1, pX2, pY2, pZ2);
      ZONES.put(id, aabb);
      return aabb;
   }
}

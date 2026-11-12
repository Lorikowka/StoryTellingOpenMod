package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.content.entities.RiddleEntity;
import com.storyteam.storytelling.system.emotions.BubbleType;
import com.storyteam.storytelling.system.riddles.data.RiddlesDef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RiddleEntityRenderer extends EntityRenderer<RiddleEntity> {
   private static final ResourceLocation QUESTION_TEXTURE = St.tx("entity/riddle");

   public RiddleEntityRenderer(Context context) {
      super(context);
   }

   public void render(RiddleEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      RiddlesDef def = entity.getRiddle();
      if (def != null && def.isVisible()) {
         Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
         float questionScale = entity.getQuestionScale();
         poseStack.pushPose();
         poseStack.translate(0.0F, 3.0F, 0.0F);
         poseStack.mulPose(camera.rotation());
         if (questionScale > 0.0F) {
            this.renderTexturedQuad(poseStack, QUESTION_TEXTURE, Vec3.ZERO, questionScale, questionScale, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1);
         }

         if (entity.shouldShowText() && questionScale <= 0.0F) {
            this.renderTextBubble(entity, def.getText(), poseStack, buffer, packedLight);
         }

         poseStack.popPose();
         super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
      }
   }

   private void renderTextBubble(RiddleEntity entity, String text, PoseStack poseStack, MultiBufferSource buffer, int light) {
      float scale = entity.getBubbleScale();
      if (!(scale <= 0.0F)) {
         Font font = Minecraft.getInstance().font;
         String[] lines = text.split("\n");
         float textScale = 0.02F * scale;
         float lineHeight = 9.0F * textScale;
         float bubbleHeight = lineHeight * (float)lines.length + 0.5F;
         float maxLineWidth = 0.0F;

         for (String line : lines) {
            maxLineWidth = Math.max(maxLineWidth, (float)font.width(line) * 0.02F * 1.0F);
         }

         float segmentWidth = bubbleHeight / 1.5F;
         int midSegments = Math.max(1, (int)Math.ceil((double)(maxLineWidth / segmentWidth)));
         float totalWidth = (float)(midSegments + 2) * segmentWidth;
         float leftX = totalWidth / 2.0F - segmentWidth / 2.0F;
         float rightX = -totalWidth / 2.0F + segmentWidth / 2.0F;
         float midStartX = rightX + segmentWidth;
         poseStack.pushPose();
         poseStack.scale(scale, scale, scale);
         this.renderTexturedQuad(
            poseStack, BubbleType.THINK.LEFT, new Vec3((double)leftX, 0.0, 0.0), segmentWidth, bubbleHeight, buffer, light, OverlayTexture.NO_OVERLAY, 1
         );

         for (int i = 0; i < midSegments; i++) {
            float midX = midStartX + (float)i * segmentWidth;
            this.renderTexturedQuad(
               poseStack, BubbleType.THINK.MID, new Vec3((double)midX, 0.0, 0.0), segmentWidth, bubbleHeight, buffer, light, OverlayTexture.NO_OVERLAY, 1
            );
         }

         this.renderTexturedQuad(
            poseStack, BubbleType.THINK.RIGHT, new Vec3((double)rightX, 0.0, 0.0), segmentWidth, bubbleHeight, buffer, light, OverlayTexture.NO_OVERLAY, 1
         );
         poseStack.popPose();
         if (!(scale < 0.6F)) {
            float totalHeight = (float)lines.length * lineHeight;
            float startY = (totalHeight - lineHeight) / 2.0F + 0.075F;

            for (int i = 0; i < lines.length; i++) {
               String line = lines[i];
               poseStack.pushPose();
               float yOffset = startY - (float)i * lineHeight;
               poseStack.translate(0.0F, yOffset, 0.0F);
               poseStack.scale(-textScale, -textScale, textScale);
               float textX = (float)(-font.width(line)) / 2.0F;
               Component renderLine;
               if (entity.canReadText()) {
                  renderLine = Component.literal(line);
               } else {
                  renderLine = Component.literal(line).withStyle(ChatFormatting.OBFUSCATED);
               }

               font.drawInBatch(renderLine, textX, 0.0F, 16777215, false, poseStack.last().pose(), buffer, DisplayMode.NORMAL, 0, 15728880);
               poseStack.popPose();
            }
         }
      }
   }

   private void renderTexturedQuad(
      PoseStack poseStack, ResourceLocation texture, Vec3 offset, float width, float height, MultiBufferSource buffer, int light, int overlay, int layer
   ) {
      VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
      Matrix4f matrix = poseStack.last().pose();
      Matrix3f normalMatrix = poseStack.last().normal();
      float x = (float)offset.x;
      float y = (float)offset.y;
      float z = (float)(offset.z + (double)((float)layer / 100.0F));
      vertexConsumer.vertex(matrix, -width / 2.0F + x, -height / 2.0F + y, 0.0F + z)
         .color(255, 255, 255, 255)
         .uv(1.0F, 1.0F)
         .overlayCoords(overlay)
         .uv2(light)
         .normal(normalMatrix, 0.0F, 0.0F, 1.0F)
         .endVertex();
      vertexConsumer.vertex(matrix, -width / 2.0F + x, height / 2.0F + y, 0.0F + z)
         .color(255, 255, 255, 255)
         .uv(1.0F, 0.0F)
         .overlayCoords(overlay)
         .uv2(light)
         .normal(normalMatrix, 0.0F, 0.0F, 1.0F)
         .endVertex();
      vertexConsumer.vertex(matrix, width / 2.0F + x, height / 2.0F + y, 0.0F + z)
         .color(255, 255, 255, 255)
         .uv(0.0F, 0.0F)
         .overlayCoords(overlay)
         .uv2(light)
         .normal(normalMatrix, 0.0F, 0.0F, 1.0F)
         .endVertex();
      vertexConsumer.vertex(matrix, width / 2.0F + x, -height / 2.0F + y, 0.0F + z)
         .color(255, 255, 255, 255)
         .uv(0.0F, 1.0F)
         .overlayCoords(overlay)
         .uv2(light)
         .normal(normalMatrix, 0.0F, 0.0F, 1.0F)
         .endVertex();
   }

   public ResourceLocation getTextureLocation(RiddleEntity entity) {
      return QUESTION_TEXTURE;
   }

   public boolean shouldRender(RiddleEntity entity, Frustum frustum, double x, double y, double z) {
      RiddlesDef def = entity.getRiddle();
      return def != null && def.isVisible() ? super.shouldRender(entity, frustum, x, y, z) : false;
   }
}

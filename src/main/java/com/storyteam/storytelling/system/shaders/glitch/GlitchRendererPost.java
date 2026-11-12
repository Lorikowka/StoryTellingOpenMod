package com.storyteam.storytelling.system.shaders.glitch;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.storyteam.storytelling.Storytelling;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class GlitchRendererPost {
   private ShaderInstance shader;
   private float iTime = 0.0F;
   private float timer = 0.0F;
   private boolean glitchFlag = false;
   private static final Minecraft mc = Minecraft.getInstance();

   public GlitchRendererPost() {
      try {
         this.shader = new ShaderInstance(mc.getResourceManager(), new ResourceLocation("storytelling", "glitch_post"), DefaultVertexFormat.POSITION_COLOR_TEX);
      } catch (IOException var2) {
         Storytelling.LOG.error("Failed to load shader", var2);
      }
   }

   public void render(
      PoseStack poseStack,
      float x,
      float y,
      float width,
      float height,
      float glitchAmplitude,
      float glitchNarrowness,
      float glitchBlockiness,
      float glitchMinimizer,
      float delay,
      float glitchTime,
      float partialTicks
   ) {
      if (this.shader != null) {
         this.timer += partialTicks;
         float swithDelay = !this.glitchFlag ? glitchTime : delay;
         if ((float)((int)this.timer) % swithDelay == 0.0F) {
            this.glitchFlag = !this.glitchFlag;
         }

         this.iTime = this.iTime + Minecraft.getInstance().getDeltaFrameTime() / 20.0F;
         int scaledWidth = mc.getWindow().getGuiScaledWidth();
         int scaledHeight = mc.getWindow().getGuiScaledHeight();
         if (!this.glitchFlag) {
            this.shader.safeGetUniform("iTime").set(this.iTime);
            this.shader.safeGetUniform("iResolution").set((float)scaledWidth, (float)scaledHeight);
            this.shader.safeGetUniform("glitchAmplitude").set(glitchAmplitude);
            this.shader.safeGetUniform("glitchNarrowness").set(glitchNarrowness);
            this.shader.safeGetUniform("glitchBlockiness").set(glitchBlockiness);
            this.shader.safeGetUniform("glitchMinimizer").set(glitchMinimizer);
            this.shader.safeGetUniform("region").set(x, y, width, height);
            RenderSystem.setShader(() -> this.shader);
            RenderSystem.setShaderTexture(0, mc.getMainRenderTarget().getColorTextureId());
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Matrix4f matrix = poseStack.last().pose();
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            buffer.vertex(matrix, 0.0F, (float)scaledHeight, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 1.0F).endVertex();
            buffer.vertex(matrix, (float)scaledWidth, (float)scaledHeight, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 1.0F).endVertex();
            buffer.vertex(matrix, (float)scaledWidth, 0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0.0F).endVertex();
            buffer.vertex(matrix, 0.0F, 0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 0.0F).endVertex();
            BufferUploader.drawWithShader(buffer.end());
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            this.shader.clear();
         }
      }
   }
}

package com.storyteam.storytelling.system.shaders.glitch;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.DrawUtils;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class GlitchRendererFragment {
   private ShaderInstance shader;
   private float iTime = 0.0F;
   private float delayTimer = 0.0F;
   private boolean glitchFlag = false;
   private static final Minecraft mc = Minecraft.getInstance();

   public GlitchRendererFragment() {
      try {
         this.shader = new ShaderInstance(mc.getResourceManager(), new ResourceLocation("storytelling", "glitch"), DefaultVertexFormat.POSITION_COLOR_TEX);
      } catch (IOException var2) {
         Storytelling.LOG.error("Failed to load shader", var2);
      }
   }

   public void render(
      PoseStack poseStack,
      ResourceLocation texture,
      float x,
      float y,
      float width,
      float height,
      float glitchAmplitude,
      float glitchNarrowness,
      float glitchBlockiness,
      float glitchMinimizer,
      float delay,
      float partialTicks
   ) {
      if (this.shader != null) {
         this.delayTimer += partialTicks;
         if ((float)((int)this.delayTimer) % delay == 0.0F) {
            this.glitchFlag = !this.glitchFlag;
         }

         this.iTime = this.iTime + Minecraft.getInstance().getDeltaFrameTime() / 20.0F;
         this.shader.safeGetUniform("iTime").set(this.iTime);
         this.shader.safeGetUniform("iResolution").set((float)mc.getWindow().getGuiScaledWidth(), (float)mc.getWindow().getGuiScaledHeight());
         this.shader.safeGetUniform("glitchAmplitude").set(glitchAmplitude);
         this.shader.safeGetUniform("glitchNarrowness").set(glitchNarrowness);
         this.shader.safeGetUniform("glitchBlockiness").set(glitchBlockiness);
         this.shader.safeGetUniform("glitchMinimizer").set(this.glitchFlag ? 100.0F : glitchMinimizer);
         RenderSystem.setShader(() -> this.shader);
         RenderSystem.setShaderTexture(0, texture);
         RenderSystem.disableDepthTest();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         DrawUtils.drawQuad(poseStack, x, y, width, height);
         RenderSystem.disableBlend();
         RenderSystem.enableDepthTest();
         this.shader.clear();
      }
   }
}

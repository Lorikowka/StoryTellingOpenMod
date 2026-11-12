package com.storyteam.storytelling.client.particle.st_particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class ParticleRenderTypes {
   public static boolean disableDepthTest = false;
   public static final ParticleRenderType RENDERER = new ParticleRenderType() {
      public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
         if (ParticleRenderTypes.disableDepthTest) {
            RenderSystem.disableDepthTest();
         }

         RenderSystem.setShader(GameRenderer::getParticleShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
         RenderSystem.depthMask(false);
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(770, 1);
         bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);
      }

      public void end(Tesselator tessellator) {
         tessellator.end();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
         RenderSystem.depthMask(true);
      }

      @Override
      public String toString() {
         return "storytelling:quad_gradient_tint";
      }
   };
   public static final ParticleRenderType RENDERER_BLUR = new ParticleRenderType() {
      public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
         if (ParticleRenderTypes.disableDepthTest) {
            RenderSystem.disableDepthTest();
         }

         RenderSystem.setShader(GameRenderer::getParticleShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
         RenderSystem.depthMask(false);
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(770, 1);
         textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).setBlurMipmap(true, false);
         bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);
      }

      public void end(Tesselator tessellator) {
         tessellator.end();
         RenderSystem.enableDepthTest();
         Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
         RenderSystem.disableBlend();
         RenderSystem.depthMask(true);
      }

      @Override
      public String toString() {
         return "storytelling:quad_gradient_tint";
      }
   };
   public static final ParticleRenderType RENDERER_STANDARD_BLEND = new ParticleRenderType() {
      public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
         RenderSystem.setShader(GameRenderer::getParticleShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
         RenderSystem.depthMask(false);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);
      }

      public void end(Tesselator tessellator) {
         tessellator.end();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
         RenderSystem.depthMask(true);
      }

      @Override
      public String toString() {
         return "storytelling:quad_tint";
      }
   };
   public static final ParticleRenderType RENDERER_STANDARD_BLEND_BLUR = new ParticleRenderType() {
      public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
         RenderSystem.setShader(GameRenderer::getParticleShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
         RenderSystem.depthMask(false);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).setBlurMipmap(true, false);
         bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);
      }

      public void end(Tesselator tessellator) {
         tessellator.end();
         RenderSystem.enableDepthTest();
         Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
         RenderSystem.disableBlend();
         RenderSystem.depthMask(true);
      }

      @Override
      public String toString() {
         return "storytelling:quad_tint";
      }
   };
   public static final ParticleRenderType HUY = new ParticleRenderType() {
      public void begin(BufferBuilder buffer, TextureManager textureManager) {
         RenderSystem.disableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.depthMask(false);
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         textureManager.bindForSetup(TextureAtlas.LOCATION_PARTICLES);
         buffer.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);
      }

      public void end(Tesselator tesselator) {
         tesselator.end();
         RenderSystem.depthMask(true);
         RenderSystem.disableBlend();
      }
   };
}

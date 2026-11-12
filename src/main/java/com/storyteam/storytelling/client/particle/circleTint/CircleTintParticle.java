package com.storyteam.storytelling.client.particle.circleTint;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.storyteam.storytelling.client.particle.StoryTexturedSheetParticle;
import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.mixins._client.minecraft.ParticleEngineAccessor;
import com.storyteam.storytelling.mixins._client.minecraft.TextureAtlasAccessor;
import com.storyteam.storytelling.util.Color;
import javax.annotation.Nonnull;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CircleTintParticle extends StoryTexturedSheetParticle {
   protected final boolean standardBlending;
   float resizeSpeed;
   float diameter;
   int fadeInTime;
   protected float originalSize;
   public static boolean disableDepthTest = false;
   private static final ParticleRenderType RENDERER = new ParticleRenderType() {
      public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
         if (CircleTintParticle.disableDepthTest) {
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
         return "storytelling:circle_tint";
      }
   };
   private static final ParticleRenderType RENDERER_STANDARD_BLEND = new ParticleRenderType() {
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
         return "storytelling:circle_tint";
      }
   };

   public CircleTintParticle(
      ControllerData data,
      ClientLevel world,
      double x,
      double y,
      double z,
      double velocityX,
      double velocityY,
      double velocityZ,
      Color spark,
      float diameter,
      int fadeInTime,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending
   ) {
      super(data, world, x, y, z, velocityX, velocityY, velocityZ);
      this.resizeSpeed = resizeSpeed;
      this.setColor((float)spark.getRed() / 255.0F, (float)spark.getGreen() / 255.0F, (float)spark.getBlue() / 255.0F);
      this.setSize(diameter, diameter);
      this.fadeInTime = fadeInTime;
      this.lifetime = lifeTime + fadeInTime;
      this.diameter = diameter;
      this.quadSize = 0.0F;
      this.originalSize = diameter;
      this.alpha = 1.0F;
      this.xd = velocityX;
      this.yd = velocityY;
      this.zd = velocityZ;
      this.standardBlending = standardBlending;
      this.hasPhysics = shouldCollide;
   }

   public float getQuadSize(float scaleFactor) {
      return this.quadSize;
   }

   protected int getLightColor(float partialTick) {
      return LightTexture.pack(15, 15);
   }

   @Nonnull
   public ParticleRenderType getRenderType() {
      return this.standardBlending ? RENDERER_STANDARD_BLEND : RENDERER;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.age < this.fadeInTime) {
         this.quadSize = this.diameter * (float)this.age / (float)(this.fadeInTime + 1);
      } else if (this.resizeSpeed != -1.0F) {
         this.quadSize = this.diameter;
         this.diameter = this.diameter * this.resizeSpeed;
      } else {
         this.quadSize = this.diameter;
         this.diameter = this.originalSize * (1.0F - (float)(this.age - this.fadeInTime) / (float)(this.lifetime - this.fadeInTime));
      }
   }

   public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
      Vec3 $$3 = pRenderInfo.getPosition();
      float $$4 = (float)(Mth.lerp((double)pPartialTicks, this.xo, this.x) - $$3.x());
      float $$5 = (float)(Mth.lerp((double)pPartialTicks, this.yo, this.y) - $$3.y());
      float $$6 = (float)(Mth.lerp((double)pPartialTicks, this.zo, this.z) - $$3.z());
      Quaternionf $$8;
      if (this.roll == 0.0F) {
         $$8 = pRenderInfo.rotation();
      } else {
         $$8 = new Quaternionf(pRenderInfo.rotation());
         $$8.rotateZ(Mth.lerp(pPartialTicks, this.oRoll, this.roll));
      }

      Vector3f[] $$9 = new Vector3f[]{
         new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
      };
      float $$10 = this.getQuadSize(pPartialTicks);

      for (int $$11 = 0; $$11 < 4; $$11++) {
         Vector3f $$12 = $$9[$$11];
         $$12.rotate($$8);
         $$12.mul($$10);
         $$12.add($$4, $$5, $$6);
      }

      float $$13 = this.getU0();
      float $$14 = this.getU1();
      float $$15 = this.getV0();
      float $$16 = this.getV1();
      int $$17 = this.getLightColor(pPartialTicks);
      pBuffer.vertex((double)$$9[0].x(), (double)$$9[0].y(), (double)$$9[0].z())
         .uv($$14, $$16)
         .color(this.rCol, this.gCol, this.bCol, this.alpha)
         .uv2($$17)
         .endVertex();
      pBuffer.vertex((double)$$9[1].x(), (double)$$9[1].y(), (double)$$9[1].z())
         .uv($$14, $$15)
         .color(this.rCol, this.gCol, this.bCol, this.alpha)
         .uv2($$17)
         .endVertex();
      pBuffer.vertex((double)$$9[2].x(), (double)$$9[2].y(), (double)$$9[2].z())
         .uv($$13, $$15)
         .color(this.rCol, this.gCol, this.bCol, this.alpha)
         .uv2($$17)
         .endVertex();
      pBuffer.vertex((double)$$9[3].x(), (double)$$9[3].y(), (double)$$9[3].z())
         .uv($$13, $$16)
         .color(this.rCol, this.gCol, this.bCol, this.alpha)
         .uv2($$17)
         .endVertex();
   }

   protected float getU0() {
      float v = super.getU0();
      ParticleEngine engine = Minecraft.getInstance().particleEngine;
      TextureAtlas atlas = ((ParticleEngineAccessor)engine).storytelling$getTextureAtlas();
      int w = ((TextureAtlasAccessor)atlas).st$getWidth();
      return v + 1.0F / (float)w;
   }

   protected float getU1() {
      float v = super.getU1();
      ParticleEngine engine = Minecraft.getInstance().particleEngine;
      TextureAtlas atlas = ((ParticleEngineAccessor)engine).storytelling$getTextureAtlas();
      int w = ((TextureAtlasAccessor)atlas).st$getWidth();
      return v - 1.0F / (float)w;
   }

   protected float getV0() {
      float v = super.getV0();
      ParticleEngine engine = Minecraft.getInstance().particleEngine;
      TextureAtlas atlas = ((ParticleEngineAccessor)engine).storytelling$getTextureAtlas();
      int h = ((TextureAtlasAccessor)atlas).st$getHeight();
      return v + 1.0F / (float)h;
   }

   protected float getV1() {
      float v = super.getV1();
      ParticleEngine engine = Minecraft.getInstance().particleEngine;
      TextureAtlas atlas = ((ParticleEngineAccessor)engine).storytelling$getTextureAtlas();
      int h = ((TextureAtlasAccessor)atlas).st$getHeight();
      return v - 1.0F / (float)h;
   }
}

package com.storyteam.storytelling.client.particle.st_particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.client.particle.StoryTexturedSheetParticle;
import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.client.particle.st_particle.options.STAlphaInOutOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STDefaultOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STGradientOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STScalingInOutOptions;
import com.storyteam.storytelling.mixins._client.minecraft.ParticleEngineAccessor;
import com.storyteam.storytelling.mixins._client.minecraft.TextureAtlasAccessor;
import com.storyteam.storytelling.util.Color;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class STParticle extends StoryTexturedSheetParticle {
   protected float initSize;
   protected float initAlpha;
   protected boolean standardBlending;
   protected boolean glowing;
   protected boolean blur;
   protected STDefaultOptions defaultOptions;
   protected STScalingInOutOptions scalingOptions;
   protected STAlphaInOutOptions alphaOptions;
   protected STGradientOptions gradientInOutOptions;

   public STParticle(
      ControllerData data, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, STParticleData stParticleData
   ) {
      super(data, world, x, y, z, velocityX, velocityY, velocityZ);
      this.defaultOptions = stParticleData.defaultOptions;
      this.xd = velocityX;
      this.yd = velocityY;
      this.zd = velocityZ;
      this.rCol = this.defaultOptions.r;
      this.gCol = this.defaultOptions.g;
      this.bCol = this.defaultOptions.b;
      this.alpha = this.defaultOptions.alpha;
      this.initAlpha = this.alpha;
      this.quadSize = this.defaultOptions.size;
      this.hasPhysics = this.defaultOptions.hasPhysics;
      this.lifetime = this.defaultOptions.lifetime;
      this.initSize = this.quadSize;
      this.friction = this.defaultOptions.friction;
      this.standardBlending = this.defaultOptions.standardBlending;
      this.glowing = this.defaultOptions.glowing;
      this.blur = this.defaultOptions.blur;
      this.scalingOptions = stParticleData.scalingOptions;
      this.alphaOptions = stParticleData.alphaOptions;
      this.gradientInOutOptions = stParticleData.gradientInOutOptions;
      if (this.scalingOptions.in > 0) {
         this.quadSize = 0.0F;
      }

      if (this.alphaOptions.in > 0) {
         this.alpha = 0.0F;
      }

      this.setSize(this.quadSize, this.quadSize);
   }

   public Color getColor() {
      return new Color(this.rCol, this.gCol, this.bCol);
   }

   public Vec3 getMoveVec() {
      return new Vec3(this.xd, this.yd, this.zd);
   }

   public void setMoveVec(Vec3 vec) {
      this.xd = vec.x();
      this.yd = vec.y();
      this.zd = vec.z();
   }

   protected int getLightColor(float partialTick) {
      return this.glowing ? 15728880 : super.getLightColor(partialTick);
   }

   @Override
   public void tick() {
      super.tick();
      this.applyGradientOptions();
      this.applyAlphaOptions();
      this.applyScaleOptions();
   }

   public void applyAlphaOptions() {
      int alphaIn = this.alphaOptions.in;
      int alphaOut = this.alphaOptions.out;
      float p = 1.0F;
      if (this.age < alphaIn) {
         float t = (float)this.age / (float)alphaIn;
         p = 0.5F * (1.0F - (float)Math.cos(Math.PI * (double)t));
      } else if (this.age > this.lifetime - alphaOut) {
         int remTime = this.lifetime - this.age;
         float t = (float)remTime / (float)alphaOut;
         p = 0.5F * (1.0F - (float)Math.cos(Math.PI * (double)t));
      }

      this.alpha = this.initAlpha * p;
   }

   public void applyScaleOptions() {
      int scaleIn = this.scalingOptions.in;
      int scaleOut = this.scalingOptions.out;
      float p = 1.0F;
      if (this.age < scaleIn) {
         float t = (float)this.age / (float)scaleIn;
         p = 0.5F * (1.0F - (float)Math.cos(Math.PI * (double)t));
      } else if (this.age > this.lifetime - scaleOut) {
         int remTime = this.lifetime - this.age;
         float t = (float)remTime / (float)scaleOut;
         p = 0.5F * (1.0F - (float)Math.cos(Math.PI * (double)t));
      }

      this.quadSize = this.initSize * p;
      this.setSize(this.quadSize, this.quadSize);
   }

   public void applyGradientOptions() {
      if (!this.gradientInOutOptions.gradients.isEmpty()) {
         float lifetimeFraction = (float)this.age / (float)this.lifetime;
         float startR = this.defaultOptions.r;
         float startG = this.defaultOptions.g;
         float startB = this.defaultOptions.b;
         STGradientOptions.GradientPoint prev = new STGradientOptions.GradientPoint(0, startR, startG, startB);
         STGradientOptions.GradientPoint next = prev;

         for (int i = 0; i < this.gradientInOutOptions.gradients.size(); i++) {
            STGradientOptions.GradientPoint point = this.gradientInOutOptions.gradients.get(i);
            float pointFraction = (float)point.time / (float)this.lifetime;
            if (lifetimeFraction < pointFraction) {
               next = point;
               break;
            }

            prev = point;
         }

         float prevFraction = (float)prev.time / (float)this.lifetime;
         float nextFraction = (float)next.time / (float)this.lifetime;
         float t = (lifetimeFraction - prevFraction) / (nextFraction - prevFraction);
         t = Mth.clamp(t, 0.0F, 1.0F);
         float red = Mth.lerp(t, prev.r, next.r);
         float green = Mth.lerp(t, prev.g, next.g);
         float blue = Mth.lerp(t, prev.b, next.b);
         this.setColor(red, green, blue);
      }
   }

   public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
      Vec3 $$3 = pRenderInfo.getPosition();
      float $$4 = (float)(Mth.lerp((double)pPartialTicks, this.xo, this.x) - $$3.x());
      float $$5 = (float)(Mth.lerp((double)pPartialTicks, this.yo, this.y) - $$3.y() + (double)this.bbHeight / 2.0);
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

   public ParticleRenderType getRenderType() {
      if (this.standardBlending && this.blur) {
         return ParticleRenderTypes.RENDERER_STANDARD_BLEND_BLUR;
      } else if (this.standardBlending) {
         return ParticleRenderTypes.RENDERER_STANDARD_BLEND;
      } else {
         return this.blur ? ParticleRenderTypes.RENDERER_BLUR : ParticleRenderTypes.RENDERER;
      }
   }
}

package com.storyteam.storytelling.client.particle.circleTint;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.MathUtils;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Vector3f;

public class CircleGradientParticle extends CircleTintParticle {
   protected float rStart;
   protected float gStart;
   protected float bStart;
   protected float rEnd;
   protected float gEnd;
   protected float bEnd;
   protected boolean isGlowing;
   protected int timeStartChangeColor;
   private float randomDirectionX;
   private float randomDirectionZ;
   private float randomDeviationX;
   private float randomDeviationZ;

   public CircleGradientParticle(
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
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      float dr,
      float dg,
      float db,
      boolean isGlowing,
      int timeStartChangeColor,
      float alpha
   ) {
      super(data, world, x, y, z, velocityX, velocityY, velocityZ, spark, diameter, 0, lifeTime, resizeSpeed, shouldCollide, standardBlending);
      this.bEnd = db;
      this.rEnd = dr;
      this.gEnd = dg;
      this.rStart = (float)spark.getRed() / 255.0F;
      this.gStart = (float)spark.getGreen() / 255.0F;
      this.bStart = (float)spark.getBlue() / 255.0F;
      this.isGlowing = isGlowing;
      Random rnd = new Random();
      this.randomDirectionX = MathUtils.randomFloat(rnd);
      this.randomDirectionZ = MathUtils.randomFloat(rnd);
      this.randomDeviationX = this.random.nextBoolean() ? -1.0F : 1.0F;
      this.randomDeviationZ = this.random.nextBoolean() ? -1.0F : 1.0F;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = alpha;
   }

   @Override
   protected int getLightColor(float partialTick) {
      return this.isGlowing ? 240 : super.getLightColor(partialTick);
   }

   @Override
   public void tick() {
      super.tick();
      float progress = (float)this.age / (float)this.lifetime;
      if (this.age >= this.timeStartChangeColor) {
         float red = this.rStart * (1.0F - progress) + this.rEnd * progress;
         float green = this.gStart * (1.0F - progress) + this.gEnd * progress;
         float blue = this.bStart * (1.0F - progress) + this.bEnd * progress;
         this.setColor(red, green, blue);
      }
   }

   @Override
   public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
      super.render(pBuffer, pRenderInfo, pPartialTicks);
   }

   private void lerpColors(float pPartialTick) {
      float f = ((float)this.age + pPartialTick) / ((float)this.lifetime + 1.0F);
      Vector3f vector3f = new Vector3f(this.rCol, this.gCol, this.bCol).lerp(new Vector3f(this.rEnd, this.gEnd, this.bEnd), f);
      this.rCol = vector3f.x();
      this.gCol = vector3f.y();
      this.bCol = vector3f.z();
   }
}

package com.storyteam.storytelling.client.particle.circleTint;

import com.mojang.serialization.Codec;
import com.storyteam.storytelling.client.particle.StoryParticleProvider;
import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.util.Color;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;

public class CircleTintParticleWithTrail extends CircleTintParticle {
   protected float trailLifetimeModifier;

   public CircleTintParticleWithTrail(
      ControllerData data,
      float trailLifetimeModifier,
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
      SpriteSet sprites
   ) {
      super(data, world, x, y, z, velocityX, velocityY, velocityZ, spark, diameter, 0, lifeTime, resizeSpeed, shouldCollide, standardBlending);
      this.trailLifetimeModifier = trailLifetimeModifier;
      this.friction = 1.0F;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.age > 0 && this.level != null) {
         Vec3 opos = new Vec3(this.xo, this.yo, this.zo);
         Vec3 pos = new Vec3(this.x, this.y, this.z);
         Vec3 b = pos.subtract(opos);

         for (float i = 0.0F; (double)i < b.length(); i += 0.1F) {
            Vec3 ppos = pos.add(b.normalize().reverse().multiply((double)i, (double)i, (double)i));
            this.level
               .addParticle(
                  new CircleTintData(
                     new Color(this.rCol, this.gCol, this.bCol),
                     this.quadSize * 0.9F,
                     (int)((float)this.lifetime * this.trailLifetimeModifier),
                     this.resizeSpeed,
                     this.hasPhysics,
                     this.standardBlending,
                     this.data
                  ),
                  true,
                  ppos.x,
                  ppos.y,
                  ppos.z,
                  0.0,
                  0.0,
                  0.0
               );
         }
      }
   }

   public static class CircleTintFactory implements StoryParticleProvider<CircleTintWithTrailData> {
      private final SpriteSet sprites;

      @Nullable
      public Particle createParticle(
         CircleTintWithTrailData circleTintData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity
      ) {
         CircleTintParticleWithTrail particle = new CircleTintParticleWithTrail(
            circleTintData.getController(),
            circleTintData.getTrailLifetimeModifier(),
            world,
            xPos,
            yPos,
            zPos,
            xVelocity,
            yVelocity,
            zVelocity,
            circleTintData.getTint(),
            circleTintData.getDiameter(),
            circleTintData.getLifeTime(),
            circleTintData.getResizeSpeed(),
            circleTintData.shouldCollide(),
            circleTintData.standardBlending(),
            this.sprites
         );
         particle.pickSprite(this.sprites);
         return particle;
      }

      public CircleTintFactory(SpriteSet sprite) {
         this.sprites = sprite;
      }

      public static class CircleTintType extends ParticleType<CircleTintWithTrailData> {
         public CircleTintType() {
            super(false, CircleTintWithTrailData.DESERIALIZER);
         }

         public Codec<CircleTintWithTrailData> codec() {
            return CircleTintWithTrailData.CODEC;
         }
      }
   }
}

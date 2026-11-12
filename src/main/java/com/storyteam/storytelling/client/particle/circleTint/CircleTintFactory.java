package com.storyteam.storytelling.client.particle.circleTint;

import com.mojang.serialization.Codec;
import com.storyteam.storytelling.client.particle.StoryParticleProvider;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;

public class CircleTintFactory implements StoryParticleProvider<CircleTintData> {
   private final SpriteSet sprites;

   @Nullable
   public Particle createParticle(
      CircleTintData circleTintData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity
   ) {
      CircleTintParticle particle = new CircleTintParticle(
         circleTintData.getController(),
         world,
         xPos,
         yPos,
         zPos,
         xVelocity,
         yVelocity,
         zVelocity,
         circleTintData.getTint(),
         circleTintData.getDiameter(),
         circleTintData.getFadeInTime(),
         circleTintData.getLifeTime(),
         circleTintData.getResizeSpeed(),
         circleTintData.shouldCollide(),
         circleTintData.standardBlending()
      );
      particle.pickSprite(this.sprites);
      return particle;
   }

   public CircleTintFactory(SpriteSet sprite) {
      this.sprites = sprite;
   }

   public static class CircleTintType extends ParticleType<CircleTintData> {
      public CircleTintType() {
         super(false, CircleTintData.DESERIALIZER);
      }

      public Codec<CircleTintData> codec() {
         return CircleTintData.CODEC;
      }
   }
}

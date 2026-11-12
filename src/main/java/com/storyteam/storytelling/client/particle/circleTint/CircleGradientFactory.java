package com.storyteam.storytelling.client.particle.circleTint;

import com.mojang.serialization.Codec;
import com.storyteam.storytelling.client.particle.StoryParticleProvider;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;

public class CircleGradientFactory implements StoryParticleProvider<CircleGradientData> {
   private final SpriteSet sprites;

   @Nullable
   public Particle createParticle(
      CircleGradientData circleTintData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity
   ) {
      float dr = (float)circleTintData.getRepaint().getRed();
      float dg = (float)circleTintData.getRepaint().getGreen();
      float db = (float)circleTintData.getRepaint().getBlue();
      CircleGradientParticle particle = new CircleGradientParticle(
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
         circleTintData.getLifeTime(),
         circleTintData.getResizeSpeed(),
         circleTintData.shouldCollide(),
         circleTintData.standardBlending(),
         dr / 255.0F,
         dg / 255.0F,
         db / 255.0F,
         circleTintData.glowing(),
         circleTintData.timeStartChangeColor(),
         circleTintData.alpha()
      );
      particle.pickSprite(this.sprites);
      return particle;
   }

   public CircleGradientFactory(SpriteSet sprite) {
      this.sprites = sprite;
   }

   public static class CircleGradientType extends ParticleType<CircleGradientData> {
      public CircleGradientType() {
         super(false, CircleGradientData.DESERIALIZER);
      }

      public Codec<CircleGradientData> codec() {
         return CircleGradientData.CODEC;
      }
   }
}

package com.storyteam.storytelling.client.particle.st_particle;

import com.mojang.serialization.Codec;
import com.storyteam.storytelling.client.particle.StoryParticleProvider;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;

public class STParticleProvider implements StoryParticleProvider<STParticleData> {
   protected final SpriteSet sprites;

   @Nullable
   public Particle createParticle(
      STParticleData stParticleData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity
   ) {
      STParticle particle = new STParticle(stParticleData.getController(), world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, stParticleData);
      particle.pickSprite(this.sprites);
      return particle;
   }

   public STParticleProvider(SpriteSet sprite) {
      this.sprites = sprite;
   }

   public static class STParticleType extends ParticleType<STParticleData> {
      public STParticleType() {
         super(false, STParticleData.DESERIALIZER);
      }

      public Codec<STParticleData> codec() {
         return STParticleData.CODEC;
      }
   }
}

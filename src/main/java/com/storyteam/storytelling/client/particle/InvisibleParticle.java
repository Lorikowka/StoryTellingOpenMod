package com.storyteam.storytelling.client.particle;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class InvisibleParticle extends TextureSheetParticle {
   public InvisibleParticle(ClientLevel level, double p_i232448_2_, double p_i232448_4_, double p_i232448_6_, double x, double y, double z) {
      super(level, p_i232448_2_, p_i232448_4_, p_i232448_6_, x, y, z);
      this.lifetime = 0;
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   protected int getLightColor(float pPartialTick) {
      return super.getLightColor(pPartialTick);
   }

   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSetl;

      public Factory(SpriteSet sprite) {
         this.spriteSetl = sprite;
      }

      @Nullable
      public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xv, double yv, double zv) {
         InvisibleParticle particle = new InvisibleParticle(world, x, y, z, xv, yv, zv);
         particle.pickSprite(this.spriteSetl);
         return particle;
      }
   }
}

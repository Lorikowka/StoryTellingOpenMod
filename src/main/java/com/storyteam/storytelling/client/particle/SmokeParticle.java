package com.storyteam.storytelling.client.particle;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class SmokeParticle extends TextureSheetParticle {
   private SpriteSet spriteSet;

   protected SmokeParticle(SpriteSet spriteSet, ClientLevel pLevel, double x, double y, double z, double xd, double yd, double zd) {
      super(pLevel, x, y, z, xd, yd, zd);
      this.quadSize = 0.5F;
      this.xd = xd;
      this.yd = yd;
      this.zd = zd;
      this.x = x;
      this.y = y;
      this.z = z;
      this.lifetime = 60;
      this.spriteSet = spriteSet;
      this.setSpriteFromAge(spriteSet);
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   public void tick() {
      this.setSpriteFromAge(this.spriteSet);
      super.tick();
   }

   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSetl;

      public Factory(SpriteSet sprite) {
         this.spriteSetl = sprite;
      }

      @Nullable
      public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xv, double yv, double zv) {
         SmokeParticle particle = new SmokeParticle(this.spriteSetl, world, x, y, z, xv, yv, zv);
         particle.pickSprite(this.spriteSetl);
         return particle;
      }
   }
}

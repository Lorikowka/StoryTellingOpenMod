package com.storyteam.storytelling.client.particle;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GlitchParticle extends TextureSheetParticle {
   private SpriteSet spriteSet;

   protected GlitchParticle(SpriteSet spriteSet, ClientLevel pLevel, double x, double y, double z, double xd, double yd, double zd) {
      super(pLevel, x, y, z, xd, yd, zd);
      this.quadSize = 0.4F;
      this.xd = xd;
      this.yd = yd;
      this.zd = zd;
      this.x = x;
      this.y = y;
      this.z = z;
      this.lifetime = 13;
      this.spriteSet = spriteSet;
      this.gravity = 0.0F;
      this.setSpriteFromAge(spriteSet);
   }

   public void tick() {
      this.setSpriteFromAge(this.spriteSet);
      this.alpha = (float)Math.min(this.age, 5) / 5.0F;
      super.tick();
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSetl;

      public Factory(SpriteSet sprite) {
         this.spriteSetl = sprite;
      }

      @Nullable
      public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xv, double yv, double zv) {
         GlitchParticle particle = new GlitchParticle(this.spriteSetl, world, x, y, z, xv, yv, zv);
         particle.setSpriteFromAge(this.spriteSetl);
         return particle;
      }
   }
}

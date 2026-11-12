package com.storyteam.storytelling.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredDropParticle extends TextureSheetParticle {
   public ColoredDropParticle(ClientLevel level, double x, double y, double z) {
      super(level, x, y, z);
      this.setSize(0.01F, 0.01F);
      this.gravity = 0.06F;
      this.lifetime = 40 + level.random.nextInt(20);
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.yd = this.yd - (double)this.gravity;
      this.move(this.xd, this.yd, this.zd);
      if (this.onGround || this.lifetime-- <= 0) {
         this.remove();
      }
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }
}

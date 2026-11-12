package com.storyteam.storytelling.system.particle;

import com.storyteam.storytelling.util.Color;
import java.nio.ByteBuffer;
import net.minecraft.world.phys.Vec3;

public class OptimizedParticle extends BaseParticle<OptimizedParticle> implements ISizedParticle, IColoredParticle {
   public float radius;
   public Color color;
   public OptimizedParticleType type;
   FogSettings fogSettings = FogSettings.NO_FOG;

   public OptimizedParticle(OptimizedParticleType type, Vec3 pos, float radius, Color color, float lifetime, FogSettings fogSettings) {
      this.type = type;
      this.pos = pos;
      this.radius = radius;
      this.color = color;
      this.fogSettings = fogSettings;
      this.maxLifetime = lifetime;
   }

   public OptimizedParticle(OptimizedParticleType type, Vec3 pos, float radius, Color color, float lifetime) {
      this.type = type;
      this.pos = pos;
      this.radius = radius;
      this.color = color;
      this.maxLifetime = lifetime;
   }

   public final void renderToBuffer(ByteBuffer buf) {
      buf.putFloat((float)this.pos.x);
      buf.putFloat((float)this.pos.y);
      buf.putFloat((float)this.pos.z);
      buf.putFloat(this.radius);
      buf.putFloat((float)this.color.getRed() / 255.0F);
      buf.putFloat((float)this.color.getGreen() / 255.0F);
      buf.putFloat((float)this.color.getBlue() / 255.0F);
      buf.putFloat((float)this.color.getAlpha() / 255.0F);
      buf.putFloat(this.fogSettings.NECR0MANTH_FOG_COEFF1);
      buf.putFloat(this.fogSettings.NECR0MANTH_FOG_COEFF2);
      buf.putFloat(this.fogSettings.NECR0MANTH_FOG_EXP);
      buf.putFloat(0.0F);
   }

   @Override
   public Color getColor() {
      return this.color;
   }

   @Override
   public void setColor(Color color) {
      this.color = color;
   }

   @Override
   public float getSize() {
      return this.radius;
   }

   @Override
   public void setSize(float size) {
      this.radius = size;
   }
}

package com.storyteam.storytelling.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WaterDripParticle extends TextureSheetParticle {
   private final Fluid type;
   protected boolean isGlowing;

   WaterDripParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, Fluid pType) {
      super(pLevel, pX, pY, pZ);
      this.setSize(0.01F, 0.01F);
      this.gravity = 0.06F;
      this.type = pType;
      this.xd = pXSpeed;
      this.yd = pYSpeed;
      this.zd = pZSpeed;
   }

   protected Fluid getType() {
      return this.type;
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   public int getLightColor(float pPartialTick) {
      return this.isGlowing ? 240 : super.getLightColor(pPartialTick);
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.preMoveUpdate();
      if (!this.removed) {
         this.yd = this.yd - (double)this.gravity;
         this.move(this.xd, this.yd, this.zd);
         this.postMoveUpdate();
         if (!this.removed) {
            this.xd *= 0.98F;
            this.yd *= 0.98F;
            this.zd *= 0.98F;
            if (this.type != Fluids.EMPTY) {
               BlockPos $$0 = BlockPos.containing(this.x, this.y, this.z);
               FluidState $$1 = this.level.getFluidState($$0);
               if ($$1.getType() == this.type && this.y < (double)((float)$$0.getY() + $$1.getHeight(this.level, $$0))) {
                  this.remove();
               }
            }
         }
      }
   }

   protected void preMoveUpdate() {
      if (this.lifetime-- <= 0) {
         this.remove();
      }
   }

   protected void postMoveUpdate() {
   }

   public static TextureSheetParticle createWaterFallParticle(
      SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed
   ) {
      WaterDripParticle $$8 = new WaterDripParticle.FallAndLandParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, Fluids.WATER, ParticleTypes.SPLASH);
      $$8.setColor(0.2F, 0.3F, 1.0F);
      return $$8;
   }

   @OnlyIn(Dist.CLIENT)
   static class FallAndLandParticle extends WaterDripParticle.FallingParticle {
      protected final ParticleOptions landParticle;

      FallAndLandParticle(
         ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, Fluid pType, ParticleOptions pLandParticle
      ) {
         super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
         this.landParticle = pLandParticle;
      }

      @Override
      protected void postMoveUpdate() {
         if (this.onGround) {
            this.remove();
            this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class FallingParticle extends WaterDripParticle {
      FallingParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, Fluid pType) {
         this(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType, (int)(64.0 / (Math.random() * 0.8 + 0.2)));
      }

      FallingParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, Fluid pType, int pLifetime) {
         super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
         this.lifetime = pLifetime;
      }

      @Override
      protected void postMoveUpdate() {
         if (this.onGround) {
            this.remove();
         }
      }
   }
}

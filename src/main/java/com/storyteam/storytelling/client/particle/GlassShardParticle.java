package com.storyteam.storytelling.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class GlassShardParticle extends TextureSheetParticle {
   private final BlockPos pos;
   private final float uo;
   private final float vo;

   public GlassShardParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos) {
      super(level, x, y, z, xSpeed, ySpeed, zSpeed);
      this.lifetime = 200;
      this.gravity = 0.0F;
      this.setSize(0.1F, 0.1F);
      this.quadSize = this.quadSize * (this.random.nextFloat() * 1.5F + 0.4F);
      this.xd = xSpeed;
      this.yd = ySpeed;
      this.zd = zSpeed;
      this.pos = pos;
      this.uo = this.random.nextFloat() * 3.0F;
      this.vo = this.random.nextFloat() * 3.0F;
   }

   public GlassShardParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, BlockState pState) {
      this(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pState, BlockPos.containing(pX, pY, pZ));
   }

   public Vec3 getMoveVec() {
      return new Vec3(this.xd, this.yd, this.zd);
   }

   public void setMoveVec(Vec3 vec) {
      this.xd = vec.x();
      this.yd = vec.y();
      this.zd = vec.z();
   }

   public float getGravity() {
      return this.gravity;
   }

   public void setGravity(float gravity) {
      this.gravity = gravity;
   }

   public void tick() {
      super.tick();
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.TERRAIN_SHEET;
   }

   protected float getU0() {
      return this.sprite.getU((double)((this.uo + 1.0F) / 4.0F * 16.0F));
   }

   protected float getU1() {
      return this.sprite.getU((double)(this.uo / 4.0F * 16.0F));
   }

   protected float getV0() {
      return this.sprite.getV((double)(this.vo / 4.0F * 16.0F));
   }

   protected float getV1() {
      return this.sprite.getV((double)((this.vo + 1.0F) / 4.0F * 16.0F));
   }

   public float getQuadSize(float pScaleFactor) {
      return this.quadSize;
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<BlockParticleOption> {
      @Nullable
      public Particle createParticle(BlockParticleOption blockParticleOption, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
         BlockState blockstate = blockParticleOption.getState();
         GlassShardParticle particle = new GlassShardParticle(level, x, y, z, dx, dy, dz, blockstate);
         particle.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture(blockstate, level, BlockPos.containing(x, y, z)));
         return particle;
      }
   }
}

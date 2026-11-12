package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.client.particle.st_particle.STParticleData;
import com.storyteam.storytelling.client.particle.st_particle.STParticleDataBuilder;
import com.storyteam.storytelling.client.particle.st_particle.options.STDefaultOptions;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.UtilST;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;
import org.zeith.hammeranims.api.tile.IAnimatedTile;

public class GeyserEidosTile extends TileLinkable implements IAnimatedTile {
   public final AnimationSystem animations = AnimationSystem.create(this);
   private int geyserTimer = 0;
   private static final int GEYSER_DURATION = 60;
   private int levitationTimer = 0;
   private static final int LEVITATION_DURATION = 80;
   private static int[] BASE_COLORS = new int[]{16449536, 2161659, 16457979, 15867, 16513792, 64256};

   public GeyserEidosTile(BlockPos pos, BlockState state) {
      super(TilesST.GEYSER_EIDOS_MAIN, pos, state);
   }

   public void update() {
      if (this.level != null) {
         if (this.parent == null) {
            BlockPos pos = this.getBlockPos();
            Direction facing = (Direction)this.level.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING);
            Vec3 basePos = Vec3.atCenterOf(pos);
            if (facing == Direction.WEST) {
               basePos = basePos.add(0.5, 0.0, 0.5);
            } else if (facing == Direction.EAST) {
               basePos = basePos.add(-0.5, 0.0, -0.5);
            } else if (facing == Direction.SOUTH) {
               basePos = basePos.add(0.5, 0.0, -0.5);
            } else if (facing == Direction.NORTH) {
               basePos = basePos.add(-0.5, 0.0, 0.5);
            }

            if (this.ticksExisted % 80 == 0) {
               this.spawnSmoke(this.level, basePos, 1.0F, 15);
            }

            if (this.ticksExisted % 2400 == 0) {
               this.geyserTimer = 60;
               this.levitationTimer = 80;
               UtilST.dropEidos(this.level, Vec3.atCenterOf(pos), 1, false);
            }

            if (this.geyserTimer > 0) {
               this.spawnGeyser(this.level, basePos, 12);
               this.geyserTimer--;
            }

            if (this.levitationTimer > 0) {
               this.applyLevitation(this.level, basePos, 6.0F);
               this.levitationTimer--;
            }

            AABB area = new AABB(pos).inflate(0.5, 0.5, 0.5);

            for (Player player : this.level.getEntitiesOfClass(Player.class, area)) {
               if (this.ticksExisted % 20 == 0 && !player.isCreative()) {
                  float currentHP = player.getHealth();
                  float newHP = Math.max(currentHP - 0.3F, 0.0F);
                  player.setHealth(newHP);
                  player.hurtMarked = false;
                  player.hurtDuration = 0;
                  player.hurtTime = 0;
               }
            }
         }
      }
   }

   private void spawnSmoke(Level level, Vec3 basePos, float liftHeight, int particleCount) {
      if (level != null) {
         Random random = new Random();

         for (int i = 0; i < particleCount; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double radius = random.nextDouble() * 0.4 + 0.1;
            double x = basePos.x + Math.cos(angle) * radius;
            double z = basePos.z + Math.sin(angle) * radius;
            double y = basePos.y + random.nextDouble() * (double)liftHeight * 0.5;
            double moveX = (random.nextDouble() - 0.5) * 0.002;
            double moveY = 0.01 + random.nextDouble() * 0.01;
            double moveZ = (random.nextDouble() - 0.5) * 0.002;
            int base = BASE_COLORS[random.nextInt(BASE_COLORS.length)];
            Vector3f colorVec = this.getColorVector(base, level.getRandom());
            colorVec.mul(0.8F + random.nextFloat() * 0.4F);
            float alpha = 0.25F + random.nextFloat() * 0.5F;
            float scale = 0.15F + random.nextFloat() * 0.3F;
            float size = 0.05F + random.nextFloat() * 0.15F;
            int lifetime = 160;
            Vec3 spawnPos = new Vec3(x, y, z).add(Math.sin(angle) * 0.1, 0.2, Math.cos(angle) * 0.1);
            colorVec.set(Mth.clamp(colorVec.x(), 0.0F, 1.0F), Mth.clamp(colorVec.y(), 0.0F, 1.0F), Mth.clamp(colorVec.z(), 0.0F, 1.0F));
            STParticleData particle = new STParticleDataBuilder(
                  new STDefaultOptions(size, lifetime, new Color(colorVec.x(), colorVec.y(), colorVec.z()), 0.9F, true, false, true, false),
                  ParticleTypesST.WHITE_SMOKE
               )
               .setAlphaOptions(0, 100 + random.nextInt(30))
               .build();
            ParticleHelper.spawnEnginedParticle(
               level, particle, spawnPos, moveX, moveY * 0.55, moveZ, scale, lifetime, new Color(colorVec.x(), colorVec.y(), colorVec.z()), alpha
            );
         }
      }
   }

   private void spawnGeyser(Level level, Vec3 basePos, int particleCount) {
      Random random = new Random();

      for (int i = 0; i < particleCount; i++) {
         float phase = random.nextFloat();
         double angle = random.nextDouble() * Math.PI * 2.0;
         double radius = 0.05 + (double)phase * 0.35 * random.nextDouble();
         double x = basePos.x + Math.cos(angle) * radius;
         double z = basePos.z + Math.sin(angle) * radius;
         double y = basePos.y + 0.1 + (double)phase * (1.2 + random.nextDouble() * 0.3);
         double moveY = (0.25 + random.nextDouble() * 0.15) * (1.0 - (double)phase * 0.6);
         double moveX = (random.nextDouble() - 0.5) * (0.03 + 0.02 * (double)phase);
         double moveZ = (random.nextDouble() - 0.5) * (0.03 + 0.02 * (double)phase);
         int base = BASE_COLORS[random.nextInt(BASE_COLORS.length)];
         Vector3f colorVec = this.getColorVector(base, level.getRandom());
         colorVec.mul(0.8F + random.nextFloat() * 0.3F);
         colorVec.set(Mth.clamp(colorVec.x(), 0.0F, 1.0F), Mth.clamp(colorVec.y(), 0.0F, 1.0F), Mth.clamp(colorVec.z(), 0.0F, 1.0F));
         float scale = 1.2F + (1.8F - 1.2F * phase) + random.nextFloat() * 0.4F;
         float size = 0.25F + random.nextFloat() * 0.15F * (1.0F - phase);
         float alpha = 0.8F;
         int lifetime = (int)(20.0F + 40.0F * phase + (float)random.nextInt(30));
         if (phase > 0.7F) {
            moveY *= 0.5 + random.nextDouble() * 0.5;
            moveX += (random.nextDouble() - 0.5) * 0.02;
            moveZ += (random.nextDouble() - 0.5) * 0.02;
         }

         Vec3 spawnPos = new Vec3(x, y, z);
         STParticleData particle = new STParticleDataBuilder(
               new STDefaultOptions(size, lifetime, new Color(colorVec.x(), colorVec.y(), colorVec.z()), 0.9F, true, false, true, false),
               ParticleTypesST.WHITE_SMOKE
            )
            .setAlphaOptions(0, (int)(30.0F + 40.0F * phase))
            .build();
         ParticleHelper.spawnEnginedParticle(
            level, particle, spawnPos, moveX, moveY, moveZ, scale, lifetime, new Color(colorVec.x(), colorVec.y(), colorVec.z()), alpha
         );
      }
   }

   private void applyLevitation(Level level, Vec3 basePos, float liftHeight) {
      AABB liftArea = new AABB(basePos.x - 0.5, basePos.y, basePos.z - 0.5, basePos.x + 0.5, basePos.y + (double)liftHeight, basePos.z + 0.5)
         .inflate(0.5, 0.5, 0.5);

      for (Player player : level.getEntitiesOfClass(Player.class, liftArea)) {
         double relativeY = player.getY() - basePos.y;
         double liftStrength = 0.3 - relativeY / (double)liftHeight * 0.2;
         if (liftStrength < 0.05) {
            liftStrength = 0.05;
         }

         Vec3 motion = player.getDeltaMovement();
         double targetY = Math.max(motion.y(), liftStrength);
         player.setDeltaMovement(motion.x() * 0.9, targetY, motion.z() * 0.9);
         player.fallDistance = 0.0F;
      }
   }

   private Vector3f getColorVector(int baseColor, RandomSource rand) {
      int r = baseColor >> 16 & 0xFF;
      int g = baseColor >> 8 & 0xFF;
      int b = baseColor & 0xFF;
      float variation = 0.9F + rand.nextFloat() * 0.2F;
      float rf = Mth.clamp((float)r / 255.0F * variation, 0.0F, 1.0F);
      float gf = Mth.clamp((float)g / 255.0F * variation, 0.0F, 1.0F);
      float bf = Mth.clamp((float)b / 255.0F * variation, 0.0F, 1.0F);
      return new Vector3f(rf, gf, bf);
   }

   public AABB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }

   public void setupSystem(Builder builder) {
      builder.addLayers(new org.zeith.hammeranims.api.animsys.layer.AnimationLayer.Builder[]{AnimationLayer.builder("action")}).disableSync();
   }

   public AnimationSystem getAnimationSystem() {
      return this.animations;
   }
}

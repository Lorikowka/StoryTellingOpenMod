package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.client.particle.controllers.StandartController;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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

public class GeyserEtherTile extends TileLinkable implements IAnimatedTile {
   public final AnimationSystem animations = AnimationSystem.create(this);
   private static int[] BASE_COLORS = new int[]{16746751, 8965375, 16777215};

   public GeyserEtherTile(BlockPos pos, BlockState state) {
      super(TilesST.GEYSER_ETHER_MAIN, pos, state);
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

            if (this.ticksExisted % 20 == 0) {
               this.spawnEther(this.level, basePos.add(0.0, 0.5, 0.0), 15);
            }
         }
      }
   }

   private void spawnEther(Level level, Vec3 basePos, int particleCount) {
      if (level != null) {
         RandomSource random = level.getRandom();

         for (int i = 0; i < particleCount; i++) {
            int base = BASE_COLORS[random.nextInt(BASE_COLORS.length)];
            Vector3f colorVec = this.getColorVector(base, random);
            Color color = new Color(colorVec.x, colorVec.y, colorVec.z);
            double angle = random.nextDouble() * Math.PI * 2.0;
            double radius = random.nextDouble() * 0.35 + 0.5;
            double px = basePos.x + Math.cos(angle) * radius;
            double pz = basePos.z + Math.sin(angle) * radius;
            double py = basePos.y + random.nextDouble() * 0.05;
            double moveX = (random.nextDouble() - 0.5) * 0.002;
            double moveY = 0.004 + random.nextDouble() * 0.006;
            double moveZ = (random.nextDouble() - 0.5) * 0.002;
            float diameter = 0.1F + random.nextFloat() * 0.25F;
            int lifetime = 30 + random.nextInt(30);
            float alpha = 0.2F + random.nextFloat() * 0.3F;
            CircleTintData circleTintData = new CircleTintData(color, diameter, 30, lifetime, -1.0F, true, false, new StandartController());
            ParticleHelper.spawnEnginedParticle(level, circleTintData, px, py, pz, moveX, moveY, moveZ, 1.0F, lifetime, color, alpha);
         }
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

package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.content.blocks.TrapChestBlock;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TrapChestTile extends ChestBlockEntity {
   private int tickCount = 0;
   public float lidProgress = 0.0F;
   private Random random = new Random();

   public TrapChestTile(BlockPos pos, BlockState state) {
      super(TilesST.TRAP_CHEST, pos, state);
   }

   public void tick(Level level, BlockState state, BlockPos pos) {
      this.tickCount++;
      if ((Boolean)state.getValue(TrapChestBlock.OPENED)) {
         this.spawnEidosParticles(Vec3.atCenterOf(pos).add(0.0, -0.15, 0.0));
      }

      float target = state.getValue(TrapChestBlock.OPENED) ? 1.0F : 0.0F;
      float speed = 0.35F;
      if (this.lidProgress < target) {
         this.lidProgress += speed;
         if (this.lidProgress > target) {
            this.lidProgress = target;
         }
      } else if (this.lidProgress > target) {
         this.lidProgress -= speed;
         if (this.lidProgress < target) {
            this.lidProgress = target;
         }
      }
   }

   private void spawnEidosParticles(Vec3 pos) {
      if (this.tickCount % 4 == 0) {
         int[] colors = new int[]{65535, 16711935, 16776960, 4835660, 255, 16711680};
         int count = 2;

         for (int i = 0; i < count; i++) {
            int color = colors[this.random.nextInt(colors.length)];
            float height = 0.5F;
            double px = pos.x();
            double py = pos.y() + (double)height / 2.0 + 0.1F;
            double pz = pos.z();
            double angle = this.random.nextDouble() * Math.PI * 2.0;
            double speed = 0.005;
            double speedX = Math.cos(angle) * speed;
            double speedZ = Math.sin(angle) * speed;
            double speedY = (this.random.nextDouble() - 0.5) * 0.01;
            CircleTintData circleTintData = new CircleTintData(new Color(color), 0.05F + this.random.nextFloat() * 0.04F, 80, -1.0F, false, false);
            ParticleHelper.spawnDirectedParticle(this.level, circleTintData, px, py, pz, speedX, speedY, speedZ);
         }
      }
   }
}

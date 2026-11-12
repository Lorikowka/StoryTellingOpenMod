package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.client.particle.controllers.StandartController;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

public class EtherealFlashTile extends TileSyncableTickable {
   private static int[] BASE_COLORS = new int[]{9795583, 8228095, 5879807, 16734421, 9158911, 15510527};

   public EtherealFlashTile(BlockPos pos, BlockState state) {
      super(TilesST.ETHEREAL_FLASH, pos, state);
   }

   public void update() {
      if (this.level != null) {
         if (this.ticksExisted % 2 == 0) {
            Vec3 pos = new Vec3((double)this.getBlockPos().getX(), (double)this.getBlockPos().getY(), (double)this.getBlockPos().getZ());
            this.spawnParticles(this.level, pos.add(0.5, 0.5, 0.5));
         }
      }
   }

   private void spawnParticles(Level level, Vec3 pos) {
      RandomSource random = level.getRandom();
      int count = 2;

      for (int i = 0; i < count; i++) {
         int[] BASE_COLORS = new int[]{6965982, 5991403, 4037087, 14367667, 9158911, 15510527, 13076991};
         int base = BASE_COLORS[random.nextInt(BASE_COLORS.length)];
         Vector3f colorVec = this.getColorVector(base, random);
         Color color = new Color(colorVec.x, colorVec.y, colorVec.z);
         float diameterScale = 2.0F;
         double px = pos.x() + (random.nextDouble() - 0.5) * 0.06 * (double)diameterScale;
         double py = pos.y() + 0.05 + random.nextDouble() * 0.05 * (double)diameterScale;
         double pz = pos.z() + (random.nextDouble() - 0.5) * 0.06 * (double)diameterScale;
         double speedX = (random.nextDouble() - 0.5) * 0.01 * (double)diameterScale;
         double speedY = 0.02 + random.nextDouble() * 0.03 * (double)diameterScale;
         double speedZ = (random.nextDouble() - 0.5) * 0.01 * (double)diameterScale;
         float diameter = 0.15F * diameterScale + random.nextFloat() * (0.3F * diameterScale - 0.15F * diameterScale);
         CircleTintData circleTintData = new CircleTintData(
            color,
            diameter,
            10,
            (int)(20.0F * diameterScale + (float)random.nextInt((int)(20.0F * diameterScale))),
            -1.0F,
            false,
            false,
            new StandartController()
         );
         float min = 0.2F;
         float max = 0.5F;
         float alpha = min + random.nextFloat() * (max - min);
         ParticleHelper.spawnEnginedParticle(
            level,
            circleTintData,
            px,
            py,
            pz,
            speedX * 0.25,
            speedY * 0.25,
            speedZ * 0.25,
            1.0F,
            (int)(20.0F * diameterScale + (float)random.nextInt((int)(20.0F * diameterScale))),
            color,
            alpha
         );
      }
   }

   private Vector3f getColorVector(int baseColor, RandomSource rand) {
      int r = baseColor >> 16 & 0xFF;
      int g = baseColor >> 8 & 0xFF;
      int b = baseColor & 0xFF;
      float variation = 0.75F + rand.nextFloat() * 0.5F;
      float rf = this.clamp((float)r / 255.0F * variation, 0.0F, 1.0F);
      float gf = this.clamp((float)g / 255.0F * variation, 0.0F, 1.0F);
      float bf = this.clamp((float)b / 255.0F * variation, 0.0F, 1.0F);
      return new Vector3f(rf, gf, bf);
   }

   private float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   public AABB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }
}
